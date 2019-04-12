package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector2DTest {
	
	/** Maximum allowed difference between 2 {@code double} values to still be considered as equal. */
	private final double EPSILON = 1E-7;
	
	@Test
	void lengthTest() {
		var vector = new Vector2D(1, 0);
		assertEquals(1, vector.length() , EPSILON);
		
		var vector2 = new Vector2D(-1, -1);
		assertEquals(Math.sqrt(2), vector2.length() , EPSILON);
		
		var vector3 = new Vector2D(0, 0);
		assertEquals(0, vector3.length() , EPSILON);
	}
	
	@Test
	void angleTest() {
		var vector1 = new Vector2D(1, 0);
		assertEquals(Math.PI * 0 / 4, vector1.angle() , EPSILON);
		
		var vector2 = new Vector2D(1, 1);
		assertEquals(Math.PI * 1 / 4, vector2.angle() , EPSILON);
		
		var vector3 = new Vector2D(0, 1);
		assertEquals(Math.PI * 2 / 4, vector3.angle() , EPSILON);
		
		var vector4 = new Vector2D(-1, 1);
		assertEquals(Math.PI * 3 / 4, vector4.angle() , EPSILON);
		
		var vector5 = new Vector2D(-1, 0);
		assertEquals(Math.PI * 4 / 4, vector5.angle() , EPSILON);
		
		var vector6 = new Vector2D(-1, -1);
		assertEquals(Math.PI * 5 / 4, vector6.angle() , EPSILON);
		
		var vector7 = new Vector2D(0, -1);
		assertEquals(Math.PI * 6 / 4, vector7.angle() , EPSILON);
		
		var vector8 = new Vector2D(1, -1);
		assertEquals(Math.PI * 7 / 4, vector8.angle() , EPSILON);
	}
	
	@Test
	void scaleTest() {
		var vector = new Vector2D(1, 1);
		vector.scale(2.0);
		assertEquals(Math.sqrt(8), vector.length() , EPSILON);
	}
	
	@Test
	void scaleByZeroTest() {
		var vector = new Vector2D(1, 1);
		vector.scale(0);
		assertEquals(0, vector.length() , EPSILON);
	}
	
	@Test
	void translateTest() {
		var vector = new Vector2D(1, 1);
		vector.translate(new Vector2D(2, 3));
		assertEquals(3, vector.getX(), EPSILON);
		assertEquals(4, vector.getY(), EPSILON);
		
		vector.translate(new Vector2D(-3, -4));
		assertEquals(0, vector.getX(), EPSILON);
		assertEquals(0, vector.getY(), EPSILON);
	}
	
	@Test
	void rotateTest() {
		var vector = new Vector2D(1, 0);
		
		vector.rotate(Math.PI / 2);
		assertEquals(0, vector.getX() , EPSILON);
		assertEquals(1, vector.getY() , EPSILON);
		
		vector.rotate(Math.PI / 2);
		assertEquals(-1, vector.getX() , EPSILON);
		assertEquals(0, vector.getY() , EPSILON);
	}
	
	@Test
	void scaledTest() {
		var vector = new Vector2D(2, -3);
		var scaled = vector.scaled(2.0);
		assertEquals(4, scaled.getX() , EPSILON);
		assertEquals(-6, scaled.getY() , EPSILON);
	}
	
	@Test
	void translatedTest() {
		var vector = new Vector2D(2, 3);
		var translated = vector.translated(new Vector2D(1, 1));
		assertEquals(3, translated.getX() , EPSILON);
		assertEquals(4, translated.getY() , EPSILON);
	}
	
	@Test
	void rotatedTest() {
		var vector = new Vector2D(1, 0);
		
		var rotated1 = vector.rotated(Math.PI / 2);
		assertEquals(0, rotated1.getX() , EPSILON);
		assertEquals(1, rotated1.getY() , EPSILON);
		
		var rotated2 = vector.rotated(Math.PI);
		assertEquals(-1, rotated2.getX() , EPSILON);
		assertEquals(0, rotated2.getY() , EPSILON);
	}
	
	@Test
	void copyTest() {
		var vector = new Vector2D(2.3, -3.5);
		var copy = vector.copy();
		assertEquals(2.3, copy.getX() , EPSILON);
		assertEquals(-3.5, copy.getY() , EPSILON);
	}
}
