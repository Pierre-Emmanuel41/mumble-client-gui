package fr.pederobien.mumble.client.gui.interfaces.observers.model;

import fr.pederobien.mumble.client.gui.model.Server;

public interface IObsServer {

	/**
	 * Notify this observer the name of the given server has changed.
	 * 
	 * @param server  The server whose name has changed.
	 * @param oldName The old server name.
	 * @param newName The new server name.
	 */
	void onNameChanged(Server server, String oldName, String newName);

	/**
	 * Notify this observer the ip address of the specified server has changed.
	 * 
	 * @param server     The server whose ip address has changed.
	 * @param oldAddress The old server ip address;
	 * @param newAddress The new server ip address;
	 */
	void onIpAddressChanged(Server server, String oldAddress, String newAddress);

	/**
	 * Notify this observer the port number of the given server has changed.
	 * 
	 * @param server  The server whose port number has changed.
	 * @param oldPort The old server port number.
	 * @param newPort The new server port number.
	 */
	void onPortChanged(Server server, int oldPort, int newPort);

	/**
	 * Notify this observers reachable status of the given server has changed.
	 * 
	 * @param server      The server whose status has changed.
	 * @param isReachable The new status.
	 */
	void onReachableStatusChanged(Server server, boolean isReachable);
}
