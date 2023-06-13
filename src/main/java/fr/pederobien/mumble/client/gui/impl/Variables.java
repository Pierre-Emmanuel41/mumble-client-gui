package fr.pederobien.mumble.client.gui.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import fr.pederobien.javafx.configuration.interfaces.IEnvironmentVariable;

public enum Variables implements IEnvironmentVariable {

	/**
	 * Folder that contains resource files.
	 */
	RESOURCES_FOLDER(Paths.get("src", "main", "resources")),

	/**
	 * Folder that contains dictionaries.
	 */
	DICTIONARIES_FOLDER(RESOURCES_FOLDER.getPath().resolve("dictionaries")),

	/**
	 * Folder that contains pictures.
	 */
	IMAGES_FOLDER(RESOURCES_FOLDER.getPath().resolve("images")),

	/**
	 * Folder that contains configuration files.
	 */
	MUMBLE_FOLDER(Paths.get(System.getenv("APPDATA"), ".mumble")),

	/**
	 * Folder that contains log files.
	 */
	LOG_FOLDER(MUMBLE_FOLDER.getPath().resolve("logs")),

	/**
	 * File used to create only one instance of the application
	 */
	LOCK_FILE(MUMBLE_FOLDER.getPath().resolve("MumbleClientApplication.lock")),

	/**
	 * File that contains all registered servers.
	 */
	SERVER_LIST(MUMBLE_FOLDER.getPath().resolve("ServerList.xml")),

	;

	private Path path;

	private Variables(Path path) {
		this.path = path;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public void setPath(Path path) {
		this.path = path;
	}
}
