package fr.pederobien.mumble.client.gui.properties;

import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.mumble.client.gui.properties.InternalProperty.Action;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;

public class SimpleFontProperty extends SimpleObjectProperty<Font> {

	public SimpleFontProperty(GuiConfiguration guiConfiguration) {
		super(guiConfiguration.getFont());
		new InternalProperty(guiConfiguration).registerAction(Action.FONT_CHANGED, e -> setValue((Font) e.getNewValue()));
	}
}
