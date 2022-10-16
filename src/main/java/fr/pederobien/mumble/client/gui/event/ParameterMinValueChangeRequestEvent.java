package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleParameterEvent;
import fr.pederobien.mumble.client.player.interfaces.IRangeParameter;
import fr.pederobien.utils.ICancellable;

public class ParameterMinValueChangeRequestEvent extends MumbleParameterEvent implements ICancellable {
	private boolean isCancelled;
	private Object minValue;

	/**
	 * Creates an event thrown when the user requests a new minimum value for a parameter.
	 * 
	 * @param parameter The parameter involved in this event.
	 * @param minValue  The new parameter minimum value.
	 */
	public ParameterMinValueChangeRequestEvent(IRangeParameter<?> parameter, Object minValue) {
		super(parameter);
		this.minValue = minValue;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@Override
	public IRangeParameter<?> getParameter() {
		return (IRangeParameter<?>) super.getParameter();
	}

	/**
	 * @return The current parameter minimum value.
	 */
	public Object getCurrentMinValue() {
		return getParameter().getValue();
	}

	/**
	 * @return The new parameter value.
	 */
	public Object getNewMinValue() {
		return minValue;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("parameter=" + getParameter().getName());
		joiner.add("currentMinValue=" + getCurrentMinValue());
		joiner.add("newMinValue=" + (getNewMinValue() == null ? "?" : getNewMinValue().toString()));
		return String.format("%s_%s", getName(), joiner);
	}
}
