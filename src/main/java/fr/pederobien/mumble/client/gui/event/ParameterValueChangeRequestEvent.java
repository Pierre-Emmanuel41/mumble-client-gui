package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleParameterEvent;
import fr.pederobien.mumble.client.player.interfaces.IParameter;
import fr.pederobien.utils.ICancellable;

public class ParameterValueChangeRequestEvent extends MumbleParameterEvent implements ICancellable {
	private boolean isCancelled;
	private Object value;

	/**
	 * Creates an event thrown when the request in order to change the value of the parameter is about to be thrown.
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
	 * @return The future new parameter value.
	 */
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("parameter=" + getParameter().getName());
		joiner.add("currentValue=" + getParameter().getValue());
		joiner.add("newValue=" + getValue().toString());
		return String.format("%s_%s", getName(), joiner);
	}
}
