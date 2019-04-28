package searching.slagalica;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

/**
 * Models a simple number puzzle and offers strategies
 * for solving it.
 *
 * @author Filip Nemec
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
								  Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>,
								  Predicate<KonfiguracijaSlagalice> {
	
	/** The solution to this puzzle. */
	private static final int[] solution = {1, 2, 3, 4, 5, 6, 7, 8, 0};
	
	/** The initial state (configuration). */
	private KonfiguracijaSlagalice s0;
	
	/**
	 * Constructs a new puzzle with the initial state (configuration).
	 *
	 * @param s0 the initial state (configuration)
	 */
	public Slagalica(KonfiguracijaSlagalice s0) {
		this.s0 = s0;
	}
	
	//----------------------------------------------------------------------------
	//								STRATEGIES
	//----------------------------------------------------------------------------
	
	@Override
	public KonfiguracijaSlagalice get() {
		return s0;
	}
	
	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {
		int space = t.indexOfSpace();
		var possibleMovesIndexes = new LinkedList<Integer>();
		
		if(!(space % 3 == 0)) possibleMovesIndexes.add(space - 1);
		if(!(space % 3 == 2)) possibleMovesIndexes.add(space + 1);
		if(space >= 3) 		  possibleMovesIndexes.add(space - 3);
		if(space <= 5) 		  possibleMovesIndexes.add(space + 3);
		
		var transitions = new LinkedList<Transition<KonfiguracijaSlagalice>>();
		
		possibleMovesIndexes.forEach(moveIndex -> {
			var si = new KonfiguracijaSlagalice(swapSlots(t.getPolje(), space, moveIndex));
			transitions.add(new Transition<>(si, 1));
		});
		
		return transitions;
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
	
	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		return Arrays.compare(t.getPolje(), solution) == 0;
	}
}
