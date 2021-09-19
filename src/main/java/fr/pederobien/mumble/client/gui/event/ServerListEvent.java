package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.utils.event.Event;

public class ServerListEvent extends Event {
	private ServerList serverList;

	/**
	 * Creates a server list event.
	 * 
	 * @param serverList The server list source involved in tis event.
	 */
	public ServerListEvent(ServerList serverList) {
		this.serverList = serverList;
	}

	/**
	 * @return The server list involved in this event.
	 */
	public ServerList getServerList() {
		return serverList;
	}
}