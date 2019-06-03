package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the web-page background color to the color
 * that was provided by the "color" parameter.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class SetColorServlet extends HttpServlet {
	
	/** An array of supported colors. */
	private static final String[] SUPPORTED_COLORS = {"FFFFFF", "FF0000", "00FF00", "00FFFF"};
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bgColor = req.getParameter("color");
		if(bgColor == null || !isBgColorSupported(bgColor)) {
			bgColor = "FFFFFF";
		}
		req.getSession().setAttribute("pickedBgCol", bgColor);
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	
	/**
	 * Checks if the given {@code bgColor} is supported
	 *
	 * @param bgColor the {@code String} to be checked
	 * @return {@code true} if it is supported, {@code false} otherwise
	 */
	private static boolean isBgColorSupported(String bgColor) {
		for(String supportedColor : SUPPORTED_COLORS) {
			if(bgColor.equals(supportedColor)) {
				return true;
			}
		}
		
		return false;
	}
}
