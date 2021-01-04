package fr.pederobien.mumble.client.gui.interfaces.observers.model;

import fr.pederobien.mumble.client.gui.model.Server;

public interface IObsServerList {

	/**
	 * Notify this observer the given server has been added to the list.
	 * 
	 * @param server The added server.
	 */
	void onServerAdded(Server server);

	/**
	 * Notify this observer the given server has been removed from the list.
	 * 
	 * @param server The removed server.
	 */
	void onServerRemoved(Server server);
}
