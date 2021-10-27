package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public class ServerJoinRequestPostEvent extends MumbleServerEvent {

	/**
	 * Creates an event thrown when a the user request to join a server.
	 * 
	 * @param mumbleServer The server the user request to join.
	 */
	public ServerJoinRequestPostEvent(IMumbleServer mumbleServer) {
		super(mumbleServer);
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("server=" + getMumbleServer());
		return String.format("%s_%s", getName(), joiner);
	}
}
