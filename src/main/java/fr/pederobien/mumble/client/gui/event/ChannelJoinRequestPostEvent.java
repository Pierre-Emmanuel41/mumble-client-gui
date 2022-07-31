package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleServerEvent;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ChannelJoinRequestPostEvent extends MumbleServerEvent implements ICancellable {
	private boolean isCancelled;
	private IChannel previousChannel, currentChannel;

	/**
	 * Creates an event thrown when the selected channel in the user interface has changed.
	 * 
	 * @param server          The server that contains both previous and current selected channel.
	 * @param previousChannel The channel previously selected in the user interface.
	 * @param previousChannel The channel currently selected in the user interface.
	 */
	public ChannelJoinRequestPostEvent(IPlayerMumbleServer server, IChannel previousChannel, IChannel currentChannel) {
		super(server);
		this.previousChannel = previousChannel;
		this.currentChannel = currentChannel;
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
	public IChannel getPreviousChannel() {
		return previousChannel;
	}

	/**
	 * @return The channel that is about to be selected.
	 */
	public IChannel getCurrentChannel() {
		return currentChannel;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("server=" + getServer());
		joiner.add("previousChannel=" + (getPreviousChannel() == null ? null : getPreviousChannel().getName()));
		joiner.add("currentChannel=" + (getCurrentChannel() == null ? null : getCurrentChannel().getName()));
		return String.format("%s_%s", getName(), joiner);
	}
}