package fr.pederobien.mumble.client.gui.impl;

public enum Environment {
	// Folder that contains resources files
	RESOURCES_FOLDER("src/main/resources/"),

	// File that contains gui configuration parameters.
	GUI_CONFIGURATION("GuiConfiguration"),

	// File that contains all registered servers.
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
