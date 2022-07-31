package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;

public class SelectServerPostEvent extends ServerListEvent {
	private IPlayerMumbleServer previousServer, currentServer;

	/**
	 * Creates an event thrown the selected server in user interface has changed.
	 * 
	 * @param serverList     The server list that contains both previous and current selected server.
	 * @param previousServer The server that was previously selected.
	 * @param currentServer  The server currently selected.
	 */
	public SelectServerPostEvent(ServerList serverList, IPlayerMumbleServer previousServer, IPlayerMumbleServer currentServer) {
		super(serverList);
		this.previousServer = previousServer;
		this.currentServer = currentServer;
	}

	/**
	 * @return The server previously selected.
	 */
	public IPlayerMumbleServer getPreviousServer() {
		return previousServer;
	}

	/**
	 * @return The server currently selected.
	 */
	public IPlayerMumbleServer getCurrentServer() {
		return currentServer;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("serverList=" + getServerList().hashCode());
		joiner.add("previousServer=" + (getPreviousServer() == null ? null : getPreviousServer()));
		joiner.add("currentServer=" + (getCurrentServer() == null ? null : getCurrentServer()));
		return super.toString();
	}
}
