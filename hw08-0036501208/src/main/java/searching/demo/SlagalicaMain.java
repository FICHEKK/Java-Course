package searching.demo;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

/**
 * A simple program that demonstrates the puzzle
 * solver. Puzzle being solver is a classical game
 * of 9 grids where the player needs to put all the
 * tiles in order from 1-8, leaving the last spot 
 * as an empty space.
 * <p>
 * Program expects a single argument: the grid
 * configuration. The program will proceed to solve
 * the problem, unless the given configuration is
 * unsolvable.
 * <p>
 * Argument example: 230146758
 * <br>This argument will prompt the program to
 * solve this configuration:
 * <br>2 3 0
 * <br>1 4 6
 * <br>7 5 8
 * <p>
 * Please note that digit zero acts as an empty
 * space in this program.
 *
 * @author Filip Nemec
 */
public class SlagalicaMain {
	
	/** The expected configuration argument length. */
	private static final int CONFIG_LENGTH = 9;
	
	/**
	 * The program starts here.
	 *
	 * @param args expected only a single argument: puzzle configuration of
	 * 			   format "123456780" or any other permutation of said digits
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("A single argument expected, was: " + args.length);
			return;
		}
		
		if(args[0].length() != CONFIG_LENGTH) {
			String msg = "Expected argument length " + CONFIG_LENGTH + ". Was: " + args[0].length();
			System.err.println(msg);
			return;
		}
		
		int[] configuration = new int[CONFIG_LENGTH];
		
		for(int i = 0; i < CONFIG_LENGTH; i++) {
			configuration[i] = args[0].charAt(i) - '0';
		}
		
		Slagalica puzzle = new Slagalica(new KonfiguracijaSlagalice(configuration));
		
		Node<KonfiguracijaSlagalice> solution = SearchUtil.bfsv(puzzle, puzzle, puzzle);
		
		if (solution == null) {
			System.out.println("This puzzle is unsolvable.");
		} else {
			System.out.println("Solution found! Optimal number of steps: " + solution.getCost());
			SlagalicaViewer.display(solution);
		}
	}
}