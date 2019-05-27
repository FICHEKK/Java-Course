package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing the operator element.
 */
public class ElementOperator extends Element {
	
	/** value of the operator */
	private String symbol;
	
	/**
	 * Constructs a new element that contains an operator stored
	 * as a {@code String}.
	 * 
	 * @param symbol symbol to be stored
	 * @throws NullPointerException if passed {@code null} as an argument
	 */
	public ElementOperator(String symbol) {
		Objects.requireNonNull(symbol);
		this.symbol = symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementOperator))
			return false;
		ElementOperator other = (ElementOperator) obj;
		return asText().equals(other.asText());
	}
}
