package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A singleton class that globally (across the whole application)
 * provides the localization settings.
 *
 * @author Filip Nemec
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/** The reference to the singleton instance of the {@code LocalizationProvider}. */
	private static final LocalizationProvider INSTANCE = new LocalizationProvider();
	
	/** The location of language translation files. */
	private static final String TRANSLATION_PACKAGE = "hr.fer.zemris.java.hw11.jnotepadpp.local.prijevodi";
	
	/** The default localization settings language. */
	private static final String DEFAULT_LANGUAGE = "en";
	
	/** The currently set language. */
	private String language;
	
	/** The reference to the bundle. */
	private ResourceBundle bundle;

	/**
	 * To ensure only 1 instance, constructor is private.
	 */
	private LocalizationProvider() {
		setLanguage(DEFAULT_LANGUAGE);
	}
	
	/**
	 * Returns the singleton instance of the {@code LocalizationProvider}.
	 *
	 * @return the singleton instance of the {@code LocalizationProvider}
	 */
	public static LocalizationProvider getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Sets the new language and informs all of the listeners of
	 * the language change.
	 *
	 * @param language the new language
	 * @throws NullPointerException if the provided language is {@code null}
	 */
	public void setLanguage(String language) {
		Objects.requireNonNull(language, "Given language cannot be null.");
		
		// We don't want to call bundle creation if the language
		// has not changed. "getBundle" is an expensive operation.
		if(language.equals(this.language)) return;
		
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		this.bundle = ResourceBundle.getBundle(TRANSLATION_PACKAGE, locale);
		fire();
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
}
