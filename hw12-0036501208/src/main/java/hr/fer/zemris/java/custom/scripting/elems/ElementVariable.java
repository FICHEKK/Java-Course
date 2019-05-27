package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing the variable element.
 */
public class ElementVariable extends Element {
	
	/** name of the variable */
	private String name;
	
	/**
	 * Constructs a new element that contains a variable stored
	 * as a {@code String}.
	 * 
	 * @param name name of the variable
	 * @throws NullPointerException if passed {@code null} as an argument
	 */
	public ElementVariable(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementVariable))
			return false;
		ElementVariable other = (ElementVariable) obj;
		return asText().equals(other.asText());
	}
}
