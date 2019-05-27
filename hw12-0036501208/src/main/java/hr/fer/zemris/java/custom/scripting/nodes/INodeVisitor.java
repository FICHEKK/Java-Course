package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Models the objects that can visit the
 * node hierarchy.
 *
 * @author Filip Nemec
 */
public interface INodeVisitor {
	
	/**
	 * Defines what happens once the visitor visits
	 * the text node.
	 *
	 * @param node the text node
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Defines what happens once the visitor visits
	 * the for loop node.
	 * 
	 * @param node the for loop node
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Defines what happens once the visitor visits
	 * the echo node.
	 * 
	 * @param node the echo node
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Defines what happens once the visitor visits
	 * the document node.
	 * 
	 * @param node the document node
	 */
	public void visitDocumentNode(DocumentNode node);
}
