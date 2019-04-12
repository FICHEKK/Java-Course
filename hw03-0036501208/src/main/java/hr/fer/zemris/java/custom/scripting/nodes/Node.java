package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all graph nodes.
 */
public class Node {
	/** collection of children for this node */
	private ArrayIndexedCollection children;
	
	/**
	 * Adds a new child to the children collection
	 * for this node.
	 * 
	 * @param child node to be added as a child to this node
	 */
	public void addChildNode(Node child) {
		if(children == null) {
			children = new ArrayIndexedCollection();
		}
		
		children.add(child);
	}
	
	/**
	 * Returns the number of children this node has.
	 * 
	 * @return number of children for this node
	 */
	public int numberOfChildren() {
		return children == null ? 0 : children.size();
	}
	
	/**
	 * Returns this node's child at index {@code index}, if
	 * the given index is valid.
	 * 
	 * @param index index of the child
	 * @return the node child at index {@code index} if the
	 * 		   {@code index} is in valid range
	 * @throws IllegalStateException if there were not any children added
	 * @throws IndexOutOfBoundsException if index is invalid
	 */
	public Node getChild(int index) {
		if(children == null) {
			throw new IndexOutOfBoundsException("Was '" + index + "'.");
		}
		return (Node)children.get(index);
	}
	
	/**
	 * Nodes will be equal only if all of their children are equal
	 * and the same in quantity and order.
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Node)) 
			return false;
		Node other = (Node) obj;
		
		int childCount = numberOfChildren();
		int childCountOther = other.numberOfChildren();
		if(childCount != childCountOther)
			return false;
		
		for(int i = 0; i < childCount; i++) {
			if(!Objects.equals(getChild(i), other.getChild(i)))
				return false;
		}
		
		return true;
	}
}
