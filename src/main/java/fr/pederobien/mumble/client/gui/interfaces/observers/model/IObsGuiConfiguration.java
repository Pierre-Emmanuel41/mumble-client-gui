package fr.pederobien.mumble.client.gui.interfaces.observers.model;

import java.util.Locale;

import javafx.scene.text.Font;

public interface IObsGuiConfiguration {

	/**
	 * Notify this observer the configuration locale has changed.
	 * 
	 * @param oldLocale The old configuration locale.
	 * @param newLocale The new configuration locale.
	 */
	void onLanguageChanged(Locale oldLocale, Locale newLocale);

	/**
	 * Notify this observer the configuration font has changed.
	 * 
	 * @param oldFont The old configuration font.
	 * @param newFont The new configuration font.
	 */
	void onFontChanged(Font oldFont, Font newFont);
}
