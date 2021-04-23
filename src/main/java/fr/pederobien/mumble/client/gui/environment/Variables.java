package fr.pederobien.mumble.client.gui.environment;

public enum Variables {
	// Folder that contains resources files
	RESOURCES_FOLDER("src/main/resources/"),

	// Folder that contains dictionaries
	DICTIONARIES_FOLDER(RESOURCES_FOLDER.getFileName().concat("dictionaries").concat("/")),

	// Folder that contains pictures
	IMAGE_FOLDER(RESOURCES_FOLDER.getFileName().concat("pictures").concat("/")),

	// File that contains gui configuration parameters.
	GUI_CONFIGURATION("GuiConfiguration"),

	// File that contains all registered servers.
	SERVER_LIST("ServerList");

	private String fileName;

	private Variables(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return The file name associated to this enumeration filed.
	 */
	public String getFileName() {
		return fileName;
	}
}
