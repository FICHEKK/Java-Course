package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Saves the turtle state context on the stack and
 * offers methods to place, remove and retrieve those states.
 * 
 * @author Filip Nemec
 */
public class Context {
	
	/** Stack used for storing turtle states. */
	private ObjectStack<TurtleState> turtleStateStack;
	
	/**
	 * Constructs a new context.
	 */
	public Context() {
		turtleStateStack = new ObjectStack<TurtleState>();
	}
	
	/**
	 * Returns the last placed state from the stack, but
	 * does not remove it.
	 * 
	 * @return last placed state on the stack
	 */
	public TurtleState getCurrentState() {
		return turtleStateStack.peek();
	}
	
	/**
	 * Places the given state on top of the stack.
	 * 
	 * @param state state to be pushed on top of the stack
	 */
	public void pushState(TurtleState state) {
		turtleStateStack.push(state);
	}
	
	/**
	 * Removes the state that is currently on the top of the stack.
	 */
	public void popState() {
		turtleStateStack.pop();
	}
}
