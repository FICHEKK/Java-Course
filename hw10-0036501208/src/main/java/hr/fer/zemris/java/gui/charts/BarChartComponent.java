package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import javax.swing.JComponent;

/**
 * Component that represents the bar chart. It order
 * for this component to draw itself correctly, an
 * instance of a {@code BarChart} object is needed.
 *
 * @author Filip Nemec
 */
public class BarChartComponent extends JComponent {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Distance from the edge to the x-axis. */
	private final int xAxisOffset;
	
	/** Distance from the edge to the y-axis. */
	private final int yAxisOffset;
	
	/** Distance from the edge to the x-axis title. */
	private static final int xAxisTitleOffset = 15;
	
	/** Distance from the edge to the y-axis title. */
	private static final int yAxisTitleOffset = 15;
	
	/** The width of the arrow at the end of the axis. */
	private static final int arrowWidth = 10;
	
	/** The height of the arrow at the end of the axis. */
	private static final int arrowHeight = 10;
	
	/** Length of the axis mark. */
	private static final int markLength = 10;
	
	/** Space between axis arrow and the last mark on the axis. */
	private static final int spaceBetweenArrowAndLastMark = 10;
	
	/** Reduces the width of the column (on each side). */
	private static final int columnWidthReduction = 5;
	
	/** Distance from the x-axis to numbers on the x-axis. */
	private static final int xTextOffset = 20;
	
	/** Distance from the y-axis to numbers on the y-axis. */
	private static final int yTextOffset = 20;
	
	/** Font used by the axis titles. */
	private static final Font AXIS_TITLE_FONT = new Font("Arial", Font.PLAIN, 14);
	
	/** Font used by the axis marks. */
	private static final Font AXIS_MARK_FONT = new Font("Arial", Font.BOLD, 14);
	
	/** The color of the columns. */
	private static final Color COLUMN_COLOR = new Color(0, 114, 191);
	
	/** Object that stores all the bar chart data. */
	private BarChart chart;
	
	/**
	 * Constructs a new bar chart. It uses information
	 * provided by the {@code chart} argument to correctly
	 * draw the graph.
	 *
	 * @param chart the object containing the graph information
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
		this.yAxisOffset = calculateYAxisOffset();
		this.xAxisOffset = calculateXAxisOffset();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.translate(getInsets().left, getInsets().top);
		
		// grid
		drawXGrid(g, Color.GRAY);
		drawYGrid(g, Color.GRAY);
		
		// axis
		drawXAxis(g, Color.WHITE);
		drawYAxis(g, Color.WHITE);
		
		drawXAxisMarks(g, Color.WHITE, AXIS_MARK_FONT);
		drawYAxisMarks(g, Color.WHITE, AXIS_MARK_FONT);
		
		// title
		drawXAxisTitle(g, Color.WHITE, AXIS_TITLE_FONT);
		drawYAxisTitle(g, Color.WHITE, AXIS_TITLE_FONT);
		
		drawColumnRectangles(g, COLUMN_COLOR);
	}
	
	//-----------------------------------------------------------
	//						COLUMNS
	//-----------------------------------------------------------

	/**
	 * Draws the columns based on the data stored in the {@code BarChart}
	 * instance provided in the constructor.
	 *
	 * @param g the graphics object used to draw rectangles
	 * @param color the color of the rectangles
	 */
	private void drawColumnRectangles(Graphics g, Color color) {
		g.setColor(color);
		
		int lineToDivideLengthX = getWidth() - yAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int nSectionsX = chart.getValues().size();
		double sectionLengthX = (double) lineToDivideLengthX / nSectionsX;
		
		int lineSegmentY = getHeight() - xAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int nSectionsY = (chart.getMaxY() - chart.getMinY()) / chart.getInterval();
		double sectionLengthY = (double) lineSegmentY / nSectionsY;
		
		int width = (int)(sectionLengthX - columnWidthReduction * 2);
		
		Iterator<XYValue> iter = chart.getValues().iterator();
		int index = 0;
		while(iter.hasNext()) {
			XYValue value = iter.next();
			int x = (int)(sectionLengthX * index) + yAxisOffset + columnWidthReduction;
			
			double YSectionsTaken = (double) value.y / chart.getInterval();
			int height = (int) (YSectionsTaken * sectionLengthY);
			int y = getHeight() - xAxisOffset - height;
			
			
			g.fillRect(x, y, width, height);
			index++;
		}
	}
	
