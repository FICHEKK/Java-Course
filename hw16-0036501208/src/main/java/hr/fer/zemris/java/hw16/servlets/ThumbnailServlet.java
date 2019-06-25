package hr.fer.zemris.java.hw16.servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw16.gallery.Picture;
import hr.fer.zemris.java.hw16.gallery.PicturesDB;

/**
 * A simple servlet that accesses the internal WEB-INF directory,
 * fetches (or creates if needed) the thumbnail picture and passes 
 * it to the response output stream.
 *
 * @author Filip Nemec
 */
@WebServlet(urlPatterns = {"/thumbnail"})
public class ThumbnailServlet extends HttpServlet {
	
	/** Used for serialization. */
	private static final long serialVersionUID = -1462568921263423219L;
	
	/** The location of the original size pictures directory. */
	private static final String ORIGINAL_DIR = "/WEB-INF/slike";
	
	/** The location of the thumbnail directory. */
	private static final String THUMBNAIL_DIR = "/WEB-INF/thumbnails";
	
	/** The width of the thumbnail. */
	private static final int THUMBNAIL_WIDTH = 150;
	
	/** The height of the thumbnail. */
	private static final int THUMBNAIL_HEIGHT = 150;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Check the existance of "thumbnails" folder.
        Path dir = Paths.get(req.getServletContext().getRealPath(THUMBNAIL_DIR));

        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }
        
        // Get specified picture and create a thumbnail for it (if needed).
        String title = req.getParameter("title");
        createThumbnail(req.getServletContext(), PicturesDB.getPictureWithTitle(title));
		
        // Set the content type to "image" and pass the image to the output stream.
        resp.setContentType("image/jpg");

        BufferedImage image = ImageIO.read(new File(req.getServletContext().getRealPath(THUMBNAIL_DIR + "/" + title)));
        OutputStream os = resp.getOutputStream();
        ImageIO.write(image, "jpg", os);
        os.close();
	}
	
    /**
     * Creates the thumbnail (minimized picture of the original high
     * resolution picture) of all the provided picture, unless
     * the thumbnail already exists.
     *
     * @param context the servlet context
     * @param picture the picture that is being checked for thumbnail
     * @throws IOException if an IO error occurs
     */
	private void createThumbnail(ServletContext context, Picture picture) throws IOException {
        Path thumbnail = Paths.get(context.getRealPath(THUMBNAIL_DIR + "/" + picture.getTitle()));

        // Create and save a thumbnail if it does not exist.
        if (!Files.exists(thumbnail)) {
            BufferedImage originalImage = ImageIO.read(new File(context.getRealPath(ORIGINAL_DIR + "/" + picture.getTitle())));
            BufferedImage thumbnailImage = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
            thumbnailImage.createGraphics().drawImage(originalImage.getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
            
            ImageIO.write(thumbnailImage, "jpg", thumbnail.toFile());
        }
	}
}
