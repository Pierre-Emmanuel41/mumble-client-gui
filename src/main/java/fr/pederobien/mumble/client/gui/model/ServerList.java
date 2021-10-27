package fr.pederobien.mumble.client.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pederobien.mumble.client.gui.event.ServerListAddServerPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerListAddServerPreEvent;
import fr.pederobien.mumble.client.gui.event.ServerListRemoveServerPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerListRemoveServerPreEvent;
import fr.pederobien.mumble.client.impl.MumbleServer;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.event.EventManager;

public class ServerList {
	private List<IMumbleServer> servers;

	public ServerList() {
		servers = new ArrayList<IMumbleServer>();
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

	/**
	 * Appends a server to this list.
	 * 
	 * @param server The server to add.
	 */
	public void add(IMumbleServer server) {
		EventManager.callEvent(new ServerListAddServerPreEvent(this, server), () -> servers.add(server), new ServerListAddServerPostEvent(this, server));
	}

	/**
	 * Removes a server from this list.
	 * 
	 * @param server The server to remove.
	 */
	public void remove(IMumbleServer server) {
		Runnable remove = () -> {
			server.dispose();
			servers.remove(server);
		};
		EventManager.callEvent(new ServerListRemoveServerPreEvent(this, server), remove, new ServerListRemoveServerPostEvent(this, server));
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
