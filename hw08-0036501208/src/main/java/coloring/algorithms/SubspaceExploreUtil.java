package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class that offers some basic space
 * exploring algorithms.
 *
 * @author Filip Nemec
 */
public class SubspaceExploreUtil {
	
	/**
	 * Performs a breadth-first search.
	 *
	 * @param s0 the supplier of initial state element
	 * @param process performs a defined operation on each applicable element
	 * @param succ successor function which is being performed on each processed element
	 * @param acceptable checks whether the element should be processed or just skipped
	 */
	public static <S> void bfs (Supplier<S> s0,
								Consumer<S> process,
								Function<S,List<S>> succ,
								Predicate<S> acceptable) {
		LinkedList<S> checkList = new LinkedList<S>();
		checkList.add(s0.get());
		
		while(!checkList.isEmpty()) {
			S si = checkList.removeFirst();
			
			if(!acceptable.test(si)) continue;
			
			process.accept(si);
			checkList.addAll(succ.apply(si));
		}
	}
	
	/**
	 * Performs a depth-first search.
	 *
	 * @param s0 the supplier of initial state element
	 * @param process performs a defined operation on each applicable element
	 * @param succ successor function which is being performed on each processed element
	 * @param acceptable checks whether the element should be processed or just skipped
	 */
	public static <S> void dfs(Supplier<S> s0,
			 				   Consumer<S> process,
			 				   Function<S,List<S>> succ,
			 				   Predicate<S> acceptable) {
		LinkedList<S> checkList = new LinkedList<S>();
		checkList.add(s0.get());
		
		while(!checkList.isEmpty()) {
			S si = checkList.removeFirst();
			
			if(!acceptable.test(si)) continue;
			
			process.accept(si);
			checkList.addAll(0, succ.apply(si));
		}
	}
	
	/**
	 * Performs a breadth-first search, while also keeping
	 * track of the visited states. This approach greatly
	 * lowers the step count.
	 *
	 * @param s0 the supplier of initial state element
	 * @param process performs a defined operation on each applicable element
	 * @param succ successor function which is being performed on each processed element
	 * @param acceptable checks whether the element should be processed or just skipped
	 */
	public static <S> void bfsv(Supplier<S> s0,
							 	Consumer<S> process,
							 	Function<S,List<S>> succ,
							 	Predicate<S> acceptable) {
		LinkedList<S> checkList = new LinkedList<S>();
		checkList.add(s0.get());
		
		Set<S> visited = new HashSet<S>();
		visited.add(s0.get());
		
		while(!checkList.isEmpty()) {
			S si = checkList.removeFirst();
			
			if(!acceptable.test(si)) continue;
			
			process.accept(si);
			List<S> children = succ.apply(si);
			
			children.removeIf(child -> visited.contains(child));
			
			checkList.addAll(children);
			visited.addAll(children);
		}
	}

}
