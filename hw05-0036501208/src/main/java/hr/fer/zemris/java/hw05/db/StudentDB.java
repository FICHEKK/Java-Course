package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple student database emulator that prompts
 * the user to test the database by injecting
 * simple queries.
 *
 * @author Filip Nemec
 */
public class StudentDB {
	
	/** The default database file path that is used for this emulator. */
	private static final String DEFAULT_DATABASE_PATH = "src/main/resources/database.txt";
	
	/**
	 * Student database emulator starts from here.
	 *
	 * @param args if none are provided, default database path will be used.
	 * 			   If arguments are passed, it should be a single {@code String}
	 * 			   containing the path to the desired database file.
	 */
	public static void main(String[] args) {
		String databasePath;
		
		if(args.length == 0) {
			databasePath = DEFAULT_DATABASE_PATH;
			
		} else if(args.length == 1) {
			databasePath = args[0];
			
		} else {
			System.err.println("A single argument should be passed: path to the database file.");
			return;
		}
		
		try {
			StudentDatabase db = loadDatabase(getDatabaseRows(databasePath));
			initializeUserPrompt(db);
			
		} catch(IOException ex) {
			System.err.println("Error loading from database base. Exiting...");
			return;
		}
	}
	
	/**
	 * Initializes the user prompt and allows the user to carry out queries.
	 *
	 * @param database the database being operated on
	 */
	private static void initializeUserPrompt(StudentDatabase database) {
		printInstructions();
		
		try(Scanner scanner = new Scanner(System.in)) {
			while(true) {
				System.out.print("> ");
				String instruction = scanner.next();
				
				if(instruction.equals("exit")) {
					System.out.println("Goodbye.");
					return;
					
				} else if(instruction.equals("help")) {
					printDetailedInstructions();
					continue;
					
				} else if(!instruction.equals("query")) {
					flushScanner(scanner);
					System.err.println("Query should start with a keyword 'query'. Was: '" + instruction + "'");
					System.err.println("If you wish to exit, type 'exit'.");
					System.err.println("For help, type 'help'.");
					continue;
				}
				
				String query = scanner.nextLine();
				processQuery(database, query);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Flushes the scanner's buffer.
	 *
	 * @param scanner the scanner to be flushed
	 */
	private static void flushScanner(Scanner scanner) {
		scanner.nextLine();
	}

	/**
	 * Processes and executes the given query {@code String}.
	 *
	 * @param database the database this query is being executed on
	 * @param query the query to be executed
	 */
	private static void processQuery(StudentDatabase database, String query) {
		try {
			QueryParser parser = new QueryParser(query);
			
			List<StudentRecord> records = new LinkedList<StudentRecord>();
			
			if(parser.isDirectQuery()) {
				records.add(database.forJMBAG(parser.getQueriedJMBAG()));
			} else {
				records = database.filter(new QueryFilter(parser.getQuery()));
			}
			
			List<String> formattedRecords = RecordFormatter.format(records);
			
			if(formattedRecords == null) {
				System.out.println("No records found.");
			} else {
				formattedRecords.forEach(r -> System.out.println(r));
				System.out.println("Records selected: " + (formattedRecords.size()));
			}
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println("If you are having problems with queries, type 'help'.");
		}
	}

	/**
	 * Prints the user welcome message and lists some simple
	 * operations that this database is capable of executing.
	 */
	private static void printInstructions() {
		System.out.println("Welcome to the database emulator!");
		System.out.println("This simple database offers some simple query statements:");
		System.out.println();
		System.out.println("query <fieldName> <comparisonOperator> <stringPattern>");
		System.out.println();
		System.out.println("'exit' to exit the emulator");
		System.out.println();
		System.out.println("'help' for more detailed instructions");
		System.out.println();
	}
	
	/**
	 * Prints the detailed instructions for each command.
	 */
	private static void printDetailedInstructions() {
		System.out.println("============================================================================");
		System.out.println("\t\t\t\tHELP");
		System.out.println("============================================================================");
		System.out.println();
		System.out.println("This database is of format: [ jmbag | lastName | firstName | finalGrade ]");
		System.out.println();
		System.out.println("query <fieldName> <comparisonOperator> <stringPattern>");
		System.out.println();
		System.out.println("\t- fieldName: 'jmbag', 'lastName' or 'firstName'");
		System.out.println("\t- comparisonOperator: >, >=, <, <=, !=, = or LIKE");
		System.out.println("\t- stringPattern: any string, however, there is an extra wildcard character: *\n");
		System.out.println("\tWildcard character represents zero or more of any character:");
		System.out.println("\tA* = ALPHABET, A | B*B = BOB, BB, BsometextB | *SE = dAtABaSE, SE");
		System.out.println();
		System.out.println("Retrieves all students whose name is \"Ivan\":");
		System.out.println("\tquery firstName = \"Ivan\"");
		System.out.println();
		System.out.println("Retrieves all students whose jmbag is greater than \"0000000030\" lexicographically:");
		System.out.println("\tquery jmbag > \"0000000030\"");
		System.out.println();
		System.out.println("Retrieves all students whose first name is \"Ivan\" and last name ends with 'ć':");
		System.out.println("\tquery firstName = \"Ivan\" AND lastName LIKE \"*ć\"");
		System.out.println();
	}
	
	/**
	 * Loads a student database and returns it.
	 *
	 * @param databaseRows list of {@code String} database rows
	 * @return
	 */
	private static StudentDatabase loadDatabase(List<String> databaseRows) {
		try {
			return new StudentDatabase(databaseRows);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates a list of {@code String} objects that are rows
	 * extracted from a database text file.
	 *
	 * @param filepath path to the database text file
	 * @return list of database rows represented as {@code Strings}
	 * @throws IOException if a problem with the given file occurs
	 */
	private static List<String> getDatabaseRows(String filepath) throws IOException {
		return Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
	}
}
