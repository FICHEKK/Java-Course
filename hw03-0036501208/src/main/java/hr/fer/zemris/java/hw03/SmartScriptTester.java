package hr.fer.zemris.java.hw03;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Simple testing class that shows the functionality
 * of {@code SmartScriptParser} and {@code ScriptLexer}
 * objects.
 * 
 * It expects exactly 1 argument: name of the file to be
 * parsed.
 */
public class SmartScriptTester {
	/**
	 * Parses the given file and recreates it later.
	 * 
	 * @param args exactly 1 {@code String} argument expected: name of the file
	 * 		  (for example: textfile.txt)
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("There should be only 1 argument, name of the file that will be parsed.");
			return;
		}
		
		String docBody = loader(args[0]);
		
		if(docBody == null) {
			System.err.println("Provided file was not found, or is invalid.");
			return;
		}
		
		SmartScriptParser parser = null;
		
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			e.printStackTrace();
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		
		DocumentNode original = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(original);
		
		var parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode recreated = parser2.getDocumentNode();
		
		System.out.println("Those documents are structurally the same: " + areDocStructuresEqual(original, recreated));
		
		//------------------------------------------------------------------------------
		// Code below prints tree structures and original & recreated document text.
		//------------------------------------------------------------------------------
		System.out.printf("Original document text:%n%n%s%n%n%n", docBody);
		System.out.printf("Recreated document text:%n%n%s%n%n%n", originalDocumentBody);
		SmartScriptParser.printParsedTree("ORIGINAL:", original);
		SmartScriptParser.printParsedTree("RECREATED:", recreated);
	}
	
	/**
	 * Recreates the original document text from the parsed tree hierarchy.
	 * 
	 * @param node root of the hierarchy
	 * @return original text before parsing
	 */
	public static String createOriginalDocumentBody(DocumentNode node) {
		return node.toString();
	}
	
	/**
	 * Checks whether two documents have the same structure.
	 * 
	 * @param first the first document
	 * @param second the second document
	 * @return {@code true} if those two documents have the same structure
	 * 		   {@code false} otherwise
	 */
	public static boolean areDocStructuresEqual(DocumentNode first, DocumentNode second) {
		return first.equals(second);
	}
	
	/**
	 * Returns the string that was stored inside the given file.
	 * 
	 * @param filename file path
	 * @return {@code String} of the file contents, or {@code null}
	 * 		   if exception occured
	 */
	private static String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = Files.newInputStream(Paths.get("src/main/resources/" + filename), StandardOpenOption.READ)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}
}
