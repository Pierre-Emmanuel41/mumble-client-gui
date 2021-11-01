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
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;

public class ServerList implements IEventListener {
	private List<IMumbleServer> servers;
	private IMumbleServer selectedServer;

	public ServerList() {
		servers = new ArrayList<IMumbleServer>();
		EventManager.registerListener(this);
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
		EventManager.callEvent(new ServerListAddServerPreEvent(this, server));
	}

	/**
	 * Removes a server from this list.
	 * 
	 * @param server The server to remove.
	 */
	public void remove(IMumbleServer server) {
		EventManager.callEvent(new ServerListRemoveServerPreEvent(this, server));
	}

	/**
	 * @return A list of registered servers. This list is unmodifiable.
	 */
	public List<IMumbleServer> getServers() {
		return Collections.unmodifiableList(servers);
	}

	/**
	 * @return The server currently selected in the user interface.
	 */
	public IMumbleServer getSelectedServer() {
		return selectedServer;
	}

	/**
	 * Set the selected server in the user interface.
	 * 
	 * @param selectedServer The new selected server.
	 */
	public void setSelectedServer(IMumbleServer selectedServer) {
		this.selectedServer = selectedServer;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after this call returns.
	 */
	public void clear() {
		int size = servers.size();
		for (int i = 0; i < size; i++)
			remove(servers.get(0));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onServerAdd(ServerListAddServerPreEvent event) {
		if (!event.getServerList().equals(this))
			return;

		servers.add(event.getServer());
		EventManager.callEvent(new ServerListAddServerPostEvent(this, event.getServer()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onServerRemove(ServerListRemoveServerPreEvent event) {
		if (!event.getServerList().equals(this))
			return;

		event.getServer().dispose();
		if (servers.remove(event.getServer()) && event.getServer().equals(selectedServer))
			setSelectedServer(null);
		EventManager.callEvent(new ServerListRemoveServerPostEvent(this, event.getServer()));
	}
}
