package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Defines a filter for queries which uses
 * conditional expressions to check given
 * student records.
 *
 * @author Filip Nemec
 */
public class QueryFilter implements IFilter {
	
	/** List of expressions that student record needs to pass in order to be accepted. */
	private List<ConditionalExpression> expressions;
	
	/**
	 * Constructs a new query filter.
	 *
	 * @param expressions list of expressions for this filter
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		this.expressions = Objects.requireNonNull(expressions, "Given expressions list cannot be null.");
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for(ConditionalExpression expression : expressions) {
			String field 				 = expression.getFieldGetter().get(record);
			IComparisonOperator operator = expression.getComparisonOperator();
			String pattern 				 = expression.getStringLiteral();
			
			if(!operator.satisfied(field, pattern)) return false;
		}
		
		return true;
	}
}
