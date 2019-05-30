package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Processes the web-page background color.
 *
 * @author Filip Nemec
 */
public class BgColorWorker implements IWebWorker {
	
	/** All of the valid hexadecimal characters. */
	private static final String VALID_HEX = "0123456789ABCDEFabcdef";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");
		
		if(color != null && isBgColorValid(color)) {
			context.setPersistentParameter("bgcolor", color);
			context.getDispatcher().dispatchRequest("/private/pages/bgcolorchanged.smscr");
			
		} else {
			context.getDispatcher().dispatchRequest("/private/pages/bgcolornotchanged.smscr");
			
		}
	}

	/**
	 * Checks if the given {@code bgColor} encoding is of valid
	 * format, which is specified as:
	 * <br> - Must be of length 6.
	 * <br> - All characters must be a valid hex character
	 *
	 * @param bgColor the {@code String} to be checked
	 * @return {@code true} if it is valid, {@code false} otherwise
	 */
	private static boolean isBgColorValid(String bgColor) {
		if(bgColor.length() != 6) return false;
		
		for(char c : bgColor.toCharArray()) {
			if(VALID_HEX.indexOf((int)c) < 0) {
				return false;
			}
		}
		
		return true;
	}
}
