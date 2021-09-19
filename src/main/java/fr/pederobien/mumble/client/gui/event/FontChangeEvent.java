package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.scene.text.Font;

public class FontChangeEvent extends GuiConfigurationEvent<Font> {

	/**
	 * Creates an event thrown when the font of the given configuration has changed.
	 * 
	 * @param configuration The configuration the font has changed.
	 * @param oldValue      The old font.
	 * @param newValue      The current font.
	 */
	public FontChangeEvent(IGuiConfiguration configuration, Font oldValue, Font newValue) {
		super(configuration, oldValue, newValue);
	}
}
