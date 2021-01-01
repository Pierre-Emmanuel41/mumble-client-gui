package fr.pederobien.mumble.client.gui.impl;

public enum Environment {
	SERVER_LIST("ServerList");

	private String fileName;

	private Environment(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return The file name associated to this enumeration filed.
	 */
	public String getFileName() {
		return fileName;
	}
}
