package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing the {@code String} element.
 */
public class ElementString extends Element {
	/** value of this element */
	private String value;
	
	/**
	 * Constructs a new element that contains a {@code String} value.
	 * 
	 * @param value value to be stored
	 * @throws NullPointerException if passed {@code null} as an argument
	 */
	public ElementString(String value) {
		Objects.requireNonNull(value);
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		char[] chars = value.toCharArray();
		for(char c : chars) {
			if(c == '"' || c == '\\') {
				sb.append('\\');
			}
			sb.append(c);
		}
		
		return "\"" + sb.toString() + "\"";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementString))
			return false;
		ElementString other = (ElementString) obj;
		return asText().equals(other.asText());
	}
}
