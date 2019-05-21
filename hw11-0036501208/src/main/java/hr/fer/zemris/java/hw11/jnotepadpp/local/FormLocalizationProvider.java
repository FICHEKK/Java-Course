package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A bridge provider that is designed to work with {@code JFrame}
 * objects.
 *
 * @author Filip Nemec
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructs a new {@code FormLocalizationProvider}.
	 *
	 * @param provider the localization provider
	 * @param frame the frame
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			};
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}
}