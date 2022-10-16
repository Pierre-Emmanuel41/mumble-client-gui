package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ServerListAddServerPreEvent extends ServerListEvent implements ICancellable {
	private boolean isCancelled;
	private IPlayerMumbleServer server;

	/**
	 * Creates an event thrown when a server is about to be added to a server list.
	 * 
	 * @param serverList The list to which a server is about to be added.
	 * @param server     The added server.
	 */
	public ServerListAddServerPreEvent(ServerList serverList, IPlayerMumbleServer server) {
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
	 * @return The server that is about to be added.
	 */
	public IPlayerMumbleServer getServer() {
		return server;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("serverList=" + getServerList().hashCode());
		joiner.add("server=" + getServer());
		return String.format("%s_%s", getName(), joiner);
	}
}
