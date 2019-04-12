package hr.fer.zemris.java.hw05.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw05.db.exceptions.QueryParserException;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.QueryToken;
import hr.fer.zemris.java.hw05.db.lexer.QueryTokenType;

/**
 * A simple query parser that uses a lexer to
 * create a list of conditional expressions.
 *
 * @author Filip Nemec
 */
public class QueryParser {
	
	/** A list of conditional expression. */
	private List<ConditionalExpression> conditionalExpressions;
	
	/** A lexer for this parser. */
	private QueryLexer lexer;
	
	/**
	 * Constructs a new query parser that will parse the given
	 * {@code String} query.
	 *
	 * @param query the query to be parsed
	 */
	public QueryParser(String query) {
		Objects.requireNonNull(query);
		conditionalExpressions = new LinkedList<ConditionalExpression>();
		lexer = new QueryLexer(query);
		processTokens();
	}
	
	/**
	 * Processes all of the tokens that the lexer returns until
	 * the returned token was EOF token.
	 */
	private void processTokens() {
		while(true) {
			try {
				QueryToken field = lexer.nextToken();
				QueryToken operator = lexer.nextToken();
				QueryToken pattern = lexer.nextToken();
				
				createConditionalExpression(field, operator, pattern);
				
			} catch(IllegalStateException ise) {
				String msg = "Conditional expression should consist of field, operator and pattern.";
				throw new QueryParserException(msg);
			}
			
			QueryToken afterExpressionToken = lexer.nextToken();
			QueryTokenType type = afterExpressionToken.getType();
			Object value = afterExpressionToken.getValue();
			
			if(type == QueryTokenType.EOF) break;
			
			if(type != QueryTokenType.IDENTIFIER || !((String) value).equalsIgnoreCase("AND"))
				throw new QueryParserException("Expected token 'AND', was: " + value);
		}
	}
	
	/**
	 * Creates a conditional expression from the given field,
	 * operator and pattern token.
	 *
	 * @param fieldToken the field token
	 * @param operatorToken the operator token
	 * @param patternToken the pattern token
	 * @throws QueryParserException if any of the provided tokens are invalid
	 */
	private void createConditionalExpression(QueryToken fieldToken, QueryToken operatorToken, QueryToken patternToken) {
		if(fieldToken.getType()    != QueryTokenType.IDENTIFIER)
			throw new QueryParserException("Given field token '" + fieldToken.getValue() + "' is not of type IDENTIFIER.");
		
		if(operatorToken.getType() != QueryTokenType.OPERATOR)
			throw new QueryParserException("Given operator token '" + operatorToken.getValue() + "' is not of type OPERATOR.");
		
		if(patternToken.getType() != QueryTokenType.STRING)
			throw new QueryParserException("Given pattern token '" + patternToken.getValue() + "' is not of type STRING.");
			
		IFieldValueGetter getter = getFieldGetter(fieldToken.getValue());
		IComparisonOperator operator = getComparisonOperator(operatorToken.getValue());
		String pattern = patternToken.getValue();
		
		conditionalExpressions.add(new ConditionalExpression(getter, pattern, operator));
	}
	
	/**
	 * Returns an appropriate field getter for the given {@code String}.
	 *
	 * @param field the field
	 * @return field getter for the given {@code String}
	 * @throws QueryParserException if the given field is invalid
	 */
	private static IFieldValueGetter getFieldGetter(String field) {
		if(field.equals("jmbag")) {
			return FieldValueGetters.JMBAG;
			
		} else if(field.equals("firstName")) {
			return FieldValueGetters.FIRST_NAME;
			
		} else if(field.equals("lastName")) {
			return FieldValueGetters.LAST_NAME;
			
		} else {
			throw new QueryParserException("Given field '" + field + "' is invalid.");
		
		}
	}
	
	/**
	 * Returns a comparison operator for the given {@code String}.
	 *
	 * @param operator the {@code String} to be processes
	 * @return comparison operator for the given {@code String}
	 * @throws QueryParserException if the given operator is invalid
	 */
	private static IComparisonOperator getComparisonOperator(String operator) {
		if(operator.equals("=")) {
			return ComparisonOperators.EQUALS;
			
		} else if(operator.equals("!=")) {
			return ComparisonOperators.NOT_EQUALS;
			
		} else if(operator.equals(">")) {
			return ComparisonOperators.GREATER;
			
		} else if(operator.equals(">=")) {
			return ComparisonOperators.GREATER_OR_EQUALS;
			
		} else if(operator.equals("<")) {
			return ComparisonOperators.LESS;
			
		} else if(operator.equals("<=")) {
			return ComparisonOperators.LESS_OR_EQUALS;
			
		} else if(operator.equalsIgnoreCase("LIKE")) {
			return ComparisonOperators.LIKE;
			
		} else {
			throw new QueryParserException("Given operator '" + operator + "' is invalid.");
			
		}
	}
	
	/**
	 * Checks if the given query is a direct query, meaning
	 * it is of format <i><b>jmbag = "xxx"</b></i>. This type
	 * of query can be executed in O(1) time complexity.
	 * 
	 * @return {@code true} if this query is a direct query,
	 * 		   otherwise {@code false}
	 */
	public boolean isDirectQuery() {
		if(conditionalExpressions.size() != 1) return false;
		
		ConditionalExpression expression = conditionalExpressions.get(0);
		
		return expression.getComparisonOperator() == ComparisonOperators.EQUALS &&
			   expression.getFieldGetter() == FieldValueGetters.JMBAG;
	}
	
	/**
	 * Returns the queried JMBAG if this query is a direct query,
	 * otherwise throws {@code IllegalStateException}.
	 *
	 * @return the queried JMBAG
	 * @throws IllegalStateException if this query is not a direct query
	 */
	public String getQueriedJMBAG() {
		if(!isDirectQuery())
			throw new IllegalStateException("This query is not a direct query.");
		
		return conditionalExpressions.get(0).getStringLiteral();
	}
	
	/**
	 * Returns a list of conditional expression that this
	 * parser holds.
	 *
	 * @return list of conditional expression
	 */
	public List<ConditionalExpression> getQuery() {
		return conditionalExpressions;
	}
}
