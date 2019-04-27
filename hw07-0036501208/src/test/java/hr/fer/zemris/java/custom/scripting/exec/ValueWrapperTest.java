package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {
	
	private static final double EPSILON = 1E-7;

	@Test
	void addDoubleTest() {
		ValueWrapper vw = new ValueWrapper(10.0);
		
		vw.add("5");
		assertEquals(15.0, (Double) vw.getValue(), EPSILON);
		
		vw.add(10.0);
		assertEquals(25.0, (Double) vw.getValue(), EPSILON);
	}
	
	@Test
	void addIntegerTest() {
		ValueWrapper vw = new ValueWrapper(10);
		
		vw.add("5");
		assertEquals(15, (Integer) vw.getValue());
		
		vw.add(10);
		assertEquals(25, (Integer) vw.getValue());
	}
	
	@Test
	void subDoubleTest() {
		ValueWrapper vw = new ValueWrapper(10.0);
		
		vw.subtract("5");
		assertEquals(5.0, (Double) vw.getValue(), EPSILON);
		
		vw.subtract(10.0);
		assertEquals(-5.0, (Double) vw.getValue(), EPSILON);
	}
	
	@Test
	void subIntegerTest() {
		ValueWrapper vw = new ValueWrapper(10);
		
		vw.subtract("5");
		assertEquals(5, (Integer) vw.getValue());
		
		vw.subtract(10);
		assertEquals(-5, (Integer) vw.getValue());
	}
	
	@Test
	void mulDoubleTest() {
		ValueWrapper vw = new ValueWrapper(10.0);
		
		vw.multiply("5");
		assertEquals(50.0, (Double) vw.getValue(), EPSILON);
		
		vw.multiply(10.0);
		assertEquals(500.0, (Double) vw.getValue(), EPSILON);
	}
	
	@Test
	void mulIntegerTest() {
		ValueWrapper vw = new ValueWrapper(10);
		
		vw.multiply("5");
		assertEquals(50, (Integer) vw.getValue());
		
		vw.multiply(10);
		assertEquals(500, (Integer) vw.getValue());
	}
	
	@Test
	void divDoubleTest() {
		ValueWrapper vw = new ValueWrapper(10.0);
		
		vw.divide("5");
		assertEquals(2.0, (Double) vw.getValue(), EPSILON);
		
		vw.divide(10.0);
		assertEquals(0.2, (Double) vw.getValue(), EPSILON);
	}
	
	@Test
	void divIntegerTest() {
		ValueWrapper vw = new ValueWrapper(10);
		
		vw.divide("5");
		assertEquals(2, (Integer) vw.getValue());
		
		vw.divide(10);
		assertEquals(0, (Integer) vw.getValue());
	}
	
	@Test
	void compareDoubleTest() {
		ValueWrapper vw = new ValueWrapper(10.0);
		
		assertTrue(vw.numCompare(1.0) == 1);
		assertTrue(vw.numCompare(10.0) == 0);
		assertTrue(vw.numCompare(100.0) == -1);
	}
	
	@Test
	void compareIntegerTest() {
		ValueWrapper vw = new ValueWrapper(10);
		
		assertTrue(vw.numCompare(1) == 1);
		assertTrue(vw.numCompare(10) == 0);
		assertTrue(vw.numCompare(100) == -1);
	}
	
	@Test
	void arithmeticWithNullTest() {
		// integer arithmetic
		ValueWrapper vwInt = new ValueWrapper(10);
		
		vwInt.add(null);
		assertEquals(10, vwInt.getValue());
		
		vwInt.subtract(null);
		assertEquals(10, vwInt.getValue());
		
		vwInt.multiply(null);
		assertEquals(0, vwInt.getValue());
		
		// double arithmetic
		ValueWrapper vwDouble = new ValueWrapper(7.0);
		
		vwDouble.add(null);
		assertEquals(7.0, (Double) vwDouble.getValue(), EPSILON);
		
		vwDouble.subtract(null);
		assertEquals(7.0, (Double) vwDouble.getValue(), EPSILON);
		
		vwDouble.multiply(null);
		assertEquals(0.0, (Double) vwDouble.getValue(), EPSILON);
	}
	
	@Test
	void divideByZeroTest() {
		ValueWrapper vw = new ValueWrapper(10);
		
		assertThrows(ArithmeticException.class, () -> vw.divide(0));
		assertThrows(ArithmeticException.class, () -> vw.divide(null));
	}
	
	@Test
	void invalidTypeArithmeticTest() {
		ValueWrapper vw = new ValueWrapper(10);
		Object object = new Object();
		
		assertThrows(IllegalArgumentException.class, () -> vw.add(object));
		assertThrows(IllegalArgumentException.class, () -> vw.subtract(object));
		assertThrows(IllegalArgumentException.class, () -> vw.multiply(object));
		assertThrows(IllegalArgumentException.class, () -> vw.divide(object));
		
		assertThrows(NumberFormatException.class, () -> vw.add("Štefica"));
		assertThrows(NumberFormatException.class, () -> vw.add("3.3.3"));
	}
	
	@Test
	void getterSetterTest() {
		ValueWrapper vw = new ValueWrapper(10);
		assertEquals(10, vw.getValue());
		
		vw.setValue(20);
		assertEquals(20, vw.getValue());
		
		vw.setValue("20");
		assertEquals("20", vw.getValue());
	}
}
