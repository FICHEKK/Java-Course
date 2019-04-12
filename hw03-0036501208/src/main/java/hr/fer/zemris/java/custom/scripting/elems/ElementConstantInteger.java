package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class representing the {@code int} constant.
 */
public class ElementConstantInteger extends Element {
	/** the value of this element */
	private int value;
	
	/**
	 * Constructs a new element that contains an {@code int} value.
	 * 
	 * @param value value to be stored in this element
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return Integer.toString(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementConstantInteger))
			return false;
		ElementConstantInteger other = (ElementConstantInteger) obj;
		return asText().equals(other.asText());
	}
}
