package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * A node representing a command which generates
 * some textual output dynamically.
 */
public class EchoNode extends Node {
	
	/** Array of elements stored inside this echo node. */
	private Element[] elements;
	
	/**
	 * Constructs a new echo node with the given elements.
	 * @param elements elements to be stored in this echo node
	 * @throws NullPointerException if the given {@code elements} parameter is {@code null}
	 */
	public EchoNode(Element[] elements) {
		Objects.requireNonNull(elements);
		this.elements = elements;
	}
	
	/**
	 * Returns array of all the elements this echo node stores.
	 * 
	 * @return array of this node's elements
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append("{$ = ");
		
		for(Element e : elements) {
			sb.append(e + " ");
		}
		
		sb.append("$}");
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof EchoNode))
			return false;
		EchoNode other = (EchoNode) obj;
		
		Element[] otherElements = other.getElements();
		if(elements.length != otherElements.length)
			return false;
		
		int elementCount = elements.length;
		for(int i = 0; i < elementCount; i++) {
			if(!elements[i].equals(otherElements[i]))
				return false;
		}
		
		return true;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
