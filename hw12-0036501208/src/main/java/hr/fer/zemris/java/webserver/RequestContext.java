package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Models the request context.
 *
 * @author Filip Nemec
 */
public class RequestContext {
	
	/** The output stream used by this request. */
	private OutputStream outputStream;
	
	/** The charset used by this request. */
	private Charset charset;
	
	/** The encoding used by this request. */
	private String encoding = "utf-8";
	
	/** The status code of this request. */
	private int statusCode = 200;
	
	/** The status text of this request. */
	private String statusText = "OK";
	
	/** The mime type used by this request. */
	private String mimeType = "text/html";
	
	/** The content length of this request. */
	private Long contentLength = null;
	
	/** A map of parameters. */
	private Map<String, String> parameters;
	
	/** A map of temporary parameters. */
	private Map<String,String> temporaryParameters = new HashMap<>();
	
	/** A map of persistent parameters. */
	private Map<String,String> persistentParameters;
	
	/** A list of output cookies. */
	private List<RCCookie> outputCookies;
	
	/** Flag that indicates whether the header has been generated. */
	private boolean headerGenerated = false;
	
	/** This request's dispatcher. */
	private IDispatcher dispatcher;
	
	/** The session ID. */
	private String sid;
	
	//=====================================================================================
	//									CONSTRUCTORS
	//=====================================================================================
	
