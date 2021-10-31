package fr.pederobien.mumble.client.gui.environment;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum Variables {
	// Folder that contains resources files
	RESOURCES_FOLDER(Paths.get("src", "main", "resources")),

	// Folder that contains dictionaries
	DICTIONARIES_FOLDER(RESOURCES_FOLDER.getPath().resolve("dictionaries")),

	// Folder that contains pictures
	IMAGE_FOLDER(RESOURCES_FOLDER.getPath().resolve("images")),

	// Folder that contains configuration files,
	MUMBLE_FOLDER(Paths.get(System.getenv("APPDATA"), ".mumble")),

	// Folder that contains log files
	LOG_FOLDER(MUMBLE_FOLDER.getPath().resolve("logs")),

	// File used to create only one instance of the application
	LOCK_FILE("MumbleClientApplication.lock"),

	// File that contains gui configuration parameters.
	GUI_CONFIGURATION("GuiConfiguration"),

	// File that contains all registered servers.
	SERVER_LIST("ServerList"),

	// File that correspond to the unmute microphone.
	MICROPHONE_UNMUTE("Unmute.png"),

	// File that correspond to the mute microphone.
	MICROPHONE_MUTE("Mute.png"),

	// File that correspond to the microphone off.
	MICROPHONE_OFF("MicroOff.png"),

	// File that correspond to the undeafen headset.
	HEADSET_UNDEAFEN("Undeafen.png"),

	// File that correspond to the deafen headset.
	HEADSET_DEAFEN("Deafen.png"),

	// File that correspond to the headset off.
	HEADSET_OFF("HeadsetOff.png"),

	// File that correspond to the hang up.
	HANG_UP("Hangup.png");

	private Path path;
	private String fileName;

	private Variables(String fileName) {
		this.fileName = fileName;
	}

	private Variables(Path path) {
		this.path = path;
		this.fileName = path.toString();
	}

	/**
	 * @return The file name associated to this enumeration filed.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return The path leading to this variable. This path is null when the variable refers to a file and not to a folder.
	 */
	public Path getPath() {
		return path;
	}
}
