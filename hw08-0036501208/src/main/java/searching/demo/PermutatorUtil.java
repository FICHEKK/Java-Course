package searching.demo;

import java.util.LinkedList;
import java.util.Random;

/**
 * A simple class that offers a simple method for
 * permuting the 3x3 grid for the puzzle solver.
 *
 * @author Filip Nemec
 */
public class PermutatorUtil {
	
	/** The amount of random moves that will be performed. */
	private static final int STEP_COUNT = 1000;
	
	/**
	 * Generates a random <b>solvable</b> puzzle 3x3 grid state. It does
	 * so by starting from the solved state, and performs a lot
	 * of valid moves.
	 *
	 * @return the random solvable puzzle initial state
	 */
	public static final int[] permute() {
		int[] state = {1, 2, 3, 4, 5, 6, 7, 8, 0};
		int space = 8;
		
		Random random = new Random();
		
		for(int i = 0; i < STEP_COUNT; i++) {
			var possibleMovesIndexes = new LinkedList<Integer>();
			
			if(!(space % 3 == 0)) possibleMovesIndexes.add(space - 1);
			if(!(space % 3 == 2)) possibleMovesIndexes.add(space + 1);
			if(space >= 3) 		  possibleMovesIndexes.add(space - 3);
			if(space <= 5) 		  possibleMovesIndexes.add(space + 3);
			
			int randomMoveIndex = random.nextInt(possibleMovesIndexes.size());
			int moveIndex = possibleMovesIndexes.get(randomMoveIndex);
			swapSlots(state, space, moveIndex);
			space = moveIndex;
		}

		return state;
	}
	
	/**
	 * Swaps the slots and returns a new state with those swapped slots.
	 * It <b>does modify</b> the given state array.
	 *
	 * @param state the state
	 * @param spaceIndex the empty space index
	 * @param toIndex the index to move empty space to
	 * @return the new state with swapped slots 
	 */
	private static int[] swapSlots(int[] state, int spaceIndex, int toIndex) {
		int temp = state[toIndex];
		state[toIndex] = state[spaceIndex];
		state[spaceIndex] = temp;
		
		return state;
	}
}
