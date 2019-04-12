package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentDatabaseTest {
	
	private StudentDatabase db;
	
	private static final IFilter alwaysTrue = record -> true;
	private static final IFilter alwaysFalse = record -> false;
	
	@BeforeEach
	void createDB() {
		List<String> rows = new LinkedList<String>();
		rows.add("1 last first 5");
		rows.add("2 last first 4");
		db = new StudentDatabase(rows);
	}

	@Test
	void alwaysTrueFilterTest() {
		List<StudentRecord> filteredList = db.filter(alwaysTrue);
		assertEquals(2, filteredList.size());
	}
	
	@Test
	void alwaysFalseFilterTest() {
		List<StudentRecord> filteredList = db.filter(alwaysFalse);
		assertEquals(0, filteredList.size());
	}
	
	@Test
	void forJMBAGTest() {
		StudentRecord record = db.forJMBAG("1");

		assertNotNull(record);
		assertEquals("1", record.getJmbag());
		assertEquals("last", record.getLastName());
		assertEquals("first", record.getFirstName());
		assertEquals(5, record.getFinalGrade());
	}
	
	@Test
	void forJMBAGNotFoundTest() {
		StudentRecord record = db.forJMBAG("7");
		assertNull(record);
	}

}
