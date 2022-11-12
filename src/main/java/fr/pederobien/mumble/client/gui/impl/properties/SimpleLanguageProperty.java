package fr.pederobien.mumble.client.gui.impl.properties;

import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.dictionary.interfaces.ICode;
import fr.pederobien.dictionary.interfaces.IDictionaryContext;
import fr.pederobien.mumble.client.gui.impl.properties.InternalProperty.Action;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.beans.property.SimpleStringProperty;

public class SimpleLanguageProperty extends SimpleStringProperty {
	private InternalProperty internalProperty;
	private ICode code;
	private Object[] args;

	/**
	 * Create a string property based on the given gui configuration. If the local parameter in the gui configuration changes then
	 * this property is automatically updated.
	 * 
	 * @param guiConfiguration The gui configuration that updates this property.
	 * @param code             The code associated to the message to display.
	 * @param args             The message arguments if the message needs arguments.
	 */
	public SimpleLanguageProperty(IGuiConfiguration guiConfiguration, ICode code, Object... args) {
		internalProperty = new InternalProperty(guiConfiguration);
		this.code = code;
		this.args = args;

		internalProperty.registerAction(Action.LOCALE_CHANGED, o -> update());
		update();
	}

	/**
	 * @return The code associated to this property.
	 */
	public ICode getCode() {
		return code;
	}

	/**
	 * Set the code associated to this property. This will automatically change the displayed text.
	 * 
	 * @param code The new code associated to the message to display.
	 * @param args The new arguments array associated to the code.
	 */
	public void setCode(ICode code, Object... args) {
		this.code = code;
		this.args = args;
		update();
	}

	/**
	 * @return An array that contains arguments for message that need parameters.
	 */
	public Object[] getArgs() {
		return args;
	}

	private void update() {
		IDictionaryContext context = internalProperty.getGuiConfiguration().getDictionaryContext();
		setValue(context.getMessage(new MessageEvent(internalProperty.getGuiConfiguration().getLocale(), getCode(), getArgs())));
	}
}
