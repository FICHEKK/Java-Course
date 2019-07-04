package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

/**
 * Models the canvas that shows the visual representation of the
 * {@code DrawingModel} data.
 *
 * @author Filip Nemec
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {
	
	/** Used for serialization. */
	private static final long serialVersionUID = -385586970709447261L;

	/** The supplier of the current tool. */
	private Supplier<Tool> supplier;
	
	/** The drawing model - layer used for storage of geometrical objects. */
	private DrawingModel drawingModel;
	
	/**
	 * Constructs a new drawing canvas.
	 *
	 * @param supplier the tool supplier
	 * @param drawingModel the data model of this canvas
	 */
	public JDrawingCanvas(Supplier<Tool> supplier, DrawingModel drawingModel) {
		this.supplier = supplier;
		this.drawingModel = drawingModel;
		this.drawingModel.addDrawingModelListener(this);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				supplier.get().mousePressed(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				supplier.get().mouseReleased(e);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				supplier.get().mouseClicked(e);
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				supplier.get().mouseMoved(e);
				repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				supplier.get().mouseDragged(e);
				repaint();
			}
		});
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);
		
		for(int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(painter);
		}
		
		supplier.get().paint((Graphics2D) g);
		
		//----------------------------------------------------
		// 				Drawing the bounding box.
		//----------------------------------------------------
		
//		var BBcalculator = new GeometricalObjectBBCalculator();
//    	
//		for(int i = 0; i < drawingModel.getSize(); i++) {
//			drawingModel.getObject(i).accept(BBcalculator);
//		}
//		
//		// The bounding box
//		Rectangle bb = BBcalculator.getBoundingBox();
//		
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.GREEN);
//		
//		int x      = (int) bb.getX();
//		int y      = (int) bb.getY();
//		int width  = (int) bb.getWidth();
//		int height = (int) bb.getHeight();
//		
//		g2d.drawLine(x, y, x + width, y);
//		g2d.drawLine(x, y, x, y + height);
//		g2d.drawLine(x + width, y + height, x + width, y);
//		g2d.drawLine(x + width, y + height, x, y + height);
	}
}
