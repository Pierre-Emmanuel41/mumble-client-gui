package fr.pederobien.mumble.client.gui.model;

import java.util.function.Consumer;

import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServer;
import fr.pederobien.mumble.client.impl.AudioThread;
import fr.pederobien.mumble.client.impl.MumbleConnection;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IMumbleConnection;
import fr.pederobien.mumble.client.interfaces.IPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsMumbleConnection;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;

public class Server implements IObservable<IObsServer> {
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_ADDRESS = "0.0.0.0";
	public static final int DEFAULT_PORT = 0;
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
		return name.equals(other.getName()) && address.equals(other.getAddress()) && port == other.getPort();
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

	/**
	 * Get the audio thread. The thread is started but does not get the microphone input and does not play data received from the
	 * remote. You have to call method {@link AudioThread#connect()} in order to get the microphone input and send it to the remote.
	 * If you only want to stop getting data, but not stopping the thread, you have to call method {@link AudioThread#disconnect()}.
	 * This will stop sending microphone data and receiving data from the remote.
	 * 
	 * @return The audio thread that capture the microphone input and play sound received from the remote.
	 */
	public AudioThread getAudioThread() {
		return connection.getAudioThread();
	}

	/**
	 * Get the player associated to this client.
	 * 
	 * @param response Callback when the response is received.
	 */
	public void getPlayer(Consumer<IResponse<IPlayer>> response) {
		connection.getPlayer(response);
	}

	/**
	 * Get the list of channel currently registered on the server.
	 * 
	 * @param response Callback when response is received.
	 */
	public void getChannels(Consumer<IResponse<IChannelList>> response) {
		connection.getChannels(response);
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

	private void initiateConnection() {
		if (this.connection != null && !this.connection.isDisposed()) {
			this.connection.dispose();
			this.connection.removeObserver(internalObserver);
			setIsReachable(false);
		}

		connection = MumbleConnection.of(getAddress(), getPort(), 32000);
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
