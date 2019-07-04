package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.listeners.ColorChangeListener;

/**
 * Models a component used for displaying the color of
 * something. Clicking on this component will prompt
 * the user to pick a new color.
 *
 * @author Filip Nemec
 */
public class JColorArea extends JComponent implements IColorProvider {
	
	/** Used for serialization. */
	private static final long serialVersionUID = -5344668125705708576L;
	
	/** Previous color reference (the color that was before the {@link #selectedColor}. */
	private Color previousColor;

	/** The currently selected color. */
	private Color selectedColor;
	
	/** A constant dimension. */
	private static final Dimension DIMENSION = new Dimension(15, 15);
	
	/** A list of color change listeners. */
	private List<ColorChangeListener> listeners = new LinkedList<>();
	
	//----------------------------------------------------------------
	//						  Constructor
	//----------------------------------------------------------------
	
	/**
	 * Constructs a new color area with the initially selected color.
	 *
	 * @param selectedColor the initially selected color
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = selectedColor;
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Color picked = JColorChooser.showDialog(JColorArea.this, "Pick a new color!", selectedColor, true);
				
				if(picked == null) return;
				
				JColorArea.this.previousColor = selectedColor;
				JColorArea.this.selectedColor = picked;
				repaint();
				notifyListeners();
			}
		});
		
		setMinimumSize(DIMENSION);
		setMaximumSize(DIMENSION);
	}
	
	//----------------------------------------------------------------
	//						JComponent methods
	//----------------------------------------------------------------

	@Override
	public Dimension getPreferredSize() {
		return DIMENSION;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect(0, 0, DIMENSION.width, DIMENSION.height);
	}
	
	//----------------------------------------------------------------
	//						  Color provider
	//----------------------------------------------------------------

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
	
	private void notifyListeners() {
		listeners.forEach(l -> l.newColorSelected(this, previousColor, selectedColor));
	}
}
