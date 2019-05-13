package hr.fer.zemris.java.gui.layouts;

/**
 * Models the component's position in the
 * grid.
 *
 * @author Filip Nemec
 */
public class RCPosition {
	
	/** The row of the component. */
	public final int row;
	
	/** The column of the component. */
	public final int column;
	
	/**
	 * Constructs a new component position with
	 * the given row and column.
	 *
	 * @param row the row
	 * @param column the column
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
}