	//-----------------------------------------------------------
	//						AXIS MARKS
	//-----------------------------------------------------------
	
	/**
	 * Draws the x-axis marks.
	 *
	 * @param g the graphics object used for drawing
	 * @param color the color of the marks
	 * @param font the font of the marks
	 */
	private void drawXAxisMarks(Graphics g, Color color, Font font) {
		g.setColor(color);
		g.setFont(font);
		
		int lineSegmentLength = getWidth() - yAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int nSections = chart.getValues().size();
		double sectionLength = (double) lineSegmentLength / nSections;
		
		FontMetrics metrics = g.getFontMetrics();
		
		int y1 = getHeight() - xAxisOffset;
		int y2 = y1 + markLength;
		
		double currentX = yAxisOffset;
		
		Iterator<XYValue> iter = chart.getValues().iterator();
		while(iter.hasNext()) {
			g.drawLine((int)currentX, y1, (int)currentX, y2);
			
			XYValue value = iter.next();
			
			String text = value.x + "";
			double textX = currentX + sectionLength/2 - metrics.stringWidth(text)/2;
			g.drawString(text, (int)textX, y1 + xTextOffset);
			
			currentX += sectionLength;
		}
		
		// The last mark just before the x-axis arrow.
		g.drawLine((int)currentX, y1, (int)currentX, y2);
	}

	/**
	 * Draws the y-axis marks.
	 *
	 * @param g the graphics object used for drawing
	 * @param color the color of the marks
	 * @param font the font of the marks
	 */
	private void drawYAxisMarks(Graphics g, Color color, Font font) {
		g.setColor(color);
		g.setFont(font);
		
		int lineToDivideLength = getHeight() - xAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int nSections = (chart.getMaxY() - chart.getMinY()) / chart.getInterval();
		double sectionLength = (double) lineToDivideLength / nSections;
		
		FontMetrics metrics = g.getFontMetrics();
		
		int x1 = yAxisOffset;
		int x2 = x1 - markLength;
		
		double currentY = getHeight() - xAxisOffset;
		int currentValue = chart.getMinY();
		for(int i = 0; i <= nSections; i++) {
			int y = (int) currentY;
			g.drawLine(x1, y, x2, y);
			
			g.drawString(currentValue + "", (x1 - yTextOffset) - metrics.stringWidth(currentValue + ""), 
											y + metrics.getHeight() / 3);
			
			currentY -= sectionLength;
			currentValue += chart.getInterval();
		}
	}
	
	//-----------------------------------------------------------
	//							GRID
	//-----------------------------------------------------------
	
	/**
	 * Draws the x component of the grid.
	 *
	 * @param g the graphics object used for drawing
	 * @param gridColor the color of the grid
	 */
	private void drawXGrid(Graphics g, Color gridColor) {
		g.setColor(gridColor);
		
		int lineSegmentLength = getWidth() - yAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int nSections = chart.getValues().size();
		double sectionLength = (double) lineSegmentLength / nSections;
		
		int y1 = getHeight() - xAxisOffset;
		int y2 = y1 + markLength;
		
		double currentX = yAxisOffset;
		
		for(int i = 0; i <= nSections; i++) {
			g.drawLine((int)currentX, y1, (int)currentX, 0);
			currentX += sectionLength;
		}
		
		// The last mark just before the x-axis arrow.
		g.drawLine((int)currentX, y1, (int)currentX, y2);
	}
	
	/**
	 * Draws the y component of the grid.
	 *
	 * @param g the graphics object used for drawing
	 * @param gridColor the color of the grid
	 */
	private void drawYGrid(Graphics g, Color gridColor) {
		g.setColor(gridColor);
		
		int lineToDivideLength = getHeight() - xAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int nSections = (chart.getMaxY() - chart.getMinY()) / chart.getInterval();
		double sectionLength = (double) lineToDivideLength / nSections;
		
		int x1 = yAxisOffset;
		double currentY = getHeight() - xAxisOffset;

		for(int i = 0; i <= nSections; i++) {
			g.drawLine(x1, (int)currentY, getWidth(), (int)currentY);
			currentY -= sectionLength;
		}
	}
	
