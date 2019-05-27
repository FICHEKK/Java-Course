package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Object that executes the previously parsed script
 * document.
 *
 * @author Filip Nemec
 */
public class SmartScriptEngine {
	
	/** The root of the hierarchy. */
	private DocumentNode documentNode;
	
	/** The request context. */
	private RequestContext requestContext;
	
	/** The multi-stack helper data structure. */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * Constructs a new engine.
	 *
	 * @param documentNode the root of the script
	 * @param requestContext the request context
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	/**
	 * Executes the script.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
	
	/**
	 * A visitor that executes the parsed script.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
				
			} catch(IOException e) {
				System.err.println("Error writing the text node.");
				
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String var 	 = node.getVariable().asText();
			String start = node.getStartExpression().asText();
			String step  = node.getStepExpression().asText();
			String end 	 = node.getEndExpression().asText();
			
			if(step == null) {
				step = "1";
			}
			
			multistack.push(var, new ValueWrapper(start));
			
			while(multistack.peek(var).numCompare(end) <= 0) {
				iterateNodeChildren(node);
				multistack.peek(var).add(step);
			}
			
			multistack.pop(var);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Element[] elements = node.getElements();
			
			for(Element element : elements) {
				if(element instanceof ElementConstantInteger ||
				   element instanceof ElementConstantDouble ||
				   element instanceof ElementString) {
					multistack.push("temp", new ValueWrapper(element.asText()));
					
				} else if(element instanceof ElementVariable) {
					ValueWrapper varValue = multistack.peek(element.asText());
					multistack.push("temp", new ValueWrapper(varValue.getValue()));
					
				} else if(element instanceof ElementOperator) {
					ValueWrapper operand2 = multistack.pop("temp");
					ValueWrapper operand1 = multistack.pop("temp");
					
					String operator = element.asText();
					
					if(operator.equals("+")) {
						operand1.add(operand2.getValue());
						
					} else if(operator.equals("-")) {
						operand1.subtract(operand2.getValue());
						
					} else if(operator.equals("*")) {
						operand1.multiply(operand2.getValue());
						
					} else if(operator.equals("/")) {
						operand1.divide(operand2.getValue());
						
					} else {
						throw new UnsupportedOperationException("Unsupported operator '" + operator + "'.");
						
					}
					
					multistack.push("temp", operand1);
					
				} else if(element instanceof ElementFunction) {
					String function = element.asText();
					
					if(function.equals("sin")) {
						String x = multistack.pop("temp").getValue().toString();
						Double r = Math.sin(Math.toRadians(Double.parseDouble(x)));
						multistack.push("temp", new ValueWrapper(r));
						
					} else if(function.equals("decfmt")) {
						String pattern = multistack.pop("temp").getValue().toString();
						Double number = Double.parseDouble(multistack.pop("temp").getValue().toString());
						DecimalFormat formatter = new DecimalFormat(pattern);
						String result = formatter.format(number);
						multistack.push("temp", new ValueWrapper(result));
						
					} else if(function.equals("dup")) {
						ValueWrapper wrapper = multistack.pop("temp");
						multistack.push("temp", new ValueWrapper(wrapper.getValue()));
						multistack.push("temp", new ValueWrapper(wrapper.getValue()));
						
					} else if(function.equals("swap")) {
						ValueWrapper a = multistack.pop("temp");
						ValueWrapper b = multistack.pop("temp");
						multistack.push("temp", a);
						multistack.push("temp", b);
						
					} else if(function.equals("setMimeType")) {
						String mimeType = (String) multistack.pop("temp").getValue();
						requestContext.setMimeType(mimeType);
						
					} else if(function.equals("paramGet")) {
						String defaultValue = multistack.pop("temp").getValue().toString();
						String name 		= multistack.pop("temp").getValue().toString();
						String value = requestContext.getParameter(name);
						multistack.push("temp", new ValueWrapper(value == null ? defaultValue : value));
						
					} else if(function.equals("pparamGet")) {
						String defaultValue = multistack.pop("temp").getValue().toString();
						String name 		= multistack.pop("temp").getValue().toString();
						String value = requestContext.getPersistentParameter(name);
						multistack.push("temp", new ValueWrapper(value == null ? defaultValue : value));
						
					} else if(function.equals("pparamSet")) {
						String name  = multistack.pop("temp").getValue().toString();
						String value = multistack.pop("temp").getValue().toString();
						requestContext.setPersistentParameter(name, value);
						
					} else if(function.equals("pparamDel")) {
						String name = multistack.pop("temp").getValue().toString();
						requestContext.removePersistentParameter(name);
						
					} else if(function.equals("tparamGet")) {
						String defaultValue = multistack.pop("temp").getValue().toString();
						String name 		= multistack.pop("temp").getValue().toString();
						String value = requestContext.getTemporaryParameter(name);
						multistack.push("temp", new ValueWrapper(value == null ? defaultValue : value));
						
					} else if(function.equals("tparamSet")) {
						String name  = multistack.pop("temp").getValue().toString();
						String value = multistack.pop("temp").getValue().toString();
						requestContext.setTemporaryParameter(name, value);
						
					} else if(function.equals("tparamDel")) {
						String name = multistack.pop("temp").getValue().toString();
						requestContext.removeTemporaryParameter(name);
						
					}
				}
			}
			
			if(!multistack.isEmpty("temp")) {
				var values = new LinkedList<ValueWrapper>();
				
				while(!multistack.isEmpty("temp")) {
					values.addFirst(multistack.pop("temp"));
				}
				
				values.forEach(v -> {
					try {
						requestContext.write(v.getValue().toString());
					} catch (IOException e) {
					}
				});
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			iterateNodeChildren(node);
		}
		
		/**
		 * Iterates the given {@code Node} children and calls
		 * the {@code this} visitor's {@code accept} method on each.
		 *
		 * @param node the node that holds the children
		 */
		private void iterateNodeChildren(Node node) {
			int childCount = node.numberOfChildren();
			for(int i = 0; i < childCount; i++) {
				node.getChild(i).accept(this);
			}
		}
	};
}
