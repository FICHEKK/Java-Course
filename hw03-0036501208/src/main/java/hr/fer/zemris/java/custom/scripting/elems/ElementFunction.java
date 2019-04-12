package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing the function element.
 */
public class ElementFunction extends Element {
	/** the name of the function */
	private String name;
	
	/**
	 * Constructs a new element that contains a function stored
	 * as a {@code String}.
	 * 
	 * @param name name of the function
	 * @throws NullPointerException if passed {@code null} as an argument
	 */
	public ElementFunction(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return "@" + name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementFunction))
			return false;
		ElementFunction other = (ElementFunction) obj;
		return asText().equals(other.asText());
	}
}
