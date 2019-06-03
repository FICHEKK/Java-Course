package hr.fer.zemris.java.hw13.servleti;

import java.io.IOException;
import java.util.function.DoubleUnaryOperator;

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
public class TrigonometricServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String paramA = req.getParameter("a");
		String paramB = req.getParameter("b");
		
		int a, b;
		
		try {
			a = Integer.parseInt(paramA);
		} catch(NumberFormatException e) {
			a = 0;
		}
		
		try {
			b = Integer.parseInt(paramB);
		} catch(NumberFormatException e) {
			b = 360;
		}
		
		if(a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		b = Math.min(b, a + 720);
		
		req.setAttribute("a", a);
		req.setAttribute("b", b);
		req.setAttribute("sinValues", getValues(a, b, Math::sin));
		req.setAttribute("cosValues", getValues(a, b, Math::cos));

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	private double[] getValues(int a, int b, DoubleUnaryOperator operator) {
		double[] values = new double[b - a + 1];
		
		int i = 0;
		for(; a <= b; a++) {
			values[i++] = operator.applyAsDouble(Math.toRadians(a));
		}
		
		return values;
	}
}
