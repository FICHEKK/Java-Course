package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Sets the web-page background color to the color
 * that was provided by the "color" parameter.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class ReportServlet extends HttpServlet {
	
	/** The chart width in pixels. */
	private static final int CHART_WIDTH = 800;
	
	/** The chart height in pixels. */
	private static final int CHART_HEIGHT = 600;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
        JFreeChart chart = createChart(createDataset(), "Operating system usage - April 2019");
        
        ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, CHART_WIDTH, CHART_HEIGHT);
	}

    /**
     * Fill the dataset with the pie chart values and returns
     * the filled dataset.
     * 
     * @return the dataset filled with values
     */
    private PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        
        result.setValue("Windows 10", 52.9);
        result.setValue("Windows 8", 4.7);
        result.setValue("Windows 7", 16.3);
        result.setValue("Linux", 5.7);
        result.setValue("Mac", 10.6);
        result.setValue("Other", 9.8);

        return result;
    }

    /**
     * Creates and returns the created chart.
     *
     * @param dataset the chart data-set
     * @param title the chart title
     * @return the generated chart
     */
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(
            title,                  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(0);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}
