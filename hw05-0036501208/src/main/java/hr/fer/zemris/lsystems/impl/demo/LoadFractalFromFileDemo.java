package hr.fer.zemris.lsystems.impl.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * A simple demo program that allows the user to display
 * L-system fractals defined by the set of rules that are
 * loaded from the file.
 * <p>
 * User's only job is to provide a single argument through the
 * console: the file name that contains the L-system rules and
 * configuration.
 * <p>
 * Also, it should be noted that files are ought be saved to
 * "src/test/resources" and should have an extension of ".txt".
 * 
 * @author Filip Nemec
 */
public class LoadFractalFromFileDemo {
	
	/** The number of arguments that are expected. */
	private static final int EXPECTED_ARGUMENT_COUNT = 1;
	
	/*
	 * Note: this demo program was made purely for the
	 * fun of it. You can freely ignore this demo.
	 */
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		if(args.length != EXPECTED_ARGUMENT_COUNT) {
			System.err.println(EXPECTED_ARGUMENT_COUNT + "arguments expected. Was " + args.length + ".");
			return;
		}
		
		String filename = args[0];
		Path filepath = Paths.get("src/test/resources/" + filename + ".txt");
		
		List<String> input = new LinkedList<>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(filepath.toFile()))) {
			String line;
			System.out.println(filename + "\n");
			while((line = reader.readLine()) != null) {
				System.out.println(line);
				input.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String[] textInput = input.toArray(new String[0]);
		
		LSystemViewer.showLSystem(createLSystem(LSystemBuilderImpl::new, textInput));
	}
	
	private static LSystem createLSystem(LSystemBuilderProvider provider, String[] data) {
		return provider.createLSystemBuilder().configureFromText(data).build();
	}
}
