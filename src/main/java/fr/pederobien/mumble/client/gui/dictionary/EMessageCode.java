package fr.pederobien.mumble.client.gui.dictionary;

import fr.pederobien.dictionary.interfaces.IMessageCode;

public enum EMessageCode implements IMessageCode {
	// Code when there is no registered server.
	EMPTY_SERVER_LIST,

	// Code to join a server
	JOIN_SERVER,

	// Code to add a server
	ADD_SERVER,

	// Code to edit a server
	EDIT_SERVER,

	// Code to delete a server
	DELETE_SERVER,

	// Code to refresh servers state
	REFRESH_SERVERS,

	// Code when the server is reachable
	REACHABLE_SERVER,

	// Code when the server is unreachable
	UNREACHABLE_SERVER;

	@Override
	public String value() {
		return toString();
	}
}
