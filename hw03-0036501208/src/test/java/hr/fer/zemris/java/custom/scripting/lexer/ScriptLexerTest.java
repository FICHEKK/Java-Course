package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.hw03.prob1.LexerException;

class ScriptLexerTest {
	
	@Test
	void nextTokenTest() {
		ScriptLexer lexer = new ScriptLexer("backslash \\\\ curly \\{" +
											"{$ FOR 1 \"text\" -5.55 * @sin $}" +
											" ending text\n ");
		
		assertEquals(ScriptTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.TAG_OPEN, lexer.nextToken().getType());
		
		lexer.setState(ScriptLexerState.PROCESSING_TAG);
		
		assertEquals(ScriptTokenType.IDENTIFIER, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.INTEGER, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.STRING, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.DOUBLE, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.OPERATOR, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.FUNCTION, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.TAG_CLOSE, lexer.nextToken().getType());
		
		lexer.setState(ScriptLexerState.PROCESSING_TEXT);
		
		assertEquals(ScriptTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(ScriptTokenType.EOF, lexer.nextToken().getType());
		
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void wrongEscapingInTextTest() {
		ScriptLexer lexer = new ScriptLexer("text\\Q");
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void wrongEscapingInTagTest() {
		ScriptLexer lexer = new ScriptLexer("\"  text\\M  \"");
		lexer.setState(ScriptLexerState.PROCESSING_TAG);
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void stringNotClosedTest() {
		ScriptLexer lexer = new ScriptLexer("\"  text  ");
		lexer.setState(ScriptLexerState.PROCESSING_TAG);
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void decimalPointNotFollowedByADigitTest() {
		ScriptLexer lexer = new ScriptLexer("  4.L  ");
		lexer.setState(ScriptLexerState.PROCESSING_TAG);
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void monkeySignNotFollowedByALetterTest() {
		ScriptLexer lexer = new ScriptLexer("  @_function  ");
		lexer.setState(ScriptLexerState.PROCESSING_TAG);
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void getTokenTest() {
		ScriptLexer lexer = new ScriptLexer("text");
		
		assertEquals(ScriptTokenType.TEXT, lexer.nextToken().getType());
		
		// getToken should not change the token
		assertEquals(ScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals(ScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals(ScriptTokenType.TEXT, lexer.getToken().getType());
	}

	@Test
	void constructorNullPassedTest() {
		assertThrows(NullPointerException.class, () -> new ScriptLexer(null));
	}
	
	@Test
	void setStateNullTest() {
		assertThrows(NullPointerException.class, () -> new ScriptLexer("").setState(null));
	}
}
