package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * A node representing a piece of textual data.
 */
public class TextNode extends Node {
	/** text data stored in this node */
	private String text;
	
	/**
	 * Constructs a new text node with specified text.
	 * 
	 * @param text text that this node should store
	 * @throws NullPointerException if the given text is {@code null}
	 */
	public TextNode(String text) {
		Objects.requireNonNull(text);
		this.text = text;
	}
	
	/**
	 * Returns the text data that this node stores.
	 * 
	 * @return text data of this node
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		char[] chars = text.toCharArray();
		
		for(char c : chars) {
			if(c == '{' || c == '\\') {
				sb.append('\\');
			}
			sb.append(c);
		}
		
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TextNode))
			return false;
		TextNode other = (TextNode) obj;
		
		String thisText = getText().replaceAll(" ", "");
		String otherText = other.getText().replaceAll(" ", "");
		
		return thisText.equals(otherText);
	}
}
