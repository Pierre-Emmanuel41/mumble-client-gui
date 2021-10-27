package fr.pederobien.mumble.client.gui.event;

import java.util.Locale;

import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;

public class LocaleChangePostEvent extends GuiConfigurationEvent<Locale> {

	/**
	 * Creates an event thrown when the locale of the given configuration has changed.
	 * 
	 * @param configuration The configuration whose the locale has changed.
	 * @param oldValue      The old locale.
	 * @param newValue      The current locale.
	 */
	public LocaleChangePostEvent(IGuiConfiguration configuration, Locale oldValue, Locale newValue) {
		super(configuration, oldValue, newValue);
	}
}
