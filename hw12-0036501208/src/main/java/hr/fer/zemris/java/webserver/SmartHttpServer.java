package hr.fer.zemris.java.webserver;

import java.awt.image.BufferedImage;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

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
	
	/** The duration of user sessions in seconds. */
	private int sessionTimeout;
	
	/** The root directory for serving files. */
	private Path documentRoot;
	
	/** All of the supported mime types. */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	
	/** The server thread. */
	private ServerThread serverThread;
	
	/** The thread pool used by this server. */
	private ExecutorService threadPool;

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
		
		// Mime properties
		Properties mime = new Properties();
		mime.load(Files.newInputStream(Paths.get(server.getProperty("server.mimeConfig"))));
		mime.stringPropertyNames().forEach(key -> this.mimeTypes.put(key, mime.getProperty(key)));
	}

	protected synchronized void start() {
		if(serverThread == null) {
			System.out.println("Starting the server for adress " + address + " and port " + port +"...");
			
			serverThread = new ServerThread();
			serverThread.start();
			threadPool = Executors.newFixedThreadPool(workerThreads);
		}
	}

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
				
				while(true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
			}
		}
	}
	
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
			
				// Initialize the host property.
				initHost(headers);
				
				// Resolving the request path.
				String requestedPath = firstLine[1];
				
				String[] requestedPathParts = requestedPath.split("\\?");
				String path = requestedPathParts[0];
				
				if(requestedPathParts.length == 2) {
					String paramString = requestedPathParts[1];
					parseParameters(paramString);
				}
				
				internalDispatchRequest(path, true);

				csocket.close();
				
			} catch (Exception e) {
				System.err.println("Error occurred: " + e.getMessage());
				return;
			}
		}
		
		
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			Path resolvedPath = documentRoot.resolve(urlPath.substring(1));

			if(!Files.isReadable(resolvedPath)) {
				sendError(ostream, 404, "File not found.");
				return;
			}
			
			String extension = getExtensionFromPath(resolvedPath);
			
			if(extension.endsWith("smscr")) {
				String documentBody = readFromDisk(resolvedPath);
				
				new SmartScriptEngine(
						new SmartScriptParser(documentBody).getDocumentNode(),
						new RequestContext(ostream,
										   params,
										   new HashMap<String, String>(),
										   new ArrayList<RequestContext.RCCookie>(),
										   new HashMap<String, String>(),
										   this)
				).execute();
				
			} else {
				String mimeType = (mimeTypes.get(extension) != null) ? mimeTypes.get(extension) :
																	   "application/octet-stream";
				
				// Set-up the request context.
				RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);
				rc.setMimeType(mimeType);
				rc.setStatusCode(200);
				
				// Send the requested file to the client.
				if(mimeType.endsWith("png") || mimeType.endsWith("jpg") || mimeType.endsWith("gif")) {
					rc.write(returnImageData(resolvedPath, mimeType.substring(mimeType.indexOf('/') + 1)));
					
				} else if(mimeType.endsWith("html") || mimeType.endsWith("txt")) {
					rc.write(Files.readAllBytes(resolvedPath));
					
				}
			}
		}
		
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
				throw new Exception("Request is of invalid format.");
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
		 * Returns the {@code byte[]} of the image at the given {@code Path}.
		 *
		 * @param imagePath the image path
		 * @param imageType the image type; for example "png" or "jpg"
		 * @return the image data
		 * @throws IOException if IO error occurs
		 */
		private byte[] returnImageData(Path imagePath, String imageType) throws IOException {
			BufferedImage image = ImageIO.read(imagePath.toFile());
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, imageType, bos);

			return bos.toByteArray();
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
			case 0:
				if (b == 13) {
					state = 1;
				} else if (b == 10)
					state = 4;
				break;
			case 1:
				if (b == 10) {
					state = 2;
				} else
					state = 0;
				break;
			case 2:
				if (b == 13) {
					state = 3;
				} else
					state = 0;
				break;
			case 3:
				if (b == 10) {
					break l;
				} else
					state = 0;
				break;
			case 4:
				if (b == 10) {
					break l;
				} else
					state = 0;
				break;
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
			return null;
			
		}
	}
}
