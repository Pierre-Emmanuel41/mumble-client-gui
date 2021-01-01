package fr.pederobien.mumble.client.gui.model;

public class Server {
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_ADDRESS = "0.0.0.0";
	private static final int DEFAULT_PORT = 0;
	private String name, address;
	private int port;

	public Server(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}

	public Server() {
		this(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_PORT);
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
		this.name = name;
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
		this.address = address;
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
		this.port = port;
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
}
