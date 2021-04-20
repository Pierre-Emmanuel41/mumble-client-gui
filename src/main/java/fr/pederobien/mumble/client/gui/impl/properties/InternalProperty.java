package fr.pederobien.mumble.client.gui.impl.properties;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsGuiConfiguration;
import javafx.scene.text.Font;

public class InternalProperty {
	private Map<Action, Consumer<PropertyChangeEvent>> actions;
	private IGuiConfiguration guiConfiguration;

	public InternalProperty(IGuiConfiguration guiConfiguration) {
		this.guiConfiguration = guiConfiguration;
		this.guiConfiguration.addObserver(new Observer());
		actions = new HashMap<InternalProperty.Action, Consumer<PropertyChangeEvent>>();
	}

	/**
	 * Register an action to execute for the given enumeration value.
	 * 
	 * @param action   The action type.
	 * @param consumer The code to execute.
	 */
	public void registerAction(Action action, Consumer<PropertyChangeEvent> consumer) {
		actions.put(action, consumer);
	}

	/**
	 * @return The gui configuration this internal property observes.
	 */
	public IGuiConfiguration getGuiConfiguration() {
		return guiConfiguration;
	}

	public enum Action {
		// Action when the gui locale has changed.
		LOCALE_CHANGED,

		// Action when the gui Font changed.
		FONT_CHANGED,
	}

	private class Observer implements IObsGuiConfiguration {

		@Override
		public void onLanguageChanged(Locale oldLocale, Locale newLocale) {
			firePropertyChanged(Action.LOCALE_CHANGED, "Locale", oldLocale, newLocale);
		}

		@Override
		public void onFontChanged(Font oldFont, Font newFont) {
			firePropertyChanged(Action.FONT_CHANGED, "Font", oldFont, newFont);
		}

		private void firePropertyChanged(Action action, String propertyName, Object oldValue, Object newValue) {
			Consumer<PropertyChangeEvent> consumer = actions.get(action);
			if (consumer != null)
				consumer.accept(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
		}
	}
}