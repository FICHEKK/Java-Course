package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Encapsulates the bar chart information.
 *
 * @author Filip Nemec
 */
public class BarChart {
	
	/** The list of (x, y) value samples. */
	private List<XYValue> values;
	
	/** The title of the x-axis. */
	private String titleX;
	
	/** The title of the y-axis. */
	private String titleY;
	
	/** The minimum y value. */
	private int minY;
	
	/** The maximum y value. */
	private int maxY;
	
	/** Defines how often the y value on y-axis will be shown. */
	private int interval;
	
	/**
	 * Constructs a new bar chart information object.
	 *
	 * @param values the list of sample values
	 * @param titleX the title of the x-axis
	 * @param titleY the title of the y-axis
	 * @param minY the minimum y value
	 * @param maxY the maximum y value
	 * @param interval the y-axis interval
	 */
	public BarChart(List<XYValue> values, String titleX, String titleY,
					int minY, int maxY, int interval) {
		if(minY < 0)
			throw new IllegalArgumentException("Minimum y value should not be negative.");
		
		if(!(maxY > minY))
			throw new IllegalArgumentException("Maximum y value should be greater than the minimum.");
		
		if((maxY - minY) % interval != 0) {
			maxY = (int) (minY + Math.ceil((maxY - minY) / (double) interval) * interval);
		}
		
		for(XYValue value : values) {
			if(value.y < minY) {
				var msg = "Given sample's " + value + " y-value is lower than the minimum y-value " + minY + ".";
				throw new IllegalArgumentException(msg);
			}
		}
		
		this.values = values;
		this.titleX = titleX;
		this.titleY = titleY;
		this.minY = minY;
		this.maxY = maxY;
		this.interval = interval;
	}


	//--------------------------------------------------------------
	//							GETTERS
	//--------------------------------------------------------------
	
	/**
	 * Returns the list of samples that this bar chart holds.
	 *
	 * @return the list of samples that this bar chart holds
	 */
	public List<XYValue> getValues() {
		return values;
	}
	
	/**
	 * Returns the title of the x-axis.
	 *
	 * @return the title of the x-axis
	 */
	public String getTitleX() {
		return titleX;
	}

	/**
	 * Returns the title of the y-axis.
	 *
	 * @return the title of the y-axis
	 */
	public String getTitleY() {
		return titleY;
	}
	
	/**
	 * Returns the minimum y value.
	 *
	 * @return the minimum y value
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Returns the maximum y value.
	 *
	 * @return the maximum y value
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Returns the y-axis interval.
	 *
	 * @return the y-axis interval
	 */
	public int getInterval() {
		return interval;
	}
}
