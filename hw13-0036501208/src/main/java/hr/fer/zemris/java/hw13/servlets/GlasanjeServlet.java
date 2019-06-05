package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that prepares the list of the possible bands
 * that can be voted for. Bands are sorted in descending
 * order by their IDs.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Path filePath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
		List<Band> bands = getBandList(filePath);
		
		Collections.sort(bands, Band.BY_ID_ASC);
		
		req.getSession().setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	private List<Band> getBandList(Path filePath) throws IOException {
		List<String> bandStrings = Files.readAllLines(filePath);
		List<Band> bands = new LinkedList<>();
		
		for(String bandString : bandStrings) {
			String[] parts = bandString.split("\\t");
			bands.add(new Band(parts[0], parts[1], parts[2]));
		}
		
		return bands;
	}
	
	/**
	 * Representation of a single band.
	 *
	 * @author Filip Nemec
	 */
	public static class Band {
		
		/** Comparator used for sorting in the ascending order by band ID. */
		public static final Comparator<Band> BY_ID_ASC = (b1, b2) -> {
			Integer thisID = Integer.parseInt(b1.id);
			Integer otherID = Integer.parseInt(b2.id);
			return thisID.compareTo(otherID);
		};
		
		/** Comparator used for sorting in the descending order by band vote count. */
		public static final Comparator<Band> BY_VOTES_DESC = (b1, b2) -> {
			return b2.getVotes().compareTo(b1.getVotes());
		};
		
		/** The unique ID for the band. */
		public final String id;
		
		/** The name of the band. */
		public final String name;
		
		/** The link to some song performed by the band. */
		public final String link;
		
		/** The number of votes that this band has. */
		private Integer votes;
		
		/**
		 * Constructs a new band.
		 *
		 * @param id the band id
		 * @param name the band name
		 * @param link the link to some band's song
		 */
		public Band(String id, String name, String link) {
			this.id = id;
			this.name = name;
			this.link = link;
		}
		
		/**
		 * Changes the vote count for this band.
		 *
		 * @param votes new vote count
		 */
		public void setVotes(Integer votes) {
			this.votes = votes;
		}
		
		/**
		 * Returns the vote count of this band
		 *
		 * @return the vote count of this band
		 */
		public Integer getVotes() {
			return votes;
		}
	}
}
