package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ServerListRemoveServerPreEvent extends ServerListEvent implements ICancellable {
	private boolean isCancelled;
	private IMumbleServer server;

	/**
	 * Creates an event thrown when a server is about to be removed from a server list.
	 * 
	 * @param serverList The list from which a server is about to be removed.
	 * @param server     The removed server.
	 */
	public ServerListRemoveServerPreEvent(ServerList serverList, IMumbleServer server) {
		super(serverList);
		this.server = server;
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
	 * @return The server that is about to be removed.
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
