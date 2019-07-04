package hr.fer.zemris.java.hw17.trazilica;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Models a console that is an interface to the
 * search engine.
 *
 * @author Filip Nemec
 */
public class Konzola {
	
	/** The root of the hierarchy containing all of the documents. */
	private static final Path ROOT_PATH = Paths.get("src/main/resources/clanci");
	
	/** Location of all the stop-words. */
	private static final Path STOPWORD_PATH = Paths.get("src/main/resources/hrvatski_stoprijeci.txt");
	
	/**
	 * Program starts from here.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		SearchEngine engine = new SearchEngine(ROOT_PATH, STOPWORD_PATH);
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.print("Enter command > ");
			String command = scanner.nextLine().trim();
				
			if(command.startsWith("query")) {
				engine.query(getWordsFromQuery(command));
				
			} else if(command.startsWith("type")) {
				engine.type(getNumberFromTypeCommand(command));
				
			} else if(command.equals("results")) {
				engine.printQueryResults();
				
			} else if(command.equals("exit")) {
				System.out.println("Exiting...");
				break;
				
			} else {
				System.out.println("Invalid command.");
				
			}
		}
		
		scanner.close();
	}
	
	/**
	 * Tries to parse the given "type" command to an integer.
	 * If the parsing fails, the default value of 0 will be
	 * returned.
	 *
	 * @param command the "type" command to be parsed
	 * @return the parsed integer, or 0 if parsing fails
	 */
	private static int getNumberFromTypeCommand(String command) {
		try {
			return Integer.parseInt(command.substring(4).trim());
			
		} catch (NumberFormatException e) {
			System.out.println("Failed to parse the to the integer. Returning the default value of 0.");
			return 0;
			
		}
	}

	/**
	 * Extracts the words from the query command.
	 *
	 * @param query the query command
	 * @return the list of words from the query command
	 */
	private static List<String> getWordsFromQuery(String query) {
		return Util.convertTextToWords( query.substring(5).trim() );
	}
}