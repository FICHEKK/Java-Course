package hr.fer.zemris.java.hw05.db;

/**
 * A simple class that encapsulates a single conditional expression
 * that consists of a single field, comparison operator and a 
 * string literal.
 * <p>
 * To provide an example, this class could model these expressions:
 * <ul>
 * 		<li> firstName = "James" </li>
 * 		<li> lastName != "Bond" </li>
 *  	<li> jmbag >= "0000000007" </li>
 *  	<li> field LIKE "A*" </li>
 * </ul>
 * 
 * @author Filip Nemec
 */
public class ConditionalExpression {
	
	/** A field getter strategy. */
	private IFieldValueGetter fieldGetter;
	
	/** A string literal. */
	private String stringLiteral;
	
	/** A comparison operator strategy. */
	IComparisonOperator comparisonOperator;
	
	/**
	 * Constructs a new conditional expression.
	 * 
	 * @param fieldGetter the field getter
	 * @param stringLiteral the string literal
	 * @param comparisonOperator the comparison operator
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter,
								 String stringLiteral,
								 IComparisonOperator comparisonOperator) {
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Returns this expression's field getter.
	 * @return this expression's field getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Returns this expression's string literal.
	 * @return this expression's string literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}
	
	/**
	 * Returns this expression's comparison operator.
	 * @return this expression's comparison operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
}
