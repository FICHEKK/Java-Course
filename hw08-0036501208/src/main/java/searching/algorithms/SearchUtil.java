package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class that offers some basic search algorithms.
 *
 * @author Filip Nemec
 */
public class SearchUtil {
	
	/**
	 * Performs the breadth-first search. This algorithm
	 * will get stuck in an infinite loop, if the problem
	 * is unsolvable.
	 *
	 * @param s0 {@code Supplier} that returns the initial state
	 * @param succ {@code Function} that generates the next states
	 * @param goal {@code Predicate} that checks if the goal was met
	 * @return the final {@code Node} in search result, or will crash
	 * 		   the process once it runs out of memory since it will
	 * 		   enter the infinite loop for unsolvable problems
	 */
	public static <S> Node<S> bfs(Supplier<S> s0,
			 					  Function<S, List<Transition<S>>> succ,
			 					  Predicate<S> goal) {
		LinkedList<Node<S>> checkList = new LinkedList<>();
		checkList.add(new Node<S>(null, s0.get(), 0));
		
		while(!checkList.isEmpty()) {
			Node<S> ni = checkList.removeFirst();
			
			if(goal.test(ni.getState())) return ni;
			
			List<Transition<S>> transitions = succ.apply(ni.getState());
			transitions.forEach(t -> checkList.add(new Node<S>(ni, t.getState(), ni.getCost() + t.getCost())));
		}
		
		return null;
	}
	
	/**
	 * Performs the breadth-first search, but also keeps
	 * track of the already visited states. This prevents the
	 * algorithm from entering the infinite loop for unsolvable
	 * problems.
	 *
	 * @param s0 {@code Supplier} that returns the initial state
	 * @param succ {@code Function} that generates the next states
	 * @param goal {@code Predicate} that checks if the goal was met
	 * @return the final {@code Node} in search result, or {@code null}
	 * 		   if the goal was not found
	 */
	public static <S> Node<S> bfsv(Supplier<S> s0,
								   Function<S, List<Transition<S>>> succ,
								   Predicate<S> goal) {
		LinkedList<Node<S>> checkList = new LinkedList<>();
		Set<S> visited = new HashSet<S>();
		
		checkList.add(new Node<S>(null, s0.get(), 0));
		
		while(!checkList.isEmpty()) {
			Node<S> ni = checkList.removeFirst();
			
			if(goal.test(ni.getState())) return ni;
			
			List<Transition<S>> transitions = succ.apply(ni.getState());
			
			transitions.removeIf(t -> visited.contains(t.getState()));
			transitions.forEach(t -> checkList.add(new Node<S>(ni, t.getState(), ni.getCost() + t.getCost())));
			transitions.forEach(t -> visited.add(t.getState()));
		}
		
		return null;
	}
}
