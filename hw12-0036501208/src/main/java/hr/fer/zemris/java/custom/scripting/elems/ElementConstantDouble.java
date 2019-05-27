package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class representing the {@code double} constant.
 */
public class ElementConstantDouble extends Element {
	
	/** the value of this element */
	private double value;
	
	/**
	 * Constructs a new element that contains a {@code double} value.
	 * 
	 * @param value value to be stored in this element
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return Double.toString(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementConstantDouble))
			return false;
		ElementConstantDouble other = (ElementConstantDouble) obj;
		return asText().equals(other.asText());
	}
}