	/**
	 * Constructs a new request context.
	 *
	 * @param outputStream the output stream
	 * @param parameters the parameters
	 * @param persistentParameters the persistent parameters
	 * @param outputCookies the output cookies
	 * @param sid the session id
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters,
						  Map<String,String> persistentParameters, List<RCCookie> outputCookies, String sid) {
		this.outputStream 		  = Objects.requireNonNull(outputStream);
		this.parameters 		  = (parameters != null)           ? parameters           : new HashMap<>();
		this.persistentParameters = (persistentParameters != null) ? persistentParameters : new HashMap<>();
		this.outputCookies 		  = (outputCookies != null)        ? outputCookies        : new LinkedList<>();
		this.sid = sid;
	}
	
	/**
	 * Constructs a new request context.
	 *
	 * @param outputStream the output stream
	 * @param parameters the parameters
	 * @param persistentParameters the persistent parameters
	 * @param outputCookies the output cookies
	 * @param temporaryParameters the temporary parameters
	 * @param dispatcher the dispatcher
	 * @param sid the session id
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters,
			  			  Map<String,String> persistentParameters, List<RCCookie> outputCookies,
			  			  Map<String, String> temporaryParameters, IDispatcher dispatcher, String sid) {
		this(outputStream, parameters, persistentParameters, outputCookies, sid);
		this.dispatcher 		  = Objects.requireNonNull(dispatcher);
		this.temporaryParameters  = (temporaryParameters != null)  ? temporaryParameters  : new HashMap<>();
	}
	
	//=====================================================================================
	//									WRITE DATA
	//=====================================================================================
	
	/**
	 * Writes the given data to the output stream of this request.
	 *
	 * @param data the data to be written
	 * @return the reference to this request
	 * @throws IOException if error during the writing occurs
	 */
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) generateHeader();
		
		outputStream.write(data);
		outputStream.flush();
		return this;
	}
	
	/**
	 * Writes the given data to the output stream of this request.
	 *
	 * @param data the data to be written
	 * @param offset the offset of the data
	 * @param len the length of the data
	 * @return the reference to this request
	 * @throws IOException if error during the writing occurs
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if(!headerGenerated) generateHeader();
		
		outputStream.write(data, offset, len);
		outputStream.flush();
		return this;
	}
	
	/**
	 * Writes the given data to the output stream of this request.
	 *
	 * @param text the text to be written
	 * @return the reference to this request
	 * @throws IOException if error during the writing occurs
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) generateHeader();
		
		outputStream.write(text.getBytes(encoding));
		outputStream.flush();
		return this;
	}
	
	/**
	 * Helper method that generates the header based
	 * on the current settings.
	 */
	private void generateHeader() {
		charset = Charset.forName(encoding);
		
		// HTTP version and status.
		String header = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n";
		
		// Append mime.
		if(mimeType.startsWith("text/")) {
			mimeType += ";charset=" + encoding;
		}
		
		header += "Content-Type: " + mimeType + "\r\n";
		
		// Append content length.
		if(contentLength != null) {
			header += "Content-Length: " + contentLength + "\r\n";
		}
		
		// Append cookies.
		for(RCCookie cookie : outputCookies) {
			header += convertCookieToHeaderLine(cookie);
		}
		
		// Append empty line to signal the end of header lines.
		header += "\r\n";
		
		try {
			outputStream.write(header.getBytes(charset));
			outputStream.flush();
			headerGenerated = true;
			
		} catch (IOException e) {
			System.err.println("Error during writing of the header.");
		}
	}
	
	/**
	 * Helper method that converts the given {@code RCCookie} object
	 * to its {@code String} header line.
	 *
	 * @param cookie the cookie to be converted
	 * @return the header line that is defined by the given cookie
	 */
	private String convertCookieToHeaderLine(RCCookie cookie) {
		Objects.requireNonNull(cookie);
		
		String line = "Set-Cookie: " + cookie.name + "=\"" + cookie.value + "\"";
		
		if(cookie.domain != null) line += "; Domain=" + cookie.domain;
		if(cookie.path != null)   line += "; Path=" + cookie.path;
		if(cookie.maxAge != null) line += "; Max-Age=" + cookie.maxAge;
		
		if(cookie.name.equals("sid")) line += "; HttpOnly";
		
		return line + "\r\n";
	}
	
	//=====================================================================================
	//									  GETTERS
	//=====================================================================================
	
	/**
	 * Returns the parameter for the specified name.
	 *
	 * @param name the name of the parameter
	 * @return the parameter for the specified name
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Returns the persistent parameter for the specified name.
	 *
	 * @param name the name of the persistent parameter
	 * @return the persistent parameter for the specified name
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Returns the temporary parameter for the specified name.
	 *
	 * @param name the name of the temporary parameter
	 * @return the temporary parameter for the specified name
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Returns the unmodifiable set of parameter names
	 *
	 * @return the unmodifiable set of parameter names
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Returns the unmodifiable set of persistent parameter names
	 *
	 * @return the unmodifiable set of persistent parameter names
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Returns the unmodifiable set of temporary parameter names
	 *
	 * @return the unmodifiable set of temporary parameter names
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Returns the session ID.
	 *
	 * @return the session ID
	 */
	public String getSessionID() {
		return sid;
	}
	
	/**
	 * Returns this request's dispatcher.
	 *
	 * @return this request's dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	//=====================================================================================
	//									  SETTERS
	//=====================================================================================
	
	/**
	 * Sets a new persistent parameter
	 *
	 * @param name the name of the persistent parameter
	 * @param value the value of the persistent parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Sets a new temporary parameter
	 *
	 * @param name the name of the temporary parameter
	 * @param value the value of the temporary parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Sets the encoding
	 *
	 * @param encoding the encoding
	 */
	public void setEncoding(String encoding) {
		requireHeaderNotGenerated();
		this.encoding = encoding;
	}
	
	/**
	 * Sets the status code
	 *
	 * @param statusCode the status code
	 */
	public void setStatusCode(int statusCode) {
		requireHeaderNotGenerated();
		this.statusCode = statusCode;
	}
	
	/**
	 * Sets the status text
	 *
	 * @param statusText the status text
	 */
	public void setStatusText(String statusText) {
		requireHeaderNotGenerated();
		this.statusText = statusText;
	}
	
	/**
	 * Sets the mime type
	 *
	 * @param mimeType the mime type
	 */
	public void setMimeType(String mimeType) {
		requireHeaderNotGenerated();
		this.mimeType = mimeType;
	}
	
	/**
	 * Sets the output cookies list
	 *
	 * @param outputCookies the output cookies list
	 */
	public void setOutputCookies(List<RCCookie> outputCookies) {
		requireHeaderNotGenerated();
		this.outputCookies = outputCookies;
	}
	
	/**
	 * Sets the content length
	 *
	 * @param contentLength the content length
	 */
	public void setContentLength(Long contentLength) {
		requireHeaderNotGenerated();
		this.contentLength = contentLength;
	}
	
	/**
	 * Helper method that ensures that the header has not been generated
	 * yet. It if was generated, throws a {@code RuntimeException}.
	 */
	private void requireHeaderNotGenerated() {
		if(headerGenerated)
			throw new RuntimeException("Header was already generated.");
	}
	
	/**
	 * Adds a new cookie to this request context.
	 *
	 * @param cookie the cookie to be added
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}
	
	//=====================================================================================
	//									  REMOVE
	//=====================================================================================
	
	/**
	 * Removes the specified persistent parameter.
	 *
	 * @param name the parameter to be removed
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Removes the specified temporary parameter.
	 *
	 * @param name the parameter to be removed
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	//=====================================================================================
	//								STATIC INNER CLASSES
	//=====================================================================================
	
	/**
	 * Models a single cookie.
	 *
	 * @author Filip Nemec
	 */
	public static class RCCookie {
		
		/** Name of the cookie. */
		public final String name;
		
		/** Value of the cookie. */
		public final String value;
		
		/** Domain of the cookie. */
		public final String domain;
		
		/** Path of the cookie. */
		public final String path;
		
		/** Maximum age of the cookie. */
		public final Integer maxAge;
		
		/**
		 * Constructs a new cookie.
		 *
		 * @param name the name of the cookie
		 * @param value the value of the cookie
		 * @param maxAge the maximum age of the cookie
		 * @param domain the domain of the cookie
		 * @param path the path of the cookie
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
	}
}
