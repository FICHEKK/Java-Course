package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Models a smart HTTP based server.
 *
 * @author Filip Nemec
 */
public class SmartHttpServer {
	
	/** The address that this server listens to. */
	private String address;
	
	/** The domain name of this server. */
	private String domainName;
	
	/** The port that this server listens to. */
	private int port;
	
	/** The number of threads used in the thread pool. */
	private int workerThreads;
	
	/** The root directory for serving files. */
	private Path documentRoot;
	
	/** All of the supported mime types. */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	
	/** The server thread. */
	private ServerThread serverThread;
	
	/** The thread pool used by this server. */
	private ExecutorService threadPool;
	
	/** Maps the path to its {@code IWebWorker}. */
	private Map<String,IWebWorker> workersMap = new HashMap<>();
	
	/** The duration of user sessions in seconds. */
	private int sessionTimeout;
	
	/** A map of sessions. */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	
	/** Randomization object used by the session. */
	private Random sessionRandom = new Random();

	/**
	 * Constructs a new smart HTTP server.
	 *
	 * @param configFileName the name of the server configuration file
	 * @throws IOException if error during file reading occurs
	 */
	public SmartHttpServer(String configFileName) throws IOException {
		// Server properties
		Properties server = new Properties();
		server.load(Files.newInputStream(Paths.get("./config/" + configFileName)));
		
		this.address        = server.getProperty("server.address");
		this.domainName     = server.getProperty("server.domainName");
		this.port           = Integer.parseInt( server.getProperty("server.port") );
		this.workerThreads  = Integer.parseInt( server.getProperty("server.workerThreads") );
		this.sessionTimeout = Integer.parseInt( server.getProperty("session.timeout") );
		this.documentRoot   = Paths.get( server.getProperty("server.documentRoot") );
		
		// Workers properties
		Properties workers = new Properties();
		workers.load(Files.newInputStream(Paths.get(server.getProperty("server.workers"))));
		workers.stringPropertyNames().forEach(path -> {
			try {
				String fqcn = workers.getProperty(path);
				workersMap.put(path, getWebWorkerFromFQCN(fqcn));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// Mime properties
		Properties mime = new Properties();
		mime.load(Files.newInputStream(Paths.get(server.getProperty("server.mimeConfig"))));
		mime.stringPropertyNames().forEach(key -> this.mimeTypes.put(key, mime.getProperty(key)));
	}

	/**
	 * Starts this HTTP server.
	 */
	protected synchronized void start() {
		if(serverThread == null) {
			System.out.println("Starting the server with address " + address + " and port " + port +"...");
			
			serverThread = new ServerThread();
			serverThread.start();
			threadPool = Executors.newFixedThreadPool(workerThreads);
		}
	}

	/**
	 * Stops this HTTP server.
	 */
	protected synchronized void stop() {
		serverThread.interrupt();
		threadPool.shutdown();
	}

	/**
	 * Models the work done by this smart HTTP server
	 * as a separate thread.
	 *
	 * @author Filip Nemec
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try(ServerSocket serverSocket = new ServerSocket()) {
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
				
				SessionCleaner sessionCleaner = new SessionCleaner();
				sessionCleaner.setDaemon(true);
				sessionCleaner.start();
				
				while(true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//----------------------------------------------------------
	//						  main
	//----------------------------------------------------------
	
	/**
	 * Starts the server with the provided properties.
	 *
	 * @param args none are used
	 * @throws IOException if an IO error occurs
	 */
	public static void main(String[] args) throws IOException {
		SmartHttpServer server = new SmartHttpServer("server.properties");
		server.start();
	}
	
	//----------------------------------------------------------
	//					  CLIENT WORKER
	//----------------------------------------------------------

	/**
	 * Processes the client's request.
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		
		/** The client's socket. */
		private Socket csocket;
		
		/** The client's push-back input stream. */
		private PushbackInputStream istream;
		
		/** The client's output stream. */
		private OutputStream ostream;
		
		/** The client HTTP version. */
		private String version;
		
		/** The HTTP header method. */
		private String method;
		
		/** The host. */
		private String host;
		
		/** The parameters. */
		private Map<String, String> params = new HashMap<String, String>();
		
		/** The temporary parameters. */
		private Map<String, String> tempParams = new HashMap<String, String>();
		
		/** The persistent parameters. */
		private Map<String, String> permPrams = new HashMap<String, String>();
		
		/** The output cookies. */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		
		/** The session ID. */
		private String SID;
		
		/** This client's request. */
		private RequestContext context = null;

		/** 
		 * Constructs a new client worker for the given socket.
		 *
		 * @param csocket the client's socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				this.istream = new PushbackInputStream(new BufferedInputStream(csocket.getInputStream()));
				this.ostream = new BufferedOutputStream(csocket.getOutputStream());
			
				List<String> headers = getHeaderLines();
			
				// Process the first line
				String[] firstLine = headers.get(0).split("\\s+");
				this.method  = firstLine[0].toUpperCase();
				this.version = firstLine[2].toUpperCase();
				
				if(!method.equals("GET") || (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1"))) {
					sendError(ostream, 400, "Bad request");
					return;
				}

				initHost(headers);
				checkSession(headers);
				
				// Resolving the request path.
				String requestedPath = firstLine[1];
				
				String[] requestedPathParts = requestedPath.split("\\?");
				String path = requestedPathParts[0];
				
				if(requestedPathParts.length == 2) {
					String paramString = requestedPathParts[1];
					parseParameters(paramString);
				}
				
				internalDispatchRequest(path, true);

				ostream.flush();
				csocket.close();
				
			} catch (Exception e) {
				System.err.println("Error occurred: " + e.getMessage());
				return;
			}
		}
		
		/**
		 * Processes this client's session.
		 *
		 * @param headers the headers sent by the client
		 */
		private void checkSession(List<String> headers) {
			String sidCandidate = null;
			
			for(String line : headers) {
				if(!line.startsWith("Cookie:")) continue;
					
				String cookies = line.trim().split("\\s+")[1];
				
				for(String cookie : cookies.split(";")) {
					String[] parts = cookie.split("=");
					String name  = parts[0];
					String value = parts[1].substring(1, parts[1].length() - 1);
					
					if(name.toLowerCase().equals("sid")) {
						sidCandidate = value;
						break;
					}
				}
				
				break;
			}
			
			if(sidCandidate == null) {
				generateNewSession();
				
			} else {
				SessionMapEntry session = sessions.get(sidCandidate);
				
				if(session == null) {
					session = generateNewSession();
					
				} else if(!session.host.equals(this.host)) {
					session = generateNewSession();

				} else if(session.validUntil < System.currentTimeMillis()) {
					sessions.remove(sidCandidate);
					session = generateNewSession();

				} else {
					session.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
					
				}
				
				permPrams = session.map;
			}
		}
		
		/**
		 * A helping method that generates a brand new session and returns it.
		 *
		 * @return a freshly created {@code SessionMapEntry}
		 */
		private SessionMapEntry generateNewSession() {
			SID = generateSessionID();
			
			SessionMapEntry session = new SessionMapEntry();
			session.sid = SID;
			session.host = host;
			session.validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
			session.map = new ConcurrentHashMap<String, String>();
			
			sessions.put(session.sid, session);
			outputCookies.add(new RCCookie("sid", SID, null, host, "/"));
			
			return session;
		}

		/**
		 * Processes the dispatch request.
		 *
		 * @param urlPath the URL to process
		 * @param directCall if this request is being called directly
		 * @throws Exception if an error occurred
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if(directCall && urlPath.startsWith("/private/")) {
				sendError(ostream, 404, "File not found");
				return;
			}
			
			if(workersMap.containsKey(urlPath)) {
				IWebWorker worker = workersMap.get(urlPath);
				
				if(context == null) {
					context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
				}
				worker.processRequest(context);
				return;
			}
			
			if(urlPath.startsWith("/ext/")) {
				// substring(5) = skip the "/ext/" prefix
				String fqcn = "hr.fer.zemris.java.webserver.workers." + urlPath.substring(5);
				IWebWorker iww = getWebWorkerFromFQCN(fqcn);
				
				if(context == null) {
					context = new RequestContext(ostream, params, permPrams, outputCookies, SID);
				}
				iww.processRequest(context);
				return;
			}
			
			Path resolvedPath = documentRoot.resolve(urlPath.substring(1));

			if(!Files.isReadable(resolvedPath)) {
				sendError(ostream, 404, "File not found.");
				return;
			}
			
			String extension = getExtensionFromPath(resolvedPath);
			
			if(extension.endsWith("smscr")) {
				if(context == null) {
					context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
				}

				new SmartScriptEngine(
						new SmartScriptParser(readFromDisk(resolvedPath)).getDocumentNode(),
						context
				).execute();
				
			} else {
				String mimeType = (mimeTypes.get(extension) != null) ? mimeTypes.get(extension) : "application/octet-stream";
				
				// Set-up the request context.
				if(context == null) {
					context = new RequestContext(ostream, params, permPrams, outputCookies, SID);
					context.setMimeType(mimeType);
					context.setStatusCode(200);
				}
				
				context.write(Files.readAllBytes(resolvedPath));
			}
		}
		
		/**
		 * Returns the file extension from the provided file path.
		 *
		 * @param filePath the path of the file
		 * @return the file extension from the provided file path
		 */
		private String getExtensionFromPath(Path filePath) {
			String extension = "";
			String path = filePath.toString();
					
			int i = path.lastIndexOf('.');
			if (i > 0) {
			    extension = path.substring(i+1);
			}
			
			return extension;
		}

		/**
		 * A helper method that processes the header and returns
		 * the processed header lines.
		 *
		 * @return the processed header lines
		 * @throws Exception if an error occured
		 */
		private List<String> getHeaderLines() throws Exception {
			// Get bytes.
			byte[] requestBytes = readRequest(istream);
			if (requestBytes == null) {
				sendError(ostream, 400, "Bad request");
				throw new Exception("Client's request is of invalid format.");
			}
			
			// Get header lines in a list.
			List<String> headerLines = extractHeaders(new String(requestBytes, StandardCharsets.US_ASCII));
			if(headerLines.isEmpty()) {
				sendError(ostream, 400, "Bad request");
				throw new Exception("Empty header.");
			}
			
			return headerLines;
		}

		/**
		 * Extracts the lines from the request header.
		 *
		 * @param requestHeader the request header
		 * @return the list of lines that the given
		 * 		   header consists of
		 */
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			
			for (String s : requestHeader.split("\n")) {
				if (s.isEmpty()) break;
				
				char c = s.charAt(0);
				if (c == '\t' || c == ' ') {
					currentLine += s;
	
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
						
					currentLine = s;
				}
			}
			
			if (!currentLine.isEmpty())
				headers.add(currentLine);

			return headers;
		}
		
		/**
		 * Initializes the host property if it was found
		 * in the header. If not found, host is set to the
		 * default server domain name.
		 *
		 * @param headerLines the header lines
		 */
		private void initHost(List<String> headerLines) {
			boolean hostHeaderFound = false;
			
			for(int i = 1, n = headerLines.size(); i < n; i++) {
				String line = headerLines.get(i);
				
				if(line.startsWith("Host:")) {
					String host = line.split("\\s+")[1];
					
					if(host.contains(":")) {
						host = host.split(":")[0];
					}
					
					this.host = host;
					hostHeaderFound = true;
					break;
				}
			}
			
			if(!hostHeaderFound) {
				this.host = domainName;
			}
		}
		
		/**
		 * Parses the given {@code paramString} into a series
		 * of key-value pairs. {@code paramString} will initially
		 * be of format "k1=v1&k2=v2&k3=v3...", so we firstly split
		 * it by the {@code &} character. After that, each key-value
		 * pair is being split by the {@code =} character. The splitted
		 * parts are finally put in the {@link #params} map.
		 *
		 * @param paramString the {@code String} to be parsed
		 */
		private void parseParameters(String paramString) {
			for(String param : paramString.split("&")) {
				String[] part = param.split("=");
				params.put(part[0], part[1]);
			}
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
	}
	
	//----------------------------------------------------------
	//					   HELPER METHODS
	//----------------------------------------------------------
	
	/**
	 * A helper method for sending the error response to the client.
	 *
	 * @param cos the client output stream
	 * @param statusCode the status code
	 * @param statusText the status text
	 * @throws IOException if error during writing occurs
	 */
	private static void sendError(OutputStream cos, int statusCode, String statusText) {
		try {
			cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
					   "Server: simple java server\r\n" +
					   "Content-Type: text/plain;charset=UTF-8\r\n" +
					   "Content-Length: 0\r\n" +
					   "Connection: close\r\n" +
					   "\r\n").getBytes(StandardCharsets.US_ASCII));
			cos.flush();
			
		} catch (IOException e) {
			System.err.println("Error during the output stream writing.");
			e.printStackTrace();
		}
	}
	
