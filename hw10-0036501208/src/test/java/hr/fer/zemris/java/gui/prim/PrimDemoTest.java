package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrimDemoTest {
	
	PrimDemo.PrimListModel model;
	
	@BeforeEach
	void init() {
		model = new PrimDemo.PrimListModel();
	}

	@Test
	void size1AtStartTest() {
		assertEquals(1, model.getSize());
	}
	
	@Test
	void nextIncreasesSizeTest() {
		model.next();
		assertEquals(2, model.getSize());
	}
	
	@Test
	void getElementAtTest() {
		assertEquals(1, model.getElementAt(0));
		
		model.next();
		assertEquals(2, model.getElementAt(1));
		
		model.next();
		assertEquals(3, model.getElementAt(2));
		
		model.next();
		assertEquals(5, model.getElementAt(3));
	}
	
	@Test
	void getElementAtOutOfBoundsTest() {
		assertThrows(IndexOutOfBoundsException.class, () -> model.getElementAt(1000));
	}
	
	@Test
	void addNullListenerTest() {
		assertThrows(NullPointerException.class, () -> model.addListDataListener(null));
	}
}
