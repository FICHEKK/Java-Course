package hr.fer.zemris.java.hw13.servlets.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listens for the start of the web-application. It simply saves
 * the starting time as an attribute in the servlet context.
 *
 * @author Filip Nemec
 */
@WebListener
public class WebApplicationStartListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("startTime", System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}