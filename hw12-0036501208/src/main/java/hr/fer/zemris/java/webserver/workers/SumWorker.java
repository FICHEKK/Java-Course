package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * A simple worker that calculates the sum.
 *
 * @author Filip Nemec
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a, b;
		
		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch(NumberFormatException e) {
			a = 1;
		}
		
		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch(NumberFormatException e) {
			b = 2;
		}
		
		context.setTemporaryParameter("varA", String.valueOf(a));
		context.setTemporaryParameter("varB", String.valueOf(b));
		context.setTemporaryParameter("zbroj", String.valueOf(a + b));
		
		if((a + b) % 2 == 0) {
			context.setTemporaryParameter("imgName", "images/dog.jpg");
		} else {
			context.setTemporaryParameter("imgName", "images/cat.jpg");
		}

		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}
}
