package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A proxy localization provider.
 *
 * @author Filip Nemec
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	
	/** Flag that indicates if this bridge connected. */
	private boolean connected;
	
	/** The reference to the provider. */
	private ILocalizationProvider provider;
	
	/** A simple listener that will notify all other listeners. */
	private final ILocalizationListener notifier = () -> fire();
	
	/**
	 * Constructs a new proxy for the {@code ILocalizationProvider}.
	 *
	 * @param provider the provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * Connects this bridge to the provider. It does so by adding
	 * a "notifier" listener to the provider. A "notifier" will
	 * notify all the listeners connected to this bridge.
	 */
	public void connect() {
		if(connected) return;
		
		provider.addLocalizationListener(notifier);
		connected = true;
	}
	
	/**
	 * Disconnects this bridge from the provider. Provider's
	 * notifications will no longer reach the listeners of this
	 * bridge.
	 */
	public void disconnect() {
		provider.removeLocalizationListener(notifier);
		connected = false;
	}
	
	@Override
	public String getString(String key) {
		return provider.getString(key);
	}
}
