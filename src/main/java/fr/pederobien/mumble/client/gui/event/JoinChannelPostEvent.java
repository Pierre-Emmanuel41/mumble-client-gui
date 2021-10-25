package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.event.ServerEvent;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.ICancellable;

public class JoinChannelPostEvent extends ServerEvent implements ICancellable {
	private boolean isCancelled;
	private IChannel previousChannel, currentChannel;

	/**
	 * Creates an event thrown when the selected channel in the user interface has changed.
	 * 
	 * @param server          The server that contains both previous and current selected channel.
	 * @param previousChannel The channel previously selected in the user interface.
	 * @param previousChannel The channel currently selected in the user interface.
	 */
	public JoinChannelPostEvent(IMumbleServer server, IChannel previousChannel, IChannel currentChannel) {
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
}