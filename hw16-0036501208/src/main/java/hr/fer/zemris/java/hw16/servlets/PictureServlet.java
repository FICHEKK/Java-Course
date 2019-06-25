package hr.fer.zemris.java.hw16.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple servlet that accesses the internal WEB-INF directory,
 * fetches the original picture and passed it to the response
 * output stream.
 *
 * @author Filip Nemec
 */
@WebServlet(urlPatterns = {"/picture"})
public class PictureServlet extends HttpServlet {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 699381167842542146L;
	
	/** The location of the original size pictures directory. */
	private static final String ORIGINAL_DIR = "/WEB-INF/slike";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpg");

        BufferedImage image = ImageIO.read(new File(req.getServletContext().getRealPath(ORIGINAL_DIR + "/" + req.getParameter("title"))));
        OutputStream os = resp.getOutputStream();
        ImageIO.write(image, "jpg", os);
        os.close();
	}
}
