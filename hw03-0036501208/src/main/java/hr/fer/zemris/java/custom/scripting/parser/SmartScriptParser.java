package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Parses the given script text and creates a Document Tree for
 * said script text. There are four types of nodes that can be
 * found in the tree: Document, Text, Echo and ForLoop.
 * Document node is the root of the document. Text node is a simple
 * node containing text as a {@code String}. Echo node saves an array
 * of elements that can be dynamically displayed. Finally, ForLoop node
 * represents a for-loop construct.
 */
public class SmartScriptParser {
	
	/** The root node of the hierarchy. */
	private DocumentNode documentNode = new DocumentNode();
	
	/** Stack used as a helper for constructing a hierarchy. */
	private ObjectStack stack = new ObjectStack();
	
	/** Reference to the lexer that is a source of tokens for this parser. */
	private ScriptLexer lexer;
	
	/** After parsing finishes, this is the number of elements that should remain. */
	private final int AFTER_PARSE_EXPECTED_STACK_SIZE = 1;
	
	/** Flag that says if this parser is currently processing text. */
	//private boolean processingText = true;
	
	/**
	 * Constructs a new parser that will process the given text.
	 * 
	 * @param text text to be processed
	 * @throws NullPointerException if given {@code text} argument is {@code null}
	 */
	public SmartScriptParser(String text) {
		Objects.requireNonNull(text);
		lexer = new ScriptLexer(text);
		processDocument();
	}
	
	/**
	 * Processes the document by generating and processing tokens until
	 * there are none left. Token that signals that we reached the end
	 * is of type EOF.
	 */
	private void processDocument() {
		stack.push(documentNode);
		while(processNextScriptToken());
		verifyThatAllTagsAreClosed();
	}

	/**
	 * Processes the given script token and returns {@code true} if
	 * the given token was not of type {@code EOF}.
	 * 
	 * @param token token to be processed
	 * @return {@code true} if the given token was not of type {@code EOF}
	 */
	private boolean processNextScriptToken() {
		ScriptToken token = getNextToken();
		ScriptTokenType type = token.getType();
		
		if(type == ScriptTokenType.EOF) return false;
		
		if(type == ScriptTokenType.TEXT) {
			addToHierarchy(new TextNode(token.getValue().toString()), false);
		} else if(type == ScriptTokenType.TAG_OPEN) {
			lexer.setState(ScriptLexerState.PROCESSING_TAG);
			processTag();
		} else if(type == ScriptTokenType.TAG_CLOSE) {
			lexer.setState(ScriptLexerState.PROCESSING_TEXT);
		}
		
		return true;
	}
	
	/**
	 * Gets the next token from lexer and returns it.
	 * 
	 * @return next token that will be generated by the lexer
	 */
	private ScriptToken getNextToken() {
		ScriptToken token = lexer.nextToken();
		//System.out.println(token);
		return token;
	}
	
	/**
	 * Processes the next tag in sequence.
	 */
	private void processTag() {
		ScriptToken token = getNextToken();
		
		if(token.getType() != ScriptTokenType.IDENTIFIER) {
			throw new SmartScriptParserException("After opening tag should come a valid tag name.");
		}
		
		String tagName = token.getValue().toString();
		
		if(tagName.equals("=")) {
			processEchoTag();
		} else if(tagName.equalsIgnoreCase("FOR")) {
			processForLoopTag();
		} else if(tagName.equalsIgnoreCase("END")) {
			processEndTag();
		} else {
			throw new SmartScriptParserException("'" + tagName + "' is an unsupported tag name.");
		}
		
		// Once we are done processing the tag, lexer is now in a "processing text" state.
		lexer.setState(ScriptLexerState.PROCESSING_TEXT);
	}

	/**
	 * Processes the echo tag.
	 */
	private void processEchoTag() {
		var temporary = new LinkedListIndexedCollection();
		
		while(true) {
			ScriptToken token = getNextToken();
			ScriptTokenType type = token.getType();
			
			if(type == ScriptTokenType.EOF) {
				throw new SmartScriptParserException("Echo tag was not closed!");
			} else if(type == ScriptTokenType.TAG_CLOSE) {
				break;
			} else if(type == ScriptTokenType.IDENTIFIER) {
				temporary.add(new ElementVariable(token.getValue().toString()));
			} else if(type == ScriptTokenType.STRING) {
				temporary.add(new ElementString(token.getValue().toString()));
			} else if(type == ScriptTokenType.FUNCTION) {
				temporary.add(new ElementFunction(token.getValue().toString()));
			} else if(type == ScriptTokenType.OPERATOR) {
				String operator = token.getValue().toString();
				if(SmartScriptParserUtil.isOperatorValid(operator)) {
					temporary.add(new ElementOperator(operator));
				} else {
					throw new SmartScriptParserException("'" + operator + "' is an invalid operator.");
				}
			} else if(type == ScriptTokenType.INTEGER) {
				temporary.add(new ElementConstantInteger((Integer)token.getValue()));
			} else if(type == ScriptTokenType.DOUBLE) {
				temporary.add(new ElementConstantDouble((Double)token.getValue()));
			} else {
				throw new SmartScriptParserException("'" + type + "' is an invalid token type");
			}
		}
		
		Object[] objects = temporary.toArray();
		Element[] elements = new Element[objects.length];
		
		for(int i = 0; i < objects.length; i++) {
			elements[i] = (Element)objects[i];
		}
		
		addToHierarchy(new EchoNode(elements), false);
	}
	
