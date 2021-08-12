package fr.pederobien.mumble.client.gui.interfaces.observers.presenter;

import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public interface IObsServerListPresenter {

	/**
	 * Notify this observer the selected server has changed.
	 * 
	 * @param oldServer The old selected server.
	 * @param newServer The new selected server.
	 */
	void onSelectedServerChanged(IMumbleServer oldServer, IMumbleServer newServer);

	/**
	 * Notify this observer the user did a double click on the given server.
	 * 
	 * @param server The double clicked server.
	 */
	void onDoubleClickOnServer(IMumbleServer server);
}
