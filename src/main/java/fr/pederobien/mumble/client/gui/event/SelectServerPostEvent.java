package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public class SelectServerPostEvent extends ServerListEvent {
	private IMumbleServer previousServer, currentServer;

	/**
	 * Creates an event thrown the selected server in user interface has changed.
	 * 
	 * @param serverList     The server list that contains both previous and current selected server.
	 * @param previousServer The server that was previously selected.
	 * @param currentServer  The server currently selected.
	 */
	public SelectServerPostEvent(ServerList serverList, IMumbleServer previousServer, IMumbleServer currentServer) {
		super(serverList);
		this.previousServer = previousServer;
		this.currentServer = currentServer;
	}

	/**
	 * @return The server previously selected.
	 */
	public IMumbleServer getPreviousServer() {
		return previousServer;
	}

	/**
	 * @return The server currently selected.
	 */
	public IMumbleServer getCurrentServer() {
		return currentServer;
	}
}
