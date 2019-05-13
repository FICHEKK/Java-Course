package hr.fer.zemris.java.gui.calc;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

/**
 * Models the display used by the {@link Calculator}.
 *
 * @author Filip Nemec
 */
public class Display extends JLabel implements CalcValueListener {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new display with the given initial
	 * text and the specified horizontal alignment.
	 *
	 * @param text the initial text
	 * @param horizontalAlignment the horizontal alignment
	 */
	public Display(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	@Override
	public void valueChanged(CalcModel model) {
		SwingUtilities.invokeLater(() -> setText(model.toString()));
	}
}