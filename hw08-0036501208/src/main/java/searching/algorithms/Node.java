package searching.algorithms;

/**
 * Models a single node for the state-space search
 * algorithm. It encapsulates a current state, current
 * cost and a parent node.
 *
 * @author Filip Nemec
 * @param <S>
 */
public class Node<S> {
	
	/** The parent node used for backtracking. */
	private Node<S> parent;
	
	/** The current state that this node stores. */
	private S state;
	
	/** The current cost. */
	private double cost;
	
	/**
	 * Constructs a new node.
	 *
	 * @param parent the parent node
	 * @param state the state
	 * @param cost the cost
	 */
	public Node(Node<S> parent, S state, double cost) {
		this.parent = parent;
		this.state = state;
		this.cost = cost;
	}
	
	/**
	 * Returns the parent node.
	 *
	 * @return the parent node
	 */
	public Node<S> getParent() {
		return parent;
	}
	
	/**
	 * Returns the current state.
	 *
	 * @return the current state
	 */
	public S getState() {
		return state;
	}
	
	/**
	 * Returns the current cost.
	 *
	 * @return the current cost
	 */
	public double getCost() {
		return cost;
	}
}
