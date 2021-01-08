package fr.pederobien.mumble.client.gui.properties;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;

public class PropertyHelper {
	private GuiConfiguration guiConfiguration;

	public PropertyHelper(GuiConfiguration guiConfiguration) {
		this.guiConfiguration = guiConfiguration;
	}

	/**
	 * Create a string property based on the gui configuration associated to this helper. If the local parameter in the gui
	 * configuration changes then this property is automatically updated.
	 * 
	 * @param code The code associated to the message to display.
	 * @param args The message arguments if the message needs arguments.
	 * 
	 * @return the created language property.
	 */
	public SimpleLanguageProperty languageProperty(IMessageCode code, Object... args) {
		return new SimpleLanguageProperty(guiConfiguration, code, args);
	}

	/**
	 * Create a font property based on the gui configuration associated to this helper. If the font parameter in the gui configuration
	 * changes then this font property is automatically updated.
	 * 
	 * @return the created font property.
	 */
	public SimpleFontProperty fontProperty() {
		return new SimpleFontProperty(guiConfiguration);
	}

	/**
	 * Create a tooltip property based on the gui configuration associated to this helper. if the font or local parameter in the gui
	 * configuration changes then this property is also updated.
	 * 
	 * @param code The code associated to the message to display.
	 * @param args The message arguments if the message needs arguments.
	 * 
	 * @return the created tooltip property.
	 */
	public SimpleTooltipProperty tooltipProperty(IMessageCode code, Object... args) {
		return new SimpleTooltipProperty(guiConfiguration, code, args);
	}
}
