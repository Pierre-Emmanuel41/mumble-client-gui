package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;

public class SelectedServerChangePostEvent extends ServerListEvent {
	private IPlayerMumbleServer currentServer, oldServer;

	/**
	 * Creates an event thrown the selected server in user interface has changed.
	 * 
	 * @param serverList    The server list that contains both previous and current selected server.
	 * @param currentServer The server currently selected.
	 * @param oldServer     The old selected server.
	 */
	public SelectedServerChangePostEvent(ServerList serverList, IPlayerMumbleServer currentServer, IPlayerMumbleServer oldServer) {
		super(serverList);
		this.currentServer = currentServer;
		this.oldServer = oldServer;
	}

	/**
	 * @return The server currently selected.
	 */
	public IPlayerMumbleServer getCurrentServer() {
		return currentServer;
	}

	/**
	 * @return The old selected server.
	 */
	public IPlayerMumbleServer getOldServer() {
		return oldServer;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("serverList=" + getServerList().hashCode());
		joiner.add("currentServer=" + (getCurrentServer() == null ? null : getCurrentServer()));
		joiner.add("oldServer=" + (getOldServer() == null ? null : getOldServer()));
		return String.format("%s_%s", getName(), joiner);
	}
}
