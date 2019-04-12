package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct
 */
public class ForLoopNode extends Node {
	/** Variable used as an iterator for this for-loop. */
	private ElementVariable variable;
	
	/** Start expression of this for-loop. */
	private Element startExpression;
	
	/** End expression of this for-loop. */
	private Element endExpression;
	
	/** Step expression of this for-loop. */
	private Element stepExpression;
	
	/**
	 * Constructs a new node that represents a for-loop construct.
	 * 
	 * @param var starting variable.
	 * @param start starting expression
	 * @param end ending expression
	 * @param step step expression
	 * @return NullPointerException if {@code var, start or end} are {@code null}
	 */
	public ForLoopNode(ElementVariable var, Element start, Element end, Element step) {
		Objects.requireNonNull(var);
		Objects.requireNonNull(start);
		Objects.requireNonNull(end);
		
		this.variable = var;
		this.startExpression = start;
		this.endExpression = end;
		this.stepExpression = step;
	}
	
	/**
	 * Returns the variable of this for-loop.
	 * 
	 * @return for-loop variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Returns the starting expression of this for-loop.
	 * 
	 * @return for-loop starting expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Returns the ending expression of this for-loop.
	 * 
	 * @return for-loop ending expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Returns the step expression of this for-loop.
	 * 
	 * @return for-loop step expression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	/**
	 * Print this for-loop state and also all of
	 * its children.
	 */
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		sb.append("{$ FOR ");
		
		sb.append(variable + " ");
		sb.append(startExpression + " ");
		sb.append(endExpression + " ");
		
		if(stepExpression != null) {
			sb.append(stepExpression + " ");
		}
		
		sb.append("$}");
		
		int childCount = numberOfChildren();
		for(int i = 0; i < childCount; i++) {
			sb.append(getChild(i));
		}
		
		sb.append("{$ END $}");
		
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ForLoopNode))
			return false;
		ForLoopNode other = (ForLoopNode) obj;
		
		boolean forLoopIdentical = Objects.equals(variable, other.getVariable()) &&
								   Objects.equals(startExpression, other.getStartExpression()) &&
								   Objects.equals(endExpression, other.getEndExpression()) &&
								   Objects.equals(stepExpression, other.getStepExpression());
		boolean allChildrenIdentical = super.equals(obj);

		return forLoopIdentical && allChildrenIdentical;
	}
}
