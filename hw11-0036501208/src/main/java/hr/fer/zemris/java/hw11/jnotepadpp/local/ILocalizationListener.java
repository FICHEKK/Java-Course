package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models objects that are listening to the
 * change in localization settings.
 *
 * @author Filip Nemec
 */
public interface ILocalizationListener {
	
	/**
	 * Informs the observer that the localization
	 * has changed.
	 */
	void localizationChanged();
}
