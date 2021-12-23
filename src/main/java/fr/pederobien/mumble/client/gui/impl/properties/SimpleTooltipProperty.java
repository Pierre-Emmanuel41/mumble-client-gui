package fr.pederobien.mumble.client.gui.impl.properties;

import fr.pederobien.mumble.client.gui.interfaces.ICode;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;

public class SimpleTooltipProperty extends SimpleObjectProperty<Tooltip> {
	private SimpleLanguageProperty textProperty;

	/**
	 * Create a tooltip property based on the given gui configuration. if the font or local parameter in the gui configuration changes
	 * then this property is also updated.
	 * 
	 * @param guiConfiguration The gui configuration that updates this property.
	 * @param code             The code associated to the message to display.
	 * @param args             The message arguments if the message needs arguments.
	 */
	public SimpleTooltipProperty(IGuiConfiguration guiConfiguration, ICode code, Object... args) {
		super(new Tooltip());
		textProperty = new SimpleLanguageProperty(guiConfiguration, code, args);
		getValue().fontProperty().bind(new SimpleFontProperty(guiConfiguration));
		getValue().textProperty().bind(textProperty);
	}

	/**
	 * Set the code associated to this tooltip. This will automatically change the displayed text.
	 * 
	 * @param code The new code associated to the message to display.
	 * @param args The new arguments array associated to the code.
	 */
	public void setMessageCode(ICode code, Object... args) {
		textProperty.setCode(code, args);
	}
}