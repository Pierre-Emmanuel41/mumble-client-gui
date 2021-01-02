package fr.pederobien.mumble.client.gui.properties;

import java.util.Locale;

import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.dictionary.interfaces.INotificationCenter;
import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.mumble.client.gui.interfaces.IObsGuiConfiguration;
import javafx.beans.property.SimpleStringProperty;

public class SimpleLanguageProperty extends SimpleStringProperty {
	private GuiConfiguration guiConfiguration;
	private INotificationCenter notificationCenter;
	private IMessageCode code;
	private Object[] args;

	public SimpleLanguageProperty(GuiConfiguration guiConfiguration, INotificationCenter notificationCenter, IMessageCode code, Object... args) {
		this.guiConfiguration = guiConfiguration;
		this.notificationCenter = notificationCenter;
		this.code = code;
		this.args = args;

		guiConfiguration.addObserver(new Observer());
		update();
	}

	/**
	 * @return The code associated to this property.
	 */
	public IMessageCode getCode() {
		return code;
	}

	/**
	 * Set the code associated to this property. This will automatically change the displayed text.
	 * 
	 * @param code
	 * @param args The new arguments array associated to the code.
	 */
	public void setCode(IMessageCode code, Object... args) {
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
		setValue(notificationCenter.getDictionaryContext().getMessage(new MessageEvent(guiConfiguration.getLocale(), getCode(), getArgs())));
	}

	private class Observer implements IObsGuiConfiguration {

		@Override
		public void onLanguageChanged(Locale oldLocale, Locale newLocale) {
			update();
		}
	}
}
