package fr.pederobien.mumble.client.gui.model;

import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServer;
import fr.pederobien.mumble.client.impl.MumbleConnection;
import fr.pederobien.mumble.client.interfaces.IMumbleConnection;
import fr.pederobien.mumble.client.interfaces.observers.IObsMumbleConnection;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;

public class Server implements IObservable<IObsServer> {
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_ADDRESS = "0.0.0.0";
	private static final int DEFAULT_PORT = 0;
	private String name, address;
	private int port;
	private boolean isReachable;
	private Observable<IObsServer> observers;
	private IMumbleConnection connection;
	private InternalObserver internalObserver;

	private Server(String name, String address, int port, boolean connect) {
		this.name = name;
		this.address = address;
		this.port = port;
		observers = new Observable<IObsServer>();

		internalObserver = new InternalObserver();

		if (connect)
			initiateConnection();
	}

	public Server(String name, String address, int port) {
		this(name, address, port, true);
	}

	public Server() {
		this(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_PORT, false);
	}

	@Override
	public void addObserver(IObsServer obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsServer obs) {
		observers.removeObserver(obs);
	}

	/**
	 * @return The server name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the server name.
	 * 
	 * @param name The new server name.
	 */
	public void setName(String name) {
		if (this.name != null && this.name == name)
			return;
		String oldName = this.name;
		this.name = name;
		observers.notifyObservers(obs -> obs.onNameChanged(this, oldName, name));
	}

	/**
	 * @return The server address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set the server address.
	 * 
	 * @param address The new server address.
	 */
	public void setAddress(String address) {
		if (this.address != null && this.address == address)
			return;
		String oldAddress = this.address;
		this.address = address;
		observers.notifyObservers(obs -> obs.onIpAddressChanged(this, oldAddress, address));
		initiateConnection();
	}

	/**
	 * @return The server port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set the server port number.
	 * 
	 * @param port The new server port number.
	 */
	public void setPort(int port) {
		if (this.port == port)
			return;
		int oldPort = this.port;
		this.port = port;
		observers.notifyObservers(obs -> obs.onPortChanged(this, oldPort, port));
		initiateConnection();
	}

	/**
	 * @return True if the server is opened, false otherwise.
	 */
	public boolean isReachable() {
		return isReachable;
	}

	/**
	 * Attempt a connection to the remove.
	 */
	public void connect() {
		connection.connect();
	}

	/**
	 * Abort the connection to the remote.
	 */
	public void disconnect() {
		connection.disconnect();
	}

	/**
	 * Close definitively this connection.
	 */
	public void dispose() {
		connection.dispose();
	}

	@Override
	public String toString() {
		return "Server={" + name + "," + address + "," + port + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Server))
			return false;

		Server other = (Server) obj;
		return address.equals(other.getAddress()) && port == other.getPort();
	}

	private void initiateConnection() {
		if (this.connection != null && !this.connection.isDisposed()) {
			this.connection.dispose();
			this.connection.removeObserver(internalObserver);
			setIsReachable(false);
		}

		connection = MumbleConnection.of(getAddress(), getPort());
		connection.addObserver(internalObserver);
		connection.connect();
	}

	private void setIsReachable(boolean isReachable) {
		if (this.isReachable == isReachable)
			return;
		this.isReachable = isReachable;
		observers.notifyObservers(obs -> obs.onReachableStatusChanged(this, isReachable));
	}

	private class InternalObserver implements IObsMumbleConnection {

		@Override
		public void onConnectionComplete() {
			setIsReachable(true);
		}

		@Override
		public void onConnectionDisposed() {
			setIsReachable(false);
		}

		@Override
		public void onConnectionLost() {
			setIsReachable(false);
		}
	}
}