	//-----------------------------------------------------------
	//							AXIS
	//-----------------------------------------------------------
	
	/**
	 * Draws the x-axis with the arrow at the end of it.
	 *
	 * @param g the graphics object used to draw lines
	 * @param color the color of the x-axis
	 */
	private void drawXAxis(Graphics g, Color color) {
		g.setColor(color);
		
		int x1 = yAxisOffset;
		int x2 = getWidth();
		int y = getHeight() - xAxisOffset;
		g.drawLine(x1, y, x2, y);
		
		// Drawing the arrow
		g.drawLine(x2, y, x2-arrowHeight, y+arrowWidth/2);
		g.drawLine(x2, y, x2-arrowHeight, y-arrowWidth/2);
	}
	
	/**
	 * Draws the y-axis with the arrow at the end of it.
	 *
	 * @param g the graphics object used to draw lines
	 * @param color the color of the y-axis
	 */
	private void drawYAxis(Graphics g, Color color) {
		g.setColor(color);
		
		int x = yAxisOffset;
		int y1 = 0;
		int y2 = getHeight() - xAxisOffset;
		g.drawLine(x, y1, x, y2);
		
		// Drawing the arrow
		g.drawLine(x, y1, x+arrowWidth/2, y1+arrowHeight);
		g.drawLine(x, y1, x-arrowWidth/2, y1+arrowHeight);
	}
	
	//-----------------------------------------------------------
	//						AXIS TITLES
	//-----------------------------------------------------------
	
	/**
	 * Draws the x-axis title.
	 *
	 * @param g the graphics object used to draw the title
	 * @param color the color of the title
	 * @param font the font of the title
	 */
	private void drawXAxisTitle(Graphics g, Color color, Font font) {
		g.setColor(color);
		g.setFont(font);
		
		String title = chart.getTitleX();
		int lineSegmentLength = getWidth() - yAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int titleLength = g.getFontMetrics().stringWidth(title);
		
		int x = yAxisOffset + (lineSegmentLength - titleLength) / 2;
		int y = getHeight() - xAxisTitleOffset;
		
		g.drawString(title, x, y);
	}
	
	/**
	 * Draws the y-axis title.
	 *
	 * @param g the graphics object used to draw the title
	 * @param color the color of the title
	 * @param font the font of the title
	 */
	private void drawYAxisTitle(Graphics g, Color color, Font font) {
		g.setColor(color);
		g.setFont(font);

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saved = g2d.getTransform();
		
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2d.setTransform(at);
		
		String title = chart.getTitleY();
		int lineSegmentLength = getHeight() - xAxisOffset - arrowHeight - spaceBetweenArrowAndLastMark;
		int titleLength = g.getFontMetrics().stringWidth(title);
		
		int x = -(yAxisOffset + (lineSegmentLength + titleLength) / 2);
		int y = yAxisTitleOffset;
		
		g2d.drawString(title, x, y);

		g2d.setTransform(saved);
	}
	
	//-----------------------------------------------------------
	//						HELPER METHODS
	//-----------------------------------------------------------
	
	/**
	 * Calculates the {@link #xAxisOffset} value based on the font sizes
	 * of the x-axis title and x-axis marks.
	 *
	 * @return the calculated {@link #xAxisOffset} value
	 */
	private int calculateXAxisOffset() {
		int offset = 0;
		
		// Height of the x-axis marks
		setFont(AXIS_MARK_FONT);
		offset += getFontMetrics(getFont()).getHeight();
		
		// Height of the x-axis title
		setFont(AXIS_TITLE_FONT);
		offset += getFontMetrics(getFont()).getHeight();
		
		return offset + 20;
	}
	
	/**
	 * Calculates the {@link #yAxisOffset} value based on the given y-axis numbers.
	 *
	 * @return the calculated {@link #yAxisOffset} value
	 */
	private int calculateYAxisOffset() {
		setFont(AXIS_MARK_FONT);
		return getFontMetrics(getFont()).stringWidth(String.valueOf(chart.getMaxY())) + 50;
	}
}
