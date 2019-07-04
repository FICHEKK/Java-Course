package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * A simple tool for drawing straight lines from
 * one point to another.
 *
 * @author Filip Nemec
 */
public class LineTool implements Tool {
	
	/** Color provider used to retrieve the line color. */
	private IColorProvider colorProvider;
	
	/** The storage of geometrical objects. */
	private DrawingModel model;
	
	/** The starting point. */
	private Point start;
	
	/** The ending point. */
	private Point end;
	
	/**
	 * Constructs a new line tool.
	 *
	 * @param colorProvider the color provider
	 * @param model the data model
	 */
	public LineTool(IColorProvider colorProvider, DrawingModel model) {
		this.colorProvider = colorProvider;
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(start == null) {
			start = e.getPoint();
			end = e.getPoint();
			
		} else {
			model.add(new Line(start, end, colorProvider.getCurrentColor()));
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
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}
	
	//----------------------------------------------------------------
	//						UNUSED METHODS
	//----------------------------------------------------------------
	
	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing.
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// Do nothing.
	}
}