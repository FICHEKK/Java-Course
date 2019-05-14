package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.Arrays;

/**
 * A layout used by the calculator application.
 *
 * @author Filip Nemec
 */
public class CalcLayout implements LayoutManager2 {
	
	/** The number of rows. */
	private static final int ROWS = 5;
	
	/** The number of columns. */
	private static final int COLUMNS = 7;
	
	/** A grid storing the references to each component in the calculator. */
	private Component[][] grid = new Component[ROWS][COLUMNS];
	
	/** The gap between individual components. */
	private final int gap;
	
	/** Caching the widths array for the reuse. */
	private int[] buttonWidths = new int[COLUMNS];
	
	/** Caching the heights array for the reuse. */
	private int[] buttonHeights = new int[ROWS];
	
	/**
	 * Constructs the default calculator layout with the gap 0.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Constructs the calculator layout with the specified gap.
	 *
	 * @param gap the gap between each component.
	 */
	public CalcLayout(int gap) {
		this.gap = gap;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("Use 'addLayoutComponent(Component comp, Object constraints)' instead.");
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if(comp == null) return;
		
		for(int y = 0; y < ROWS; y++) {
			for(int x = 0; x < COLUMNS; x++) {
				if(grid[y][x] == comp) {
					grid[y][x] = null;
					return;
				}
			}
		}
	}

