package hr.fer.zemris.java.hw03.prob1.demo;

import hr.fer.zemris.java.hw03.prob1.Lexer;
import hr.fer.zemris.java.hw03.prob1.TokenType;

@SuppressWarnings("javadoc")
public class LexerDemo {
	public static void main(String[] args) {
		//---------------------------------------------
		// Simple helper testing class, please ignore
		//---------------------------------------------
		Lexer lexer = new Lexer("Ovo je 123ica, ab57.\nKraj");
		
		do {
			System.out.println(lexer.nextToken());
		} while(lexer.getToken().getType() != TokenType.EOF);
	}
}
