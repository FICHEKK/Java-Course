package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexNumberTest {
	
	private ComplexNumber cn1 = new ComplexNumber(1, 1);
	private ComplexNumber cn2 = new ComplexNumber(-1, 2); 

	@Test
	void fromRealTest() {
		ComplexNumber fromReal = ComplexNumber.fromReal(5);
		assertEquals(5, fromReal.getReal());
		assertEquals(0, fromReal.getImaginary());
	}
	
	@Test
	void fromImaginaryTest() {
		ComplexNumber fromImag = ComplexNumber.fromImaginary(7);
		assertEquals(0, fromImag.getReal());
		assertEquals(7, fromImag.getImaginary());
	}
	
	@Test
	void fromMagnitudeAndAngleTest() {
		ComplexNumber fromMagAndAng = ComplexNumber.fromMagnitudeAndAngle(1, 0);
		assertEquals(1, fromMagAndAng.getReal());
		assertEquals(0, fromMagAndAng.getImaginary());
	}
	
	@Test
	void getRealTest() {
		assertEquals(1, cn1.getReal());
	}
	
	@Test
	void getImaginaryTest() {
		assertEquals(2, cn2.getImaginary());
	}

	@Test
	void getMagnitudeTest() {
		assertEquals(Math.sqrt(2), cn1.getMagnitude(), 0.00001);
	}

	@Test
	void getAngleTest() {
		assertEquals(Math.PI / 4, cn1.getAngle(), 0.00001);
	}

	@Test
	void addTest() {
		ComplexNumber cn3 = cn1.add(cn2);
		assertEquals(0, cn3.getReal());
		assertEquals(3, cn3.getImaginary());
	}

	@Test
	void subTest() {
		ComplexNumber cn3 = cn1.sub(cn2);
		assertEquals(2, cn3.getReal());
		assertEquals(-1, cn3.getImaginary());
	}

	@Test
	void mulTest() {
		ComplexNumber cn3 = cn1.mul(cn2);
		assertEquals(-3, cn3.getReal());
		assertEquals(1, cn3.getImaginary());
	}

	@Test
	void divTest() {
		ComplexNumber cn3 = cn1.div(cn2);
		assertEquals(0.2, cn3.getReal(), 0.00001);
		assertEquals(-0.6, cn3.getImaginary(), 0.00001);
	}
	
	@Test
	void divZeroTest() {
		ComplexNumber cn3 = cn1.div(cn2);
		assertThrows(ArithmeticException.class, () -> cn3.div(new ComplexNumber(0, 0)));
	}
	
	@Test
	void divNullTest() {
		ComplexNumber cn3 = cn1.div(cn2);
		assertThrows(NullPointerException.class, () -> cn3.div(null));
	}

	@Test
	void powerTest() {
		ComplexNumber cn3 = cn1.power(0);
		assertEquals(1, cn3.getReal());
		assertEquals(0, cn3.getImaginary());
		
		assertThrows(IllegalArgumentException.class, () -> cn1.power(-1));
	}
	
	@Test
	void rootTest() {
		ComplexNumber[] solutions = cn1.root(2);
		ComplexNumber solution1 = solutions[0];
		
		assertEquals(1.09868411, solution1.getReal(), 0.001);
		assertEquals(0.455089861, solution1.getImaginary(), 0.001);
		
		assertThrows(IllegalArgumentException.class, () -> cn1.root(0));
		assertThrows(IllegalArgumentException.class, () -> cn1.root(-1));
	}
	
	@Test
	void parseTest() {
		class ParseTestCase {
			String expression;
			boolean expectedResult;
			
			ParseTestCase(String expression, boolean expectedResult) {
				this.expression = expression;
				this.expectedResult = expectedResult;
			}
		}
		
		ParseTestCase[] tests = {
				new ParseTestCase("-32.+789.2i", true),
				new ParseTestCase("-3a2.+789.2i", false),
				new ParseTestCase("++2i", false),
				new ParseTestCase("-32i", true),
				new ParseTestCase("1.145", true),
				new ParseTestCase("4.44-1i", true),
				new ParseTestCase("-i", true),
				new ParseTestCase("1+i", true),
				new ParseTestCase("-8.99 + 9.11i", true),
				new ParseTestCase("3", true),
				new ParseTestCase("3-", false),
				new ParseTestCase("-3-", false),
				new ParseTestCase("-3.+2.i", true),
				new ParseTestCase("32.1-i12i", false),
				new ParseTestCase("i", true),
				new ParseTestCase("-2.71+-3.15i", false),
		};
		
		for(ParseTestCase testCase : tests) {
			boolean wasParsed;
			ComplexNumber parsedComplexNumber = null;
			
			try {
				parsedComplexNumber = ComplexNumber.parse(testCase.expression);
				wasParsed = true;
			} catch(NumberFormatException ex) {
				wasParsed = false;
			}
			
			if(wasParsed == testCase.expectedResult) { // Test was correct!
				if(wasParsed) {
					System.out.println("Parsed successfully for '" + testCase.expression + "' => " + parsedComplexNumber);
				} else {
					System.out.println("String '" + testCase.expression + "' could not be parsed.");
				}
			} else { // Test failed.
				fail("Test failed for expression '" + testCase.expression + "'. Expected result was "
					 + testCase.expectedResult + ", but actual result was " + wasParsed + ".");
			}
		}
	}
}
