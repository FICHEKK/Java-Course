package hr.fer.zemris.java.hw13.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.servlets.GlasanjeServlet.Band;

/**
 * Servlet that processes the voting results.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Integer> idVotesMap = getIdVotesMap(Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt")));
		List<Band> bands = (List<Band>) req.getSession().getAttribute("bands");
		
		List<Band> bandsWithVotes = new LinkedList<>();
		
		for(Band band : bands) {
			Integer votes = idVotesMap.get(band.id);
			if(votes == null) votes = 0;
			
			band.setVotes(votes);
			bandsWithVotes.add(band);
		}
		
		Collections.sort(bandsWithVotes, Band.BY_VOTES_DESC);
		
		req.getSession().setAttribute("bandsWithVotes", bandsWithVotes);
		req.setAttribute("mostVotedBands", getMostVotedBands(bandsWithVotes));
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Returns the map which maps the band ID to their number of votes.
	 *
	 * @param filePath the file containing the band IDs and votes
	 * @return the map which maps the band ID to their number of votes
	 * @throws IOException if an IO error occurs
	 */
	private Map<String, Integer> getIdVotesMap(Path filePath) throws IOException {
		Map<String, Integer> idVotesMap = new HashMap<>();
		
		try(BufferedReader br = Files.newBufferedReader(filePath)) {
			String line = null;
			
			while((line = br.readLine()) != null) {
				String[] parts = line.split("\\t");
				
				String id = parts[0];
				Integer votes = Integer.parseInt(parts[1]);
				idVotesMap.put(id, votes);
			}
		}
		
		return idVotesMap;
	}
	
	/**
	 * Returns the list of the bands with the most votes. There
	 * can be multiple bands if they have the same number of votes.
	 *
	 * @param sortedBands a list of bands that is sorted from highest
	 * 				      voted to the least voted
	 * @return the list of the bands with the most votes
	 */
	private List<Band> getMostVotedBands(List<Band> sortedBands) {
		List<Band> mostVotedBands = new LinkedList<>();
		
		if(!sortedBands.isEmpty()) {
			mostVotedBands.add(sortedBands.get(0));
			int mostVotes = sortedBands.get(0).getVotes();
			
			for(int i = 1; i < sortedBands.size(); i++) {
				int bandVotes = sortedBands.get(i).getVotes();
				if(bandVotes < mostVotes) break;
				
				mostVotedBands.add(sortedBands.get(i));
			}
		}
		
		return mostVotedBands;
	}
}
