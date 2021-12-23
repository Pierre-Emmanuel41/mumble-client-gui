package fr.pederobien.mumble.client.gui.impl.properties;

import java.util.function.Function;

import fr.pederobien.mumble.client.gui.interfaces.ICode;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

public class PropertyHelper {
	private IGuiConfiguration guiConfiguration;

	public PropertyHelper(IGuiConfiguration guiConfiguration) {
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
	public SimpleLanguageProperty languageProperty(ICode code, Object... args) {
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
	public SimpleTooltipProperty tooltipProperty(ICode code, Object... args) {
		return new SimpleTooltipProperty(guiConfiguration, code, args);
	}

	/**
	 * Create a cell view. If the entered color is null then the background is not updated when the mouse enter or exit the component.
	 * 
	 * @param constructor  The constructor that create the graphic of the cell.
	 * @param enteredColor Color when mouse entered in the cell.
	 */
	public <T> ListCellView<T> cellView(Function<T, Parent> constructor, Color enteredColor) {
		return new ListCellView<>(constructor, enteredColor);
	}
}