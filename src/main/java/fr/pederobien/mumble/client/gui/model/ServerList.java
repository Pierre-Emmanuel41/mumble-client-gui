package fr.pederobien.mumble.client.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServerList;
import fr.pederobien.persistence.interfaces.IUnmodifiableNominable;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;

public class ServerList implements IUnmodifiableNominable, IObservable<IObsServerList> {
	private List<Server> servers;
	private Observable<IObsServerList> observers;

	public ServerList() {
		servers = new ArrayList<Server>();
		observers = new Observable<IObsServerList>();
	}

	@Override
	public String getName() {
		return "ServerList";
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
	public void add(Server server) {
		servers.add(server);
		observers.notifyObservers(obs -> obs.onServerAdded(server));
	}

	/**
	 * Removes a server from this list.
	 * 
	 * @param server The server to remove.
	 */
	public void remove(Server server) {
		servers.remove(server);
		observers.notifyObservers(obs -> obs.onServerRemoved(server));
	}

	/**
	 * @return A list of registered servers. This list is unmodifiable.
	 */
	public List<Server> getServers() {
		return Collections.unmodifiableList(servers);
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after this call returns.
	 */
	public void clear() {
		servers.clear();
	}
}
