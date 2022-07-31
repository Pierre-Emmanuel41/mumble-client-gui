package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleServerEvent;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ServerJoinRequestPreEvent extends MumbleServerEvent implements ICancellable {
	private boolean isCancelled;

	/**
	 * Creates an event thrown when the user tries to join a mumble server.
	 * 
	 * @param mumbleServer The server that is about to be joined by the user.
	 */
	public ServerJoinRequestPreEvent(IPlayerMumbleServer mumbleServer) {
		super(mumbleServer);
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
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("server=" + getServer());
		return String.format("%s_%s", getName(), joiner);
	}
}