	@Override
	public void layoutContainer(Container parent) {
		Dimension dimension = parent.getSize();
		Insets insets = parent.getInsets();
		
		int parentWidth = dimension.width - insets.left - insets.right - (COLUMNS - 1) * gap;
		int parentHeight = dimension.height - insets.top - insets.bottom - (ROWS - 1) * gap;
		
		getUniformDistribution(buttonWidths, parentWidth, COLUMNS);
		getUniformDistribution(buttonHeights, parentHeight, ROWS);
		
		//---------------------------------------------
		//  First row is special, handle it separately.
		//---------------------------------------------
		if(grid[0][0] != null) {
			Component display = grid[0][0];
			
			int displayWidth = gap * 4;
			
			for(int i = 0; i < 5; i++) {
				displayWidth += buttonWidths[i];
			}
			
			int displayHeight = buttonHeights[0];
			
			display.setBounds(insets.left, insets.top, displayWidth, displayHeight);
		}
		
		int firstRowX = insets.left;
		for(int x = 0; x <= 6; x++) {
			if(x >= 5) {
				Component button = grid[0][x];
				if(button != null) {
					button.setBounds(firstRowX, insets.top, buttonWidths[x], buttonHeights[0]);
				}
			}
			
			firstRowX += buttonWidths[x] + gap;
		}

		//---------------------------------------------
		// 			The rest of the buttons.
		//---------------------------------------------
		int currentX = insets.left;
		int currentY = buttonHeights[0] + gap + insets.top;
		
		for(int y = 1; y < ROWS; y++) {
			for(int x = 0; x < COLUMNS; x++) {
				Component button = grid[y][x];
				if(button != null) {
					button.setBounds(currentX, currentY, buttonWidths[x], buttonHeights[y]);
				}
				
				currentX += buttonWidths[x] + gap;
			}
			
			currentX = insets.left;
			currentY += buttonHeights[y] + gap;
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition p = validateConstraints(constraints);
		int X = p.column - 1;
		int Y = p.row - 1;

		// Check if the spot is already occupied.
		Component onGrid = grid[Y][X];
		if(onGrid != null && onGrid != comp)
			throw new CalcLayoutException("Spot at (" + p.row + ", " + p.column + ") is already occupied.");
		
		// If not occupied, add it to the array.
		grid[Y][X] = comp;
	}
	
	//-------------------------------------------------------------------
	//						  LAYOUT SIZES
	//-------------------------------------------------------------------
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return getLayoutSize(parent, Component::getPreferredSize);
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return getLayoutSize(parent, Component::getMinimumSize);
	}
	
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return getLayoutSize(target, Component::getMaximumSize);
	}
	
	//-------------------------------------------------------------------
	//						  UNUSED METHODS
	//-------------------------------------------------------------------
	
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}
	
	@Override
	public void invalidateLayout(Container target) {
	}
	
	//-------------------------------------------------------------------
	//							HELPER METHODS
	//-------------------------------------------------------------------
	
	/**
	 * Checks if the given constraints satisfy all the predefined conditions.
	 *
	 * @param constraints the constraints
	 * @return the {@code RCPosition} position, if valid
	 * @throws CalcLayoutException if the constraints don't satisfy all of the conditions
	 */
	private RCPosition validateConstraints(Object constraints) {
		RCPosition position = null;
		
		if(constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
			
		} else if(constraints instanceof String) {
			position = convertFromString((String) constraints);
			
		} else {
			var msg = "Constraints should be of type 'RCPosition' or 'String'. Was '" + constraints.getClass().getName() + "'.";
			throw new CalcLayoutException(msg);
		}
		
		
		if(position.row < 1 || position.row > 5)
			throw new CalcLayoutException("Row out of bounds for input '" + position.row + "'");
		
		if(position.column < 1 || position.column > 7)
			throw new CalcLayoutException("Column out of bounds for input '" + position.column + "'");
		
		if(position.row == 1 && (position.column > 1 && position.column < 6))
			throw new CalcLayoutException("Invalid column for position (1, " + position.column + "). Expected (1, 1).");
				
		return position;
	}
	
	/**
	 * Converts the given constraints {@code String} to the {@code RCPosition}
	 * object. This method expects a {@code String} of length 3 and of this exact
	 * format: "row,column", where both row and column are a single digit numbers.
	 *
	 * @param constraints the {@code String} to be converted
	 * @return the {@code RCPosition} object represented by the given input argument
	 */
	private RCPosition convertFromString(String constraints) {
		if(constraints.length() != 3) {
			var msg = "Expected string of length 3 and format 'row,column'. " +
					  "String '" + constraints + "' has length of " + constraints.length() + ".";
			throw new CalcLayoutException(msg);
		}
			
		int row    = constraints.charAt(0) - '0';
		int column = constraints.charAt(2) - '0';

		return new RCPosition(row, column);
	}
	
	/**
	 * Calculates the uniform distribution for the given number. For example,
	 * for number 200 and 7 parts, it will get distributed into an array
	 * [29, 28, 29, 28, 29, 28, 29].
	 *
	 * @param storage the storage to fill with the calculated distribution
	 * @param toDistribute number to distribute
	 * @param nParts number of parts
	 * @return the uniformly distributed array of {@code int} values
	 */
	private void getUniformDistribution(int[] storage, int toDistribute, int nParts) {
		// 200 / 7 = 28.57... -> int rounding -> 28
		int partSize = toDistribute / nParts;
		Arrays.fill(storage, partSize);
		
		// 200 - 28*7 = 4 -> we need to add 4 more units uniformly
		int missing = toDistribute - partSize * nParts;
		
		if(missing > 0) {
			// 7 / 4 = 1.75 -> round -> 2 -> each 2 indexes add 1 unit
			int indexIncrease = Math.round(nParts / (float)missing);

			for(int i = 0; i < nParts; i += indexIncrease) {
				++storage[i];
			}
		}
	}
	
	/**
	 * Returns the layout size. Individual child component sizes will be
	 * extracted as defined by the given {@code SizeExtractor} argument.
	 *
	 * @param parent the {@code Container} whose layout size is being calculated
	 * @param extractor the component size extractor
	 * @return the {@code Dimension} instance which holds the layout size
	 */
	private Dimension getLayoutSize(Container parent, SizeExtractor extractor) {
		int width = 0;
		int height = 0;
	
		Dimension maxDimension = getMaximumDimension(parent, extractor);
		
		// Adding component dimensions
		width += maxDimension.width * COLUMNS;
		height += maxDimension.height * ROWS;
		
		// Adding spaces between components
		width += (COLUMNS - 1) * gap;
		height += (ROWS - 1) * gap;
		
		// Adding insets
		Insets insets = parent.getInsets();
		width += insets.left + insets.right;
		height += insets.top + insets.bottom;
		
		return new Dimension(width, height);
	}
	
	/**
	 * Searches for the maximum width and height sizes of any present
	 * components and returns those values encapsulated as an instance of
	 * {@code Dimension} object. Size retrieval is defined by the given
	 * strategy {@code SizeExtractor}.
	 *
	 * @param parent the {@code Container} containing all of the components
	 * @param extractor the object which defines how component sizes will 
	 * 					be retrieved from the components
	 * @return the {@code Dimension} containing the maximum preferred 
	 * 		   width and height
	 */
	private Dimension getMaximumDimension(Container parent, SizeExtractor extractor) {
		int maxWidth = 0;
		int maxHeight = 0;
		
		for(int y = 0; y < ROWS; y++) {
			for(int x = 0; x < COLUMNS; x++) {
				Component c = grid[y][x];
				if(c == null || !c.isVisible() || (x == 0 && y == 0))
					continue;
				
				Dimension dimension = extractor.getSize(c);
				
				int width = dimension.width;
				if(width > maxWidth) {
					maxWidth = width;
				}
				
				int height = dimension.height;
				if(height > maxHeight) {
					maxHeight = height;
				}
			}
		}
		
		// Display is a special case which also needs to be checked.
		Component display = grid[0][0];
		if(display != null && display.isVisible()) {
			Dimension dimension = extractor.getSize(display);
			
			int displayWidth = (dimension.width - 4 * gap) / 5;
			if(displayWidth > maxWidth) {
				maxWidth = displayWidth;
			}
			
			int displayHeight = dimension.height;
			if(displayHeight > maxHeight) {
				maxHeight = displayHeight;
			}
		}
		
		return new Dimension(maxWidth, maxHeight);
	}
	
	/**
	 * Models objects which define how the size should be extracted
	 * from the given component.
	 *
	 * @author Filip Nemec
	 */
	@FunctionalInterface
	private interface SizeExtractor {
		
		/**
		 * Extracts the size from the component.
		 *
		 * @param component the component
		 * @return the size from the component
		 */
		Dimension getSize(Component component);
	}
}
