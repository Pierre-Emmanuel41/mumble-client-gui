package fr.pederobien.mumble.client.gui.dictionary;

import fr.pederobien.dictionary.interfaces.IMessageCode;

public enum EMessageCode implements IMessageCode {
	// MAIN WINDOW TITLE
	// -----------------------------------------------------------------------------

	MUMBLE_WINDOW_TITLE,

	// MOST GENERIC CODE
	// -----------------------------------------------------------------------------

	// Code to display ok.
	OK,

	// Code to display cancel.
	CANCEL,

	// SERVER LIST CODE
	// -----------------------------------------------------------------------------

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

	// Code when the server is reachable
	REACHABLE_SERVER,

	// Code when the server is unreachable
	UNREACHABLE_SERVER,

	// ADD SERVER CODE
	// -----------------------------------------------------------------------------

	// Code for the title of the window that add a new server
	ADD_NEW_SERVER_TITLE,

	// Code to get the server name
	SERVER_NAME,

	// Code for the server name prompt (text field)
	SERVER_NAME_PROMPT,

	// Code for the server name tooltip (text field)
	SERVER_NAME_TOOLTIP,

	// Code to get the server ip address
	SERVER_IP_ADDRESS,

	// Code for the server ip address prompt (text field)
	SERVER_IP_ADDRESS_PROMPT,

	// Code for the server ip address tooltip (text field)
	SERVER_IP_ADDRESS_TOOLTIP,

	// Code to get the server port number
	SERVER_PORT_NUMBER,

	// Code for the server port number prompt (text field)
	SERVER_PORT_NUMBER_PROMPT,

	// Code for the server port number tooltip (text field)
	SERVER_PORT_NUMBER_TOOLTIP;

	@Override
	public String value() {
		return toString();
	}
}
