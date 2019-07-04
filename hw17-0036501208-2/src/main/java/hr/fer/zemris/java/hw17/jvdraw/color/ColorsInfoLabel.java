package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;
import java.util.Objects;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw17.jvdraw.listeners.ColorChangeListener;

/**
 * A label which displays the selected foreground and background colors.
 *
 * @author Filip Nemec
 */
public class ColorsInfoLabel extends JLabel implements ColorChangeListener {
	
	/** Used for serialization. */
	private static final long serialVersionUID = -8038718082498374704L;
	
	/** The foreground color provider. */
	private IColorProvider fgColorProvider;
	
	/** The background color provider. */
	private IColorProvider bgColorProvider;
	
	/**
	 * Constructs a label which displays the selected foreground and background colors.
	 *
	 * @param fgColorProvider the foreground color provider
	 * @param bgColorProvider the background color provider
	 */
	public ColorsInfoLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = Objects.requireNonNull(fgColorProvider, "Foreground color provider cannot be null.");
		this.bgColorProvider = Objects.requireNonNull(bgColorProvider, "Background color provider cannot be null.");
		
		updateText();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		updateText();
	}

	/**
	 * Updates the text to the valid foreground and background color information.
	 */
	private void updateText() {
		Color fgColor = fgColorProvider.getCurrentColor();
		Color bgColor = bgColorProvider.getCurrentColor();
		
		this.setText("Foreground color: (" + fgColor.getRed() + ", " + fgColor.getGreen() + ", " + fgColor.getBlue() + "), " +
					 "background color: (" + bgColor.getRed() + ", " + bgColor.getGreen() + ", " + bgColor.getBlue() + ").");
	}
}
