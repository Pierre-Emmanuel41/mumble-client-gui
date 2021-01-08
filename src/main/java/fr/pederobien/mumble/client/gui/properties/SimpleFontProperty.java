package fr.pederobien.mumble.client.gui.properties;

import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.mumble.client.gui.properties.InternalProperty.Action;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;

public class SimpleFontProperty extends SimpleObjectProperty<Font> {

	/**
	 * Create a font property based on the given gui configuration. If the font parameter in the gui configuration changes then this
	 * font property is automatically updated.
	 * 
	 * @param guiConfiguration The gui configuration that updates this property.
	 */
	public SimpleFontProperty(GuiConfiguration guiConfiguration) {
		super(guiConfiguration.getFont());
		new InternalProperty(guiConfiguration).registerAction(Action.FONT_CHANGED, e -> setValue((Font) e.getNewValue()));
	}
}
