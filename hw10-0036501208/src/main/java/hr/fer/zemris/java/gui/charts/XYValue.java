package hr.fer.zemris.java.gui.charts;

/**
 * Models a simple graph value.
 *
 * @author Filip Nemec
 */
public class XYValue {
	
	/** The x value. */
	public final int x;
	
	/** The y value. */
	public final int y;
	
	/**
	 * Constructs a new (x, y) value pair.
	 *
	 * @param x the x value
	 * @param y the y value
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
