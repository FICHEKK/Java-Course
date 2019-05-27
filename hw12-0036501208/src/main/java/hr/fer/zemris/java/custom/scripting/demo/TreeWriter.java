package hr.fer.zemris.java.custom.scripting.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * A simple program demonstrating the usage of the {@code Visitor}
 * design pattern. This {@code Visitor} will visit each {@code Node}
 * in the parsed document tree hierarchy and reconstruct the original
 * document text.
 * 
 * @author Filip Nemec
 */
public class TreeWriter {
	
	/**
	 * Program starts from here. It expects a single argument: the
	 * path of the file.
	 *
	 * @param args the path of the file to be parsed
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Expected a single argument: file path. Closing...");
			return;
		}
		
		String docBody = loader(args[0]);
		
		if(docBody == null) {
			System.err.println("Error during the loading of a file. Closing...");
			return;
		}
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}
	
	/**
	 * An {@code INodeVisitor} that reconstructs the original
	 * document text from the tree hierarchy.
	 *
	 * @author Filip Nemec
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			var sb = new StringBuilder();
			
			char[] chars = node.getText().toCharArray();
			
			for(char c : chars) {
				if(c == '{' || c == '\\') {
					sb.append('\\');
				}
				sb.append(c);
			}
			
			System.out.print(sb.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			var sb = new StringBuilder();
			sb.append("{$ FOR ");
			
			sb.append(node.getVariable() + " ");
			sb.append(node.getStartExpression() + " ");
			sb.append(node.getEndExpression() + " ");
			
			Element stepExpression = node.getStepExpression();
			if(stepExpression != null) {
				sb.append(stepExpression + " ");
			}
			
			sb.append("$}");
			System.out.print(sb.toString());
			
			int childCount = node.numberOfChildren();
			for(int i = 0; i < childCount; i++) {
				node.getChild(i).accept(this);
			}
			
			System.out.print("{$ END $}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			var sb = new StringBuilder();
			sb.append("{$ = ");
			
			Element[] elements = node.getElements();
			for(Element e : elements) {
				sb.append(e + " ");
			}
			
			sb.append("$}");
			
			System.out.print(sb.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int childCount = node.numberOfChildren();
			for(int i = 0; i < childCount; i++) {
				node.getChild(i).accept(this);
			}
		}
	}
	
	/**
	 * Returns the text stored in the given file as a single
	 * {@code String}.
	 * 
	 * @param filename file path
	 * @return {@code String} of the file contents, or {@code null}
	 * 		   if exception occurred
	 */
	private static String loader(String filePath) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
			byte[] buffer = new byte[1024];
			while (true) {
				int r = is.read(buffer);
				if (r < 1) break;
				bos.write(buffer, 0, r);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
			
		} catch (IOException ex) {
			return null;
			
		}
	}
}
