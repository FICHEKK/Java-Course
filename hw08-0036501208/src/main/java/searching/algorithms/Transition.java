package searching.algorithms;

/**
 * Encapsulated a single transition. It stores
 * the new state and the cost for getting into
 * that state.
 *
 * @author Filip Nemec
 * @param <S> the type of transition
 */
public class Transition<S> {
	
	/** The state. */
	private S state;
	
	/** The cost for getting into the state. */
	private double cost;
	
	/**
	 * Constructs a new transition.
	 *
	 * @param state the state
	 * @param cost the cost of the state
	 */
	public Transition(S state, double cost) {
		this.state = state;
		this.cost = cost;
	}
	
	/**
	 * Returns the state.
	 *
	 * @return the state
	 */
	public S getState() {
		return state;
	}
	
	/**
	 * Returns the cost of the state.
	 *
	 * @return the cost of the state
	 */
	public double getCost() {
		return cost;
	}

}
