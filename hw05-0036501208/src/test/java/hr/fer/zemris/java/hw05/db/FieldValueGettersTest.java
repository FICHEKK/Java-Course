package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {
	
	private static final IFieldValueGetter firstNameGetter = FieldValueGetters.FIRST_NAME;
	private static final IFieldValueGetter lastNameGetter = FieldValueGetters.LAST_NAME;
	private static final IFieldValueGetter jmbagGetter = FieldValueGetters.JMBAG;
	
	private static final StudentRecord student = new StudentRecord("001", "LN", "FN", 5);

	@Test
	void firstNameGetterTest() {
		assertEquals("001", jmbagGetter.get(student));
	}
	
	@Test
	void lastNameGetterTest() {
		assertEquals("LN", lastNameGetter.get(student));
	}
	
	@Test
	void jmbagGetterTest() {
		assertEquals("FN", firstNameGetter.get(student));
	}
	
	@Test
	void jmbagGetterNullRecordTest() {
		assertThrows(NullPointerException.class, () -> jmbagGetter.get(null));
	}
	
	@Test
	void lastNameGetterNullRecordTest() {
		assertThrows(NullPointerException.class, () -> lastNameGetter.get(null));
	}
	
	@Test
	void firstNameGetterNullRecordTest() {
		assertThrows(NullPointerException.class, () -> firstNameGetter.get(null));
	}
}
