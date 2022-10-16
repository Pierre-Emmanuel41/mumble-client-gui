package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.ICancellable;

public class SelectedServerChangePreEvent extends ServerListEvent implements ICancellable {
	private boolean isCancelled;
	private IPlayerMumbleServer currentServer, newServer;

	/**
	 * Creates an event thrown the selected server in user interface is about to change.
	 * 
	 * @param serverList    The server list that contains both current and future selected server.
	 * @param currentServer The server currently selected in the user interface.
	 * @param newServer     The server that is about to be selected.
	 */
	public SelectedServerChangePreEvent(ServerList serverList, IPlayerMumbleServer currentServer, IPlayerMumbleServer newServer) {
		super(serverList);
		this.currentServer = currentServer;
		this.newServer = newServer;
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
	 * @return The server currently selected.
	 */
	public IPlayerMumbleServer getCurrentServer() {
		return currentServer;
	}

	/**
	 * @return The server that is about to be selected.
	 */
	public IPlayerMumbleServer getNewServer() {
		return newServer;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		joiner.add("serverList=" + getServerList().hashCode());
		joiner.add("currentServer=" + (getCurrentServer() == null ? null : getCurrentServer()));
		joiner.add("newServer=" + (getNewServer() == null ? null : getNewServer()));
		return String.format("%s_%s", getName(), joiner);
	}
}
