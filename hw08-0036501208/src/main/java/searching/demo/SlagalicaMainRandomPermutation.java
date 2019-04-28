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
 * Program does not expect any arguments, as it
 * will generate the random permutation itself,
 * and display the solution.
 *
 * @author Filip Nemec
 */
public class SlagalicaMainRandomPermutation {
	
	/**
	 * The program starts here.
	 *
	 * @param args none expected
	 */
	public static void main(String[] args) {
		Slagalica puzzle = new Slagalica(new KonfiguracijaSlagalice(PermutatorUtil.permute()));
		
		Node<KonfiguracijaSlagalice> solution = SearchUtil.bfsv(puzzle, puzzle, puzzle);
		
		if (solution == null) {
			System.out.println("This puzzle is unsolvable.");
		} else {
			System.out.println("Solution found! Optimal number of steps: " + solution.getCost());
			SlagalicaViewer.display(solution);
		}
	}
}