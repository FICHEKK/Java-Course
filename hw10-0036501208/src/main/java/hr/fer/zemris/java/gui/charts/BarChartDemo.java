package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * A simple frame that shows the usage and display of the
 * bar chart.
 *
 * @author Filip Nemec
 */
public class BarChartDemo extends JFrame {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new frame that will display the given
	 * bar chart
	 *
	 * @param barChart the reference to the bar chart descriptor
	 * @param filePath the path of the file the bar chart was
	 * 				   constructed from
	 * @param chart background color
	 */
	public BarChartDemo(BarChart barChart, String filePath) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setTitle("Bar chart");
		
		Container pane = getContentPane();
		pane.add(new BarChartComponent(barChart));
		
		JLabel label = new JLabel(filePath);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(JLabel.RIGHT);
		this.add(label, BorderLayout.NORTH);
		
		getContentPane().setBackground(Color.DARK_GRAY);
		
		// Middle of the screen placement
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
					(screen.height - getSize().height) / 2);
	}
	
	/**
	 * Program that needs a single argument: path to the file containing
	 * the format description of the bar chart that will be displayed.
	 * <p>
	 * The format description is as follows:
	 * <br> The title of the x-axis
	 * <br> The title of the y-axis
	 * <br> The sequence of the values of format "x1,y1 x2,y2 x3,x3..."
	 * <br> The minimum y value
	 * <br> The maximum y value
	 * <br> The interval - difference between two y-axis values
	 *
	 * @param args path to the file containing the formal bar chart description
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Please provide file path to a file with the chart definition.");
			return;
		}
		
		BarChart chart;
		String filePath = args[0];
		
		try(BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
			String titleX    = Objects.requireNonNull(br.readLine(), "Expected x-axis title, was null.");
			String titleY    = Objects.requireNonNull(br.readLine(), "Expected y-axis title, was null.");
			String _values   = Objects.requireNonNull(br.readLine(), "Expected x,y values, was null.");
			String _minY     = Objects.requireNonNull(br.readLine(), "Expected minimum y value, was null.");
			String _maxY     = Objects.requireNonNull(br.readLine(), "Expected maximum y value, was null.");
			String _interval = Objects.requireNonNull(br.readLine(), "Expected interval value, was null.");
			
			List<XYValue> values = convertToXYValues(_values);
			int minY = Integer.parseInt(_minY);
			int maxY = Integer.parseInt(_maxY);
			int interval = Integer.parseInt(_interval);
			
			chart = new BarChart(values, titleX, titleY, minY, maxY, interval);
			
		} catch(IOException e) {
			System.err.println("File reading error occured. Closing...");
			return;
			
		} catch(NumberFormatException e) {
			System.err.println("Error during the number formatting. Closing...");
			return;
		}
		
		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(chart, filePath).setVisible(true);
		});
	}
	
	/**
	 * Converts a {@code String} to a list of zero or more {@code XYValue}
	 * objects, if the given input is properly formatted. The proper format
	 * is as follows: "x1,y1 x2,y2 x3,y2...", where x1 and y1 are the values
	 * of the first {@code XYValue} instance, x2 and y2 of the second {@code XYValue}
	 * instance and so on...
	 *
	 * @param values the {@code String} input containing the x,y values
	 * @return a list of converted {@code XYValue} instances
	 */
	private static List<XYValue> convertToXYValues(String values) {
		List<XYValue> list = new LinkedList<>();
		
		String[] individualPairs = values.split("\\s+");
		
		for(String pair : individualPairs) {
			String[] individualValues = pair.split(",");
			
			if(individualValues.length != 2)
				throw new IllegalArgumentException("Invalid input '" + pair + "'.");

			int x = Integer.parseInt(individualValues[0]);
			int y = Integer.parseInt(individualValues[1]);
			
			list.add(new XYValue(x, y));
		}
		
		return list;
	}
}