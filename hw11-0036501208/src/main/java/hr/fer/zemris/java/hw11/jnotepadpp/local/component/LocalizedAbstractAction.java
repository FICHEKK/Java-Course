package hr.fer.zemris.java.hw11.jnotepadpp.local.component;

import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Models the localized version of the abstract action object.
 * This version has an addition of supporting multi-language.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public abstract class LocalizedAbstractAction extends AbstractAction {
	
	/**
	 * Constructs a new localized version of the abstract action,
	 * meaning that this version supports multi-language.
	 *
	 * @param key the name key
	 * @param translator the translator
	 */
	public LocalizedAbstractAction(String key, ILocalizationProvider translator) {
		putValue(Action.NAME, translator.getString(key));
		
		translator.addLocalizationListener(() -> {
			putValue(Action.NAME, translator.getString(key));
		});
	}
}
