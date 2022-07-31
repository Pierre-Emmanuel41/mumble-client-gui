package fr.pederobien.mumble.client.gui.model;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pederobien.mumble.client.gui.event.ServerListAddServerPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerListAddServerPreEvent;
import fr.pederobien.mumble.client.gui.event.ServerListRemoveServerPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerListRemoveServerPreEvent;
import fr.pederobien.mumble.client.player.impl.PlayerMumbleServer;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;

public class ServerList implements IEventListener {
	public static final String DEFAULT_SERVER_NAME = "Unknown";
	public static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_SERVER_PORT = 28000;

	private List<IPlayerMumbleServer> servers;
	private IPlayerMumbleServer selectedServer;

	public ServerList() {
		servers = new ArrayList<IPlayerMumbleServer>();
		EventManager.registerListener(this);
	}

	/**
	 * @return A new mumble server with default parameters value.
	 */
	public static IPlayerMumbleServer createNewDefaultServer() {
		return createNewServer(DEFAULT_SERVER_NAME, DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT);
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
	public static IPlayerMumbleServer createNewServer(String name, String address, int port) {
		try {
			return new PlayerMumbleServer(name, new InetSocketAddress(InetAddress.getByName(address), port));
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * Appends a server to this list.
	 * 
	 * @param server The server to add.
	 */
	public void add(IPlayerMumbleServer server) {
		EventManager.callEvent(new ServerListAddServerPreEvent(this, server));
	}

	/**
	 * Removes a server from this list.
	 * 
	 * @param server The server to remove.
	 */
	public void remove(IPlayerMumbleServer server) {
		EventManager.callEvent(new ServerListRemoveServerPreEvent(this, server));
	}

	/**
	 * @return A list of registered servers. This list is unmodifiable.
	 */
	public List<IPlayerMumbleServer> getServers() {
		return Collections.unmodifiableList(servers);
	}

	/**
	 * @return The server currently selected in the user interface.
	 */
	public IPlayerMumbleServer getSelectedServer() {
		return selectedServer;
	}

	/**
	 * Set the selected server in the user interface.
	 * 
	 * @param selectedServer The new selected server.
	 */
	public void setSelectedServer(IPlayerMumbleServer selectedServer) {
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

		event.getServer().close();
		if (servers.remove(event.getServer()) && event.getServer().equals(selectedServer))
			setSelectedServer(null);
		EventManager.callEvent(new ServerListRemoveServerPostEvent(this, event.getServer()));
	}
}
