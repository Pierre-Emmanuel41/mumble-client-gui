package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.scene.text.Font;

public class FontChangePostEvent extends GuiConfigurationEvent<Font> {

	/**
	 * Creates an event thrown when the font of the given configuration has changed.
	 * 
	 * @param configuration The configuration the font has changed.
	 * @param oldValue      The old font.
	 * @param newValue      The current font.
	 */
	public FontChangePostEvent(IGuiConfiguration configuration, Font oldValue, Font newValue) {
		super(configuration, oldValue, newValue);
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add(String.format("oldFont=[name=%s, size=%s]", getOldValue().getName(), getOldValue().getSize()));
		joiner.add(String.format("newFont=[name=%s, size=%s]", getNewValue().getName(), getNewValue().getSize()));
		return String.format("%s_%s", getName(), joiner);
	}
}
