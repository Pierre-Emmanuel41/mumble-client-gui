package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleServerEvent;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ChannelJoinRequestPostEvent extends MumbleServerEvent implements ICancellable {
	private boolean isCancelled;
	private IChannel currentChannel, oldChannel;

	/**
	 * Creates an event thrown when the selected channel in the user interface has changed.
	 * 
	 * @param server         The server that contains both previous and current selected channel.
	 * @param currentChannel The channel currently selected in the user interface.
	 * @param oldChannel     The old selected channel.
	 */
	public ChannelJoinRequestPostEvent(IPlayerMumbleServer server, IChannel currentChannel, IChannel oldChannel) {
		super(server);
		this.currentChannel = currentChannel;
		this.oldChannel = oldChannel;
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
	 * @return The channel currently selected.
	 */
	public IChannel getCurrentChannel() {
		return currentChannel;
	}

	/**
	 * @return The old selected channel.
	 */
	public IChannel getOldChannel() {
		return oldChannel;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("server=" + getServer());
		joiner.add("currentChannel=" + (getCurrentChannel() == null ? null : getCurrentChannel().getName()));
		joiner.add("oldChannel=" + (getOldChannel() == null ? null : getOldChannel().getName()));
		return String.format("%s_%s", getName(), joiner);
	}
}