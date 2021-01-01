package fr.pederobien.mumble.client.gui.interfaces;

import java.util.Locale;

public interface IObsGuiConfiguration {

	/**
	 * Notify this observer the configuration locale has changed.
	 * 
	 * @param oldLocale The old configuration locale.
	 * @param newLocale The new configuration locale.
	 */
	void onLanguageChanged(Locale oldLocale, Locale newLocale);
}
