package fr.pederobien.mumble.client.gui.properties;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;

public class SimpleTooltipProperty extends SimpleObjectProperty<Tooltip> {

	/**
	 * Create a tooltip property based on the given gui configuration. if the font or local parameter in the gui configuration changes
	 * then this property is also updated.
	 * 
	 * @param guiConfiguration The gui configuration that updates this property.
	 * @param code             The code associated to the message to display.
	 * @param args             The message arguments if the message needs arguments.
	 */
	public SimpleTooltipProperty(GuiConfiguration guiConfiguration, IMessageCode code, Object... args) {
		super(new Tooltip());
		getValue().fontProperty().bind(new SimpleFontProperty(guiConfiguration));
		getValue().textProperty().bind(new SimpleLanguageProperty(guiConfiguration, code, args));
	}
}
