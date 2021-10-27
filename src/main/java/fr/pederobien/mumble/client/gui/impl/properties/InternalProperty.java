package fr.pederobien.mumble.client.gui.impl.properties;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import fr.pederobien.mumble.client.gui.event.FontChangePostEvent;
import fr.pederobien.mumble.client.gui.event.LocaleChangePostEvent;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;

public class InternalProperty implements IEventListener {
	private Map<Action, Consumer<PropertyChangeEvent>> actions;
	private IGuiConfiguration guiConfiguration;

	public InternalProperty(IGuiConfiguration guiConfiguration) {
		this.guiConfiguration = guiConfiguration;
		EventManager.registerListener(this);
		actions = new HashMap<Action, Consumer<PropertyChangeEvent>>();
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

	@EventHandler
	public void onLanguageChanged(LocaleChangePostEvent event) {
		firePropertyChanged(Action.LOCALE_CHANGED, "Locale", event.getOldValue(), event.getNewValue());
	}

	@EventHandler
	public void onFontChanged(FontChangePostEvent event) {
		firePropertyChanged(Action.FONT_CHANGED, "Font", event.getOldValue(), event.getNewValue());
	}

	private void firePropertyChanged(Action action, String propertyName, Object oldValue, Object newValue) {
		Consumer<PropertyChangeEvent> consumer = actions.get(action);
		if (consumer != null)
			consumer.accept(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
	}
}