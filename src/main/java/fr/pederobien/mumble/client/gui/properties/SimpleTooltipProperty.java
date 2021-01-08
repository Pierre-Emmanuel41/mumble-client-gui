package fr.pederobien.mumble.client.gui.properties;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;

public class SimpleTooltipProperty extends SimpleObjectProperty<Tooltip> {

	public SimpleTooltipProperty(GuiConfiguration guiConfiguration, IMessageCode code, Object... args) {
		super(new Tooltip());
		getValue().fontProperty().bind(new SimpleFontProperty(guiConfiguration));
		getValue().textProperty().bind(new SimpleLanguageProperty(guiConfiguration, code, args));
	}
}
