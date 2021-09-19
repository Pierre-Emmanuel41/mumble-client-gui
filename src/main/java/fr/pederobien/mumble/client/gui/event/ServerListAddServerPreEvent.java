package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.ICancellable;

public class ServerListAddServerPreEvent extends ServerListEvent implements ICancellable {
	private boolean isCancelled;
	private IMumbleServer server;

	/**
	 * Creates an event thrown when a server is about to be added to a server list.
	 * 
	 * @param serverList The list to which a server is about to be added.
	 * @param server     The added server.
	 */
	public ServerListAddServerPreEvent(ServerList serverList, IMumbleServer server) {
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
	public IMumbleServer getServer() {
		return server;
	}
}