	/** 
	 * A simple automaton for finding the end of the header.
	 *
	 * @param is the input stream
	 * @return the whole header as a {@code byte[]}
	 * @throws IOException if IO error occurs
	 */
	private static byte[] readRequest(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
		int state = 0;
		l: while (true) {
			int b = is.read();
			
			if (b == -1) return null;
			
			if (b != 13) {
				bos.write(b);
			}
			
			switch (state) {
				case 0: if (b == 13) state = 1; else if (b == 10) state = 4; break;
				case 1: if (b == 10) state = 2; else state = 0; break;
				case 2: if (b == 13) state = 3; else state = 0; break;
				case 3: if (b == 10) break l; else state = 0; break;
				case 4: if (b == 10) break l; else state = 0; break;
			}
		}
		return bos.toByteArray();
	}

	/**
	 * Returns the text stored in the given file as a single
	 * {@code String}.
	 * 
	 * @param filePath the file path
	 * @return {@code String} of the file contents, or {@code null}
	 * 		   if exception occurred
	 */
	private static String readFromDisk(Path filePath) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try (InputStream is = Files.newInputStream(filePath)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int r = is.read(buffer);
				if (r < 1) break;
				bos.write(buffer, 0, r);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
			
		}
	}
	
	/**
	 * Returns an instance of {@code IWebWorker} for the provided
	 * FQCN (fully qualified class name).
	 *
	 * @param fqcn the fully qualified class name
	 * @return an instance of {@code IWebWorker} for the provided FQCN
	 */
	@SuppressWarnings("deprecation")
	private IWebWorker getWebWorkerFromFQCN(String fqcn) {
		try {
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			return (IWebWorker) referenceToClass.newInstance();
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Generates a random {@code String} that consists
	 * of 20 random upper-case letters.
	 *
	 * @return a random session ID
	 */
	private String generateSessionID() {
		var sb = new StringBuilder();
		final int SID_LENGTH = 20;
		
		for(int i = 0; i < SID_LENGTH; i++) {
			sb.append((char)('A' + sessionRandom.nextInt(26)));
		}
		
		return sb.toString();
	}
	
	//----------------------------------------------------------
	//					  SESSION MAP ENTRY	
	//----------------------------------------------------------
	
	/**
	 * Models a session entry.
	 *
	 * @author Filip Nemec
	 */
	private static class SessionMapEntry {
		
		/** The session id. */
		String sid;
		
		/** The session host. */
		String host;
		
		/** The session timeout time. */
		long validUntil;
		
		/** The session data storage. */
		Map<String, String> map;
	}
	
	//----------------------------------------------------------
	//					  SESSION CLEANER
	//----------------------------------------------------------
	
	/**
	 * A helping daemonic thread used for cleaning the expired sessions.
	 *
	 * @author Filip Nemec
	 */
	private class SessionCleaner extends Thread {
		
		@Override
		public void run() {
			while(true) {
				var iter = sessions.entrySet().iterator();
				for(; iter.hasNext(); ) {
					SessionMapEntry session = iter.next().getValue();
					
					if(session.validUntil < System.currentTimeMillis()) {
						iter.remove();
					}
				}

				try {
					Thread.sleep(300_000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
