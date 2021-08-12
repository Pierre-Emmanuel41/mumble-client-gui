package fr.pederobien.mumble.client.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServerList;
import fr.pederobien.mumble.client.impl.MumbleServer;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;

public class ServerList implements IObservable<IObsServerList> {
	private List<IMumbleServer> servers;
	private Observable<IObsServerList> observers;

	public ServerList() {
		servers = new ArrayList<IMumbleServer>();
		observers = new Observable<IObsServerList>();
	}

	/**
	 * @return A new mumble server with default parameters value.
	 */
	public static IMumbleServer createNewDefaultServer() {
		return createNewServer(IMumbleServer.DEFAULT_NAME, IMumbleServer.DEFAULT_ADDRESS, IMumbleServer.DEFAULT_PORT);
	}

	/**
	 * Create a new mumble server based on the given parameter.
	 * 
	 * @param name    The server name.
	 * @param address The server address.
	 * @param port    The server TCP port number.
	 * 
	 * @return A new mumble server instance.
	 */
	public static IMumbleServer createNewServer(String name, String address, int port) {
		return new MumbleServer(name, address, port);
	}

	@Override
	public void addObserver(IObsServerList obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsServerList obs) {
		observers.removeObserver(obs);
	}

	/**
	 * Appends a server to this list.
	 * 
	 * @param server The server to add.
	 */
	public void add(IMumbleServer server) {
		servers.add(server);
		observers.notifyObservers(obs -> obs.onServerAdded(server));
	}

	/**
	 * Removes a server from this list.
	 * 
	 * @param server The server to remove.
	 */
	public void remove(IMumbleServer server) {
		server.dispose();
		servers.remove(server);
		observers.notifyObservers(obs -> obs.onServerRemoved(server));
	}

	/**
	 * @return A list of registered servers. This list is unmodifiable.
	 */
	public List<IMumbleServer> getServers() {
		return Collections.unmodifiableList(servers);
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after this call returns.
	 */
	public void clear() {
		int size = servers.size();
		for (int i = 0; i < size; i++)
			remove(servers.get(0));
	}
}
