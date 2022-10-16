package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleParameterEvent;
import fr.pederobien.mumble.client.player.interfaces.IRangeParameter;
import fr.pederobien.utils.ICancellable;

public class ParameterMaxValueChangeRequestEvent extends MumbleParameterEvent implements ICancellable {
	private boolean isCancelled;
	private Object maxValue;

	/**
	 * Creates an event thrown when the user requests a new maximum value for a parameter.
	 * 
	 * @param parameter The parameter involved in this event.
	 * @param maxValue  The new parameter maximum value.
	 */
	public ParameterMaxValueChangeRequestEvent(IRangeParameter<?> parameter, Object maxValue) {
		super(parameter);
		this.maxValue = maxValue;
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
	 * @return The current parameter maximum value.
	 */
	public Object getCurrentMaxValue() {
		return getParameter().getValue();
	}

	/**
	 * @return The new parameter value.
	 */
	public Object getNewMaxValue() {
		return maxValue;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("parameter=" + getParameter().getName());
		joiner.add("currentMaxValue=" + getCurrentMaxValue());
		joiner.add("newMaxValue=" + (getNewMaxValue() == null ? "?" : getNewMaxValue().toString()));
		return String.format("%s_%s", getName(), joiner);
	}
}
