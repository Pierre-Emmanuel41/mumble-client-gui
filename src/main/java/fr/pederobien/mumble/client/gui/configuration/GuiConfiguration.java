package fr.pederobien.mumble.client.gui.configuration;

import java.util.Locale;
import java.util.function.Consumer;

import fr.pederobien.mumble.client.gui.interfaces.IObsGuiConfiguration;
import fr.pederobien.persistence.interfaces.IUnmodifiableNominable;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;

public class GuiConfiguration implements IUnmodifiableNominable, IObservable<IObsGuiConfiguration> {
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	private Observable<IObsGuiConfiguration> observers;
	private Locale locale;

	public GuiConfiguration() {
		observers = new Observable<IObsGuiConfiguration>();
	}

	@Override
	public String getName() {
		return "GuiConfiguration";
	}

	@Override
	public void addObserver(IObsGuiConfiguration obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsGuiConfiguration obs) {
		observers.removeObserver(obs);
	}

	/**
	 * @return The gui locale, this correspond to the language in which user messages are displayed.
	 */
	public Locale getLocale() {
		return locale == null ? DEFAULT_LOCALE : locale;
	}

	/**
	 * Set the gui locale. This correspond to the language in which user messages are displayed. Changing this locale will
	 * automatically change all user message.
	 * 
	 * @param locale The new gui locale.
	 */
	public void setLocale(Locale locale) {
		if (this.locale != null && this.locale.equals(locale))
			return;

		Locale oldLocale = this.locale;
		this.locale = locale;
		notifyObservers(obs -> obs.onLanguageChanged(oldLocale, locale));
	}

	private void notifyObservers(Consumer<IObsGuiConfiguration> consumer) {
		observers.notifyObservers(consumer);
	}
}
