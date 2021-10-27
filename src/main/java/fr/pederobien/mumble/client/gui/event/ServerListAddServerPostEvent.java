package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public class ServerListAddServerPostEvent extends ServerListEvent {
	private IMumbleServer server;

	/**
	 * Creates an event thrown when a server has been added to a server list.
	 * 
	 * @param serverList The list to which a server has been added.
	 * @param server     The added server.
	 */
	public ServerListAddServerPostEvent(ServerList serverList, IMumbleServer server) {
		super(serverList);
		this.server = server;
	}

	/**
	 * @return The added server.
	 */
	public IMumbleServer getServer() {
		return server;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("serverList=" + getServerList().hashCode());
		joiner.add("server=" + getServer().getName());
		return String.format("%s_%s", getName(), joiner);
	}
}
