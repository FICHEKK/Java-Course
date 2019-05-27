package hr.fer.zemris.java.webserver;

/**
 * Models the request dispatching objects.
 *
 * @author Filip Nemec
 */
public interface IDispatcher {
	
	/**
	 * Dispatches the request for the given {@code urlPath}.
	 *
	 * @param urlPath the URL path
	 * @throws Exception if an error occurred
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
