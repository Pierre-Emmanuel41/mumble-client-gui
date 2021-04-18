package fr.pederobien.mumble.client.gui.impl.properties;

import fr.pederobien.mumble.client.gui.impl.properties.InternalProperty.Action;
import fr.pederobien.mumble.client.gui.interfaces.observers.IGuiConfiguration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;

public class SimpleFontProperty extends SimpleObjectProperty<Font> {

	/**
	 * Create a font property based on the given gui configuration. If the font parameter in the gui configuration changes then this
	 * font property is automatically updated.
	 * 
	 * @param guiConfiguration The gui configuration that updates this property.
	 */
	public SimpleFontProperty(IGuiConfiguration guiConfiguration) {
		super(guiConfiguration.getFont());
		new InternalProperty(guiConfiguration).registerAction(Action.FONT_CHANGED, e -> setValue((Font) e.getNewValue()));
	}
}
