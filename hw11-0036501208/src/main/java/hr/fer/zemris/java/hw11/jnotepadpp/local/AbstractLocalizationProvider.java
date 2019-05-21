package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract model of the localization provider.
 *
 * @author Filip Nemec
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	protected List<ILocalizationListener> listeners;

	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		if(listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public abstract String getString(String key);
	
	/**
	 * Informs all of the listeners that the localization
	 * settings have changed.
	 */
	public void fire() {
		if(listeners == null) return;
		listeners.forEach(ILocalizationListener::localizationChanged);
	}
}
