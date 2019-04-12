package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SmartScriptParserTest {

	@Test
	void invalidTagNameTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ + $}"));
	}
	
	@Test
	void unsupportedTagNameTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ DOESNT_EXIST $}"));
	}
	
	@Test
	void echoTagNotClosedTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ = bla"));
	}
	
	@Test
	void unsupportedOperatorTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ = % $}"));
	}
	
	@Test
	void invalidForLoopArgumentsTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ FOR 3 1 10 2 $}{$ END $}"));
	}
	
	@Test
	void forLoopTooManyArgumentsTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ FOR i 1 10 2 5 $}"));
	}
	
	@Test
	void forLoopNotClosedTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ FOR i 1 10 2 $}"));
	}
	
	@Test
	void tooManyEndTagsTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ END $}"));
	}
	
	@Test
	void invalidEndTagTest() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser("{$ FOR i 1 10 2 $}{$ END 42 $}"));
	}
	
	@Test
	void nullArgumentInConstructorTest() {
		assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
	}
}
