package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.utils.event.PropertyChangeEvent;

public class GuiConfigurationEvent<T> extends PropertyChangeEvent<T> {
	private IGuiConfiguration configuration;

	/**
	 * Creates an event thrown when a property of a gui configuration has changed.
	 * 
	 * @param oldValue      The old value of the property
	 * @param newValue      The new value of the property.
	 * @param configuration The configuration source involved in this event.
	 */
	public GuiConfigurationEvent(IGuiConfiguration configuration, T oldValue, T newValue) {
		super(oldValue, newValue);
		this.configuration = configuration;
	}

	/**
	 * @return The configuration involved in this event.
	 */
	public IGuiConfiguration getConfiguration() {
		return configuration;
	}
}
