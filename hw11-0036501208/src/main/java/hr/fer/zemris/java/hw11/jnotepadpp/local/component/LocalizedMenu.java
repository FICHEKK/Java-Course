package hr.fer.zemris.java.hw11.jnotepadpp.local.component;

import javax.swing.JMenu;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Models the localized version of the {@code JMenu}.
 * This version has an addition of supporting multi-language.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class LocalizedMenu extends JMenu {
	
	/**
	 * Constructs a new localized menu.
	 *
	 * @param key the key
	 * @param translator the translation providing object
	 */
	public LocalizedMenu(String key, ILocalizationProvider translator) {
		setText(translator.getString(key));
		
		translator.addLocalizationListener(() -> {
			setText(translator.getString(key));
		});
	}
}
