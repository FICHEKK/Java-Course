package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;

/**
 * A simple tool for drawing empty circles.
 *
 * @author Filip Nemec
 */
public class CircleTool implements Tool {

	/** Color provider used to retrieve the line color. */
	private IColorProvider colorProvider;
	
	/** The storage of geometrical objects. */
	private DrawingModel model;
	
	/** The starting point. */
	private Point start;
	
	/** The ending point. Difference between end and start defines the radius */
	private Point end;
	
	/**
	 * Constructs a new line tool.
	 *
	 * @param colorProvider the color provider
	 * @param model the data model
	 */
	public CircleTool(IColorProvider colorProvider, DrawingModel model) {
		this.colorProvider = colorProvider;
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(start == null) {
			start = e.getPoint();
			end = e.getPoint();
			
		} else {
			int radius = (int) start.distance(end);
			model.add(new Circle(start, radius, colorProvider.getCurrentColor()));
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
		
		g2d.setColor(colorProvider.getCurrentColor());
		int radius = (int) start.distance(end);
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
