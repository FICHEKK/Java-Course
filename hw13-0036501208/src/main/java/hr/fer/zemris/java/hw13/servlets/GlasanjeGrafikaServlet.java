package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.List;

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

import hr.fer.zemris.java.hw13.servlets.GlasanjeServlet.Band;

/**
 * A servlet that displays the voting results in a
 * form of a pie-chart.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeGrafikaServlet extends HttpServlet {
	
	/** The chart width in pixels. */
	private static final int CHART_WIDTH = 400;
	
	/** The chart height in pixels. */
	private static final int CHART_HEIGHT = 400;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		PieDataset dataset = createDataset((List<Band>) req.getSession().getAttribute("bandsWithVotes"));
        JFreeChart chart = createChart(dataset, "Pie-chart of voting results");
        
        ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, CHART_WIDTH, CHART_HEIGHT);
	}

    /**
     * Fill the dataset with the pie chart values and returns
     * the filled dataset.
     * 
     * @return the dataset filled with values
     */
    private PieDataset createDataset(List<Band> bands) {
        DefaultPieDataset result = new DefaultPieDataset();
        
        for(Band band : bands) {
        	if(band.getVotes() > 0) {
        		result.setValue(band.name, band.getVotes());
        	}
        }

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
