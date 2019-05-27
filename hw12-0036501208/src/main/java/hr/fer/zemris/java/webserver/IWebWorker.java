package hr.fer.zemris.java.webserver;

/**
 * Models objects that can process the given
 * request context.
 *
 * @author Filip Nemec
 */
public interface IWebWorker {
	
	/**
	 * Processes the request.
	 *
	 * @param context the request context
	 * @throws Exception if an error occurred
	 */
	public void processRequest(RequestContext context) throws Exception;
}
