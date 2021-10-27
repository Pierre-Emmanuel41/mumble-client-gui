package fr.pederobien.mumble.client.gui.event;

import java.util.StringJoiner;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.ICancellable;

public class SelectServerPreEvent extends ServerListEvent implements ICancellable {
	private boolean isCancelled;
	private IMumbleServer currentServer, futureServer;

	/**
	 * Creates an event thrown the selected server in user interface is about to change.
	 * 
	 * @param serverList    The server list that contains both current and future selected server.
	 * @param currentServer The server currently selected in the user interface.
	 * @param futurServer   The server that is about to be selected.
	 */
	public SelectServerPreEvent(ServerList serverList, IMumbleServer currentServer, IMumbleServer futureServer) {
		super(serverList);
		this.currentServer = currentServer;
		this.futureServer = futureServer;
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
	 * @return The current selected server.
	 */
	public IMumbleServer getCurrentServer() {
		return currentServer;
	}

	/**
	 * @return The server that is about to be selected.
	 */
	public IMumbleServer getFutureServer() {
		return futureServer;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("serverList=" + getServerList().hashCode());
		joiner.add("currentServer=" + (getCurrentServer() == null ? null : getCurrentServer().getName()));
		joiner.add("futureServer=" + (getFutureServer() == null ? null : getFutureServer().getName()));
		return super.toString();
	}
}