	/**
	 * Processes the for-loop tag.
	 * 
	 * @throws SmartScriptParserException if the given for-loop construct
	 * 		   was invalid
	 */
	private void processForLoopTag() {
		ElementVariable variable = null;
		Element startExpression = null;
		Element endExpression = null;
		Element stepExpression = null;
		ScriptToken token = null;
		
		// 1st token
		token = getNextToken();
		if(token.getType() == ScriptTokenType.IDENTIFIER) {
			variable = new ElementVariable(token.getValue().toString());
		} else {
			String message = "Expected token of type IDENTIFICATOR. Was: " + lexer.getToken();
			throw new SmartScriptParserException(message);
		}
		
		// 2nd token
		startExpression = getElementFromToken(getNextToken());
		
		// 3rd token
		endExpression = getElementFromToken(getNextToken());
		
		// 4th token
		token = getNextToken();
		if(token.getType() != ScriptTokenType.TAG_CLOSE) {
			stepExpression = getElementFromToken(token);
			
			// Next token must be the closing tag, otherwise there are
			// more than 4 elements in this FOR tag, which is forbidden.
			if(getNextToken().getType() != ScriptTokenType.TAG_CLOSE) {
				String message = "Too many arguments: Closing tag token must come after 4 for-loop elements. Was: " + lexer.getToken();
				throw new SmartScriptParserException(message);
			}
		}
		
		addToHierarchy(new ForLoopNode(variable, startExpression, endExpression, stepExpression), true);
	}
	
	/**
	 * Constructs the correct {@code Element} from the given token.
	 * 
	 * @param token token that is the provider
	 * @return the correct {@code Element} object
	 */
	private Element getElementFromToken(ScriptToken token) {
		ScriptTokenType type = token.getType();
		String value = token.getValue().toString();
		
		if(type == ScriptTokenType.IDENTIFIER) {
			return new ElementVariable(value); 
		} else if(type == ScriptTokenType.INTEGER) {
			return new ElementConstantInteger(Integer.valueOf(value));
		} else if(type == ScriptTokenType.STRING) {
			return new ElementString(value);
		} else {
			String message = "Expected token of type IDENTIFICATOR/INTEGER/STRING. Was: " + lexer.getToken();
			throw new SmartScriptParserException(message);
		}
	}

	/**
	 * Processes the end tag.
	 * 
	 * @throws SmartScriptParserException if this END tag empties the stack
	 * 		   or if this END tag construct is invalid
	 */
	private void processEndTag() {
		if(getNextToken().getType() != ScriptTokenType.TAG_CLOSE) {
			String message = "Closing tag token must come after 'END' token. Token was '" + lexer.getToken() + "'.";
			throw new SmartScriptParserException(message);
		}
		
		stack.pop();
		if(stack.isEmpty()) {
			throw new SmartScriptParserException("There are more 'END' tags than opening tags.");
		}
	}
	

	/**
	 * Adds the given node to the tree hierarchy for this parser.
	 * 
	 * @param node node to be added
	 * @param isClosing if this node should be closed. If given {@code true},
	 * 		  this node will also get pushed to the stack.
	 */
	private void addToHierarchy(Node node, boolean isClosing) {
		Node parent = (Node)stack.peek();
		parent.addChildNode(node);
		
		if(isClosing) {
			stack.push(node);
		}
	}
	
	
	/**
	 * Verifies that all opened tags were actually closed at some point.
	 * 
	 * @throws SmartScriptParserException if there are some unclosed tags left
	 */
	private void verifyThatAllTagsAreClosed() {
		int unclosedTags = stack.size() - AFTER_PARSE_EXPECTED_STACK_SIZE;
		
		if(unclosedTags != 0) {
			throw new SmartScriptParserException("There are " + unclosedTags + " unclosed tags left.");
		}
	}
	
	/**
	 * Helper method that prints the node hierarchy tree in the console,
	 * starting from the given node{@code root}
	 * 
	 * @param root root of the tree
	 * @param recursionLevel always start at 0
	 */
	private static void printParsedTreeRecrusion(Node root, int recursionLevel, int order) {
		for(int i = 0; i < recursionLevel; i++) {
			System.out.print("\t");
		}
		
		System.out.println("[" + order + "] " + root.getClass().getSimpleName());
		
		int childCount = root.numberOfChildren();
		for(int i = 0; i < childCount; i++) {
			Node child = root.getChild(i);
			printParsedTreeRecrusion(child, recursionLevel + 1, i);
		}
	}
	
	/**
	 * Prints the parsed tree to the console.
	 * 
	 * @param documentName document name that will be shown in the console
	 * @param root root of the document
	 */
	public static void printParsedTree(String documentName, Node root) {
		System.out.println(documentName);
		printParsedTreeRecrusion(root, 0, 0);
	}
	
	/**
	 * Returns the root node of the whole document that was parsed.
	 * 
	 * @return the root node
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}
}
