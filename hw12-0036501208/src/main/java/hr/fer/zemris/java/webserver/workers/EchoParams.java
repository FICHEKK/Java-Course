package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * A simple worker that echoes the given
 * parameters in a format of an HTML table.
 *
 * @author Filip Nemec
 */
public class EchoParams implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) {
		try {
			var sb = new StringBuilder();
			sb.append("<table style=\"width:50%\">\r\n");
			sb.append("<tr>\r\n");
			sb.append("<th>Parameter name</th>\r\n");
			sb.append("<th>Parameter value</th>\r\n");
			sb.append("</tr>\r\n");
			
			context.getParameterNames().forEach(name -> {
				sb.append("<tr>\r\n");
				
				sb.append("<td>");
				sb.append(name);
				sb.append("</td>\r\n");
				
				sb.append("<td>");
				sb.append(context.getParameter(name));
				sb.append("</td>\r\n");
				
				sb.append("</tr>\r\n");
			});
			
			sb.append("</table>");
			
			context.write(sb.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}