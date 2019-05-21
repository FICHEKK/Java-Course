package hr.fer.zemris.java.hw11.jnotepadpp.local.component;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Models the localized version of the {@code JLabel}.
 * This version has an addition of supporting multi-language.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class LocalizedLabel extends JLabel {
	
	/**
	 * Constructs a new localized label-
	 *
	 * @param key the key
	 * @param translator the translation providing object
	 */
	public LocalizedLabel(String key, ILocalizationProvider translator) {
		setText(translator.getString(key));
		
		translator.addLocalizationListener(() -> {
			setText(translator.getString(key));
		});
	}
}
