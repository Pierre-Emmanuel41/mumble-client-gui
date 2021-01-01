package fr.pederobien.mumble.client.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pederobien.persistence.interfaces.IUnmodifiableNominable;

public class ServerList implements IUnmodifiableNominable {
	private List<Server> servers;

	public ServerList() {
		servers = new ArrayList<Server>();
	}

	@Override
	public String getName() {
		return "ServerList";
	}

	/**
	 * Appends a server to this list.
	 * 
	 * @param server The server to add.
	 */
	public void add(Server server) {
		servers.add(server);
	}

	/**
	 * Removes a server from this list.
	 * 
	 * @param server The server to remove.
	 */
	public void remove(Server server) {
		servers.remove(server);
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
