package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.player.event.MumbleServerEvent;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;

public class ServerJoinRequestPostEvent extends MumbleServerEvent {

	/**
	 * Creates an event thrown when a the user requests to join a server.
	 * 
	 * @param mumbleServer The server the user request to join.
	 */
	public ServerJoinRequestPostEvent(IPlayerMumbleServer mumbleServer) {
		super(mumbleServer);
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("server=" + getServer());
		return String.format("%s_%s", getName(), joiner);
	}
}
