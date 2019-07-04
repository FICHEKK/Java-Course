package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;

/**
 * A simple tool for creating filled circles.
 *
 * @author Filip Nemec
 */
public class FilledCircleTool implements Tool {

	/** Foreground color provider used to retrieve the circle's outline color. */
	private IColorProvider fgProvider;
	
	/** Background color provider used to retrieve the circle's area color. */
	private IColorProvider bgProvider;
	
	/** The storage of geometrical objects. */
	private DrawingModel model;
	
	/** The starting point. */
	private Point start;
	
	/** The ending point. Difference between end and start defines the radius */
	private Point end;
	
	/**
	 * Constructs a new tool used to draw filled circles.
	 *
	 * @param fgProvider the foreground color provider
	 * @param bgProvider the background color provider
	 * @param model the data model
	 */
	public FilledCircleTool(IColorProvider fgProvider, IColorProvider bgProvider, DrawingModel model) {
		this.fgProvider = fgProvider;
		this.bgProvider = bgProvider;
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(start == null) {
			start = e.getPoint();
			end = e.getPoint();
			
		} else {
			int radius = (int) start.distance(end);
			model.add(new FilledCircle(start, radius, fgProvider.getCurrentColor(), bgProvider.getCurrentColor()));
			start = null;
			end = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		end = e.getPoint();
	}

	@Override
	public void paint(Graphics2D g2d) {
		if(start == null || end == null) return;
		
		int radius = (int) start.distance(end);
		
		g2d.setColor(bgProvider.getCurrentColor());
		g2d.fillOval(start.x - radius, start.y - radius, radius * 2, radius * 2);
		
		g2d.setColor(fgProvider.getCurrentColor());
		g2d.drawOval(start.x - radius, start.y - radius, radius * 2, radius * 2);
	}
	
	//----------------------------------------------------------------
	//						UNUSED METHODS
	//----------------------------------------------------------------
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
}
