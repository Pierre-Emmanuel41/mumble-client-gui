package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public class ServerListRemoveServerPostEvent extends ServerListEvent {
	private IMumbleServer server;

	/**
	 * Creates an event thrown when a server has been removed from a server list.
	 * 
	 * @param serverList The list from which a server has been removed.
	 * @param server     The removed server.
	 */
	public ServerListRemoveServerPostEvent(ServerList serverList, IMumbleServer server) {
		super(serverList);
		this.server = server;
	}

	/**
	 * @return The removed server.
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
