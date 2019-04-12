package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw01.UniqueNumbers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

class UniqueNumbersTest {
	
	private static TreeNode glava = null;
	
	@BeforeAll
	static void insertData() {
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
	}
	
	@Test
	void addNodeTest() {
		assertEquals(42, glava.value);
		assertEquals(21, glava.left.value);
		assertEquals(35, glava.left.right.value);
		assertEquals(76, glava.right.value);
	}
	
	@Test
	void treeSizeTest() {
		assertNotEquals(0, treeSize(glava));
		assertEquals(4, treeSize(glava));
	}
	
	@Test
	void containsValueTest() {
		assertTrue(containsValue(glava, 76));
		assertFalse(containsValue(glava, 1000));
	}

}
