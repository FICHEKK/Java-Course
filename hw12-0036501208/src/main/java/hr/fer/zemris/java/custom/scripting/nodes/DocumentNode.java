package hr.fer.zemris.java.custom.scripting.nodes;

/**
 *  A node representing an entire document.
 */
public class DocumentNode extends Node {
	
	/**
	 * Iterates through every child node and prints it.
	 */
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		int childCount = numberOfChildren();
		for(int i = 0; i < childCount; i++) {
			sb.append(getChild(i));
		}
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DocumentNode) && super.equals(obj);
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
