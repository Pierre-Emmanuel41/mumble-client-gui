package fr.pederobien.mumble.client.gui.interfaces.observers.presenter;

import fr.pederobien.mumble.client.gui.model.Server;

public interface IObsServerListPresenter {

	/**
	 * Notify this observer the selected server has changed.
	 * 
	 * @param oldServer The old selected server.
	 * @param newServer The new selected server.
	 */
	void onSelectedServerChanged(Server oldServer, Server newServer);

	/**
	 * Notify this observer the user did a double click on the given server.
	 * 
	 * @param server The double clicked server.
	 */
	void onDoubleClickOnServer(Server server);
}
