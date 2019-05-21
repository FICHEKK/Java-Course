package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models objects that can provide localization.
 *
 * @author Filip Nemec
 */
public interface ILocalizationProvider {
	
	/**
	 * Subscribes the given listener to this provider.
	 *
	 * @param l the listener to be subscribed
	 */
	void addLocalizationListener(ILocalizationListener l);
	
	/**
	 * Removes the given listener from this provider.
	 *
	 * @param l the listener to be removed
	 */
	void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * Returns the value associated with the given key.
	 * 
	 * @param key the key
	 * @return the value associated with the given key
	 */
	String getString(String key);
}
