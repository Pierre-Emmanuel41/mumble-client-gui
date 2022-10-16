package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleParameterEvent;
import fr.pederobien.mumble.client.player.interfaces.IParameter;
import fr.pederobien.utils.ICancellable;

public class ParameterValueChangeRequestEvent extends MumbleParameterEvent implements ICancellable {
	private boolean isCancelled;
	private Object value;

	/**
	 * Creates an event thrown when the user requests a new value for a parameter.
	 * 
	 * @param parameter The parameter involved in this event.
	 * @param value     The future new parameter value.
	 */
	public ParameterValueChangeRequestEvent(IParameter<?> parameter, Object value) {
		super(parameter);
		this.value = value;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	/**
	 * @return The current parameter value.
	 */
	public Object getCurrentValue() {
		return getParameter().getValue();
	}

	/**
	 * @return The new parameter value.
	 */
	public Object getNewValue() {
		return value;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("parameter=" + getParameter().getName());
		joiner.add("currentValue=" + getCurrentValue());
		joiner.add("newValue=" + (getNewValue() == null ? "?" : getNewValue().toString()));
		return String.format("%s_%s", getName(), joiner);
	}
}
