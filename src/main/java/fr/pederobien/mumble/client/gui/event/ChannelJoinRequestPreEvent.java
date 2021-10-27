package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.event.ServerEvent;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ChannelJoinRequestPreEvent extends ServerEvent implements ICancellable {
	private boolean isCancelled;
	private IChannel currentChannel, futureChannel;

	/**
	 * Creates an event thrown when the selected channel in the user interface is about to change.
	 * 
	 * @param server         The server that contains both current and future selected channel.
	 * @param currentChannel The channel currently selected in the user interface.
	 * @param futureChannel  The channel that is about to be selected in the user interface.
	 */
	public ChannelJoinRequestPreEvent(IMumbleServer server, IChannel currentChannel, IChannel futureChannel) {
		super(server);
		this.currentChannel = currentChannel;
		this.futureChannel = futureChannel;
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
	 * @return The current selected channel.
	 */
	public IChannel getCurrentChannel() {
		return currentChannel;
	}

	/**
	 * @return The channel that is about to be selected.
	 */
	public IChannel getFutureChannel() {
		return futureChannel;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("server=" + getServer().getName());
		joiner.add("currentChannel=" + (getCurrentChannel() == null ? null : getCurrentChannel().getName()));
		joiner.add("futureChannel=" + (getFutureChannel() == null ? null : getFutureChannel().getName()));
		return super.toString();
	}
}
