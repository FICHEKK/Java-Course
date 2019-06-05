package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that updates the "results" file once
 * the user has voted.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeGlasajServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		updateResults(
				Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt")),
				req.getParameter("id")
		);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
	/**
	 * Updates the "results" file.
	 *
	 * @param filePath the path to the "results" file
	 * @param id the ID of the band that was voted for
	 * @throws IOException if an IO error occurs
	 */
	private void updateResults(Path filePath, String id) throws IOException {
		if(!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		
		List<String> fileLines = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));

		boolean bandFound = false;
		for (int i = 0; i < fileLines.size(); i++) {
			String line = fileLines.get(i);
			
			if(line.startsWith(id)) {
				String[] parts = line.split("\\t");
				Integer voteCount = Integer.parseInt(parts[1]);
				
				fileLines.set(i, id + "\t" + String.valueOf(voteCount + 1));
				bandFound = true;
				break;
			}
		}
		
		if(!bandFound) {
			fileLines.add(id + "\t" + String.valueOf(1));
		}

		Files.write(filePath, fileLines, StandardCharsets.UTF_8);
	}
}
