package hr.fer.zemris.java.hw17.trazilica;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
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
	private static final Path ROOT_PATH = Paths.get("C:/Users/FICHEKK/Desktop/clanci");
	
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
			
				
			} else if(command.equals("results")) {
				
				
			} else if(command.equals("exit")) {
				System.out.println("Exiting...");
				break;
				
			} else {
				System.out.println("Invalid command.");
				
			}
		}
		
		scanner.close();
	}
	
	private static List<String> getWordsFromQuery(String query) {
		query = query.substring(5).trim();
		return new LinkedList<String>( Arrays.asList(query.split("\\s")) );
	}
}
