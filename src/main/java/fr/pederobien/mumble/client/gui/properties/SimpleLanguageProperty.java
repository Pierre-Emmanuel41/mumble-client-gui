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
	}

	public IMessageCode getCode() {
		return code;
	}

	public void setCode(IMessageCode code) {
		this.code = code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	private class Observer implements IObsGuiConfiguration {

		@Override
		public void onLanguageChanged(Locale oldLocale, Locale newLocale) {
			setValue(notificationCenter.getDictionaryContext().getMessage(new MessageEvent(guiConfiguration.getLocale(), getCode(), getArgs())));
		}
	}
}
