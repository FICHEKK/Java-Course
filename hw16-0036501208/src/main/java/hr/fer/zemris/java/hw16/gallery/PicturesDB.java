package hr.fer.zemris.java.hw16.gallery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Representation of the picture database. It offers
 * the interface for fetching pictures. The database
 * gets filled from the local file upon the start
 * of the web-application.
 *
 * @author Filip Nemec
 */
@WebListener
public final class PicturesDB implements ServletContextListener {
	
	/** The path of the file that defines the picture database. */
	private static final String DB_DESCRIPTION_PATH = "/WEB-INF/opisnik.txt";
	
	/** A list of all the pictures in the database. */
	private static List<Picture> pictures = new LinkedList<>();
	
	/** A set containing all of the unique picture tags. */
	private static Set<String> tags = new HashSet<>();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Fills the database.
		try {
			List<String> lines = Files.readAllLines(Paths.get(sce.getServletContext().getRealPath(DB_DESCRIPTION_PATH)));
			
	        Iterator<String> iter = lines.iterator();
	        while(iter.hasNext()) {
	        	String title = iter.next();
	        	String description = iter.next();
	        	String[] tagArray = iter.next().split(",");
	        	
	        	for(int i = 0; i < tagArray.length; i++) {
	        		tagArray[i] = tagArray[i].trim();
	        		tags.add(tagArray[i]);
	        	}
	        	
	        	pictures.add(new Picture(title, description, tagArray));
	        }
	        
		} catch (IOException e) {
			System.err.println("Could not load the lines");
			return;
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Do nothing here.
	}
	
	/**
	 * Returns the list of all the pictures with the given tag.
	 *
	 * @param tag the required tag
	 * @return the list of all the pictures with the given tag
	 */
	public static final List<Picture> getPicturesWithTag(String tag) {
		List<Picture> result = new LinkedList<>();
		
		for(Picture picture : pictures) {
			for(String pictureTag : picture.getTags()) {
				if(pictureTag.equals(tag)) {
					result.add(picture);
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the {@code Picture} with the given property {@code title}.
	 *
	 * @param title the title of the {@code Picture}
	 * @return the {@code Picture} with the given property {@code title} or
	 * 			   {@code null} if not found
	 */
	public static final Picture getPictureWithTitle(String title) {
		for(Picture picture : pictures) {
			if(picture.getTitle().equals(title)) {
				return picture;
			}
		}
		
		return null;
	}

	/**
	 * Returns the set of all the unique picture tags in the database.
	 *
	 * @return the set of all the unique picture tags in the database
	 */
	public static final Set<String> getAllTags() {
		return tags;
	}
}
