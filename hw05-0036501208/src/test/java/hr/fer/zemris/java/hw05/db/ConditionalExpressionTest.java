package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConditionalExpressionTest {
	
	private static final StudentRecord record = new StudentRecord("0001", "Anić", "Ana", 5);

	@Test
	void firstNameEqualsExpression() {
		ConditionalExpression condition = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Ana", ComparisonOperators.EQUALS);
		
		IFieldValueGetter getter 	 = condition.getFieldGetter();
		String literal 				 = condition.getStringLiteral();
		IComparisonOperator operator = condition.getComparisonOperator();
		
		assertTrue(operator.satisfied(getter.get(record), literal));
	}
	
	@Test
	void lastNameEqualsExpression() {
		ConditionalExpression condition = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Anić", ComparisonOperators.EQUALS);
		
		IFieldValueGetter getter 	 = condition.getFieldGetter();
		String literal 				 = condition.getStringLiteral();
		IComparisonOperator operator = condition.getComparisonOperator();
		
		assertTrue(operator.satisfied(getter.get(record), literal));
	}
	
	@Test
	void jmbagEqualsExpression() {
		ConditionalExpression condition = new ConditionalExpression(FieldValueGetters.JMBAG, "0001", ComparisonOperators.EQUALS);
		
		IFieldValueGetter getter 	 = condition.getFieldGetter();
		String literal 				 = condition.getStringLiteral();
		IComparisonOperator operator = condition.getComparisonOperator();
		
		assertTrue(operator.satisfied(getter.get(record), literal));
	}
	
	@Test
	void firstNameLikeExpression() {
		ConditionalExpression condition = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A*", ComparisonOperators.LIKE);
		
		IFieldValueGetter getter 	 = condition.getFieldGetter();
		String literal 				 = condition.getStringLiteral();
		IComparisonOperator operator = condition.getComparisonOperator();
		
		assertTrue(operator.satisfied(getter.get(record), literal));
	}
	
	@Test
	void firstNameLessExpression() {
		ConditionalExpression condition = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Zzz", ComparisonOperators.LESS);
		
		IFieldValueGetter getter 	 = condition.getFieldGetter();
		String literal 				 = condition.getStringLiteral();
		IComparisonOperator operator = condition.getComparisonOperator();
		
		assertTrue(operator.satisfied(getter.get(record), literal));
	}
}
