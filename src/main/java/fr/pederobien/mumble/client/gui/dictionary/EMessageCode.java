package fr.pederobien.mumble.client.gui.dictionary;

import fr.pederobien.mumble.client.gui.interfaces.ICode;

public enum EMessageCode implements ICode {
	// APPLICATION ALREADY RUNNING
	// -----------------------------------------------------------------------------
	// Code for the title of the alert when running the application while one is already running.
	APPLICATION_ALREADY_RUNNING_TITLE,

	// Code for the the header of the alert when running the application while one is already running.
	APPLICATION_ALREADY_RUNNING_HEADER,

	// MAIN WINDOW TITLE
	// -----------------------------------------------------------------------------

	// Window title when server list is displayed.
	MUMBLE_WINDOW_TITLE,

	// Windows title when a server has been selected to be joined.
	SERVER_WINDOW_TITLE,

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
	SERVER_PORT_NUMBER_TOOLTIP,

	// Code for the title of the alert when joining a server fails
	SERVER_JOIN_FAILED_TITLE,

	// Code for the header of the alert when joining a server fails
	SERVER_JOIN_FAILED_HEADER,

	// PLAYER CONNECTION TO A CHANNEL CODE
	// -----------------------------------------------------------------------------

	// Code for the title of the alert when player try to connect to a channel.
	FAIL_TO_JOIN_A_CHANNEL_TITLE,

	// Code for the HEADER of the alert when player try to connect to a channel.
	FAIL_TO_JOIN_A_CHANNEL_HEADER,

	// Code when the player is not connected in game.
	PLAYER_NOT_CONNECTED_IN_GAME,

	// Code for the mute button tooltip.
	MUTE_TOOLTIP,

	// Code for the unmute button tooltip.
	UNMUTE_TOOLTIP,

	// Code for the title of the alert box when updating the mute status of a player.
	MUTE_FAILED_TITLE,

	// Code for the header of the alert box when updating the mute status of a player.
	MUTE_FAILED_HEADER,

	// Code for the deafen button tooltip.
	DEAFEN_TOOLTIP,

	// Code for the undeafen button tooltip.
	UNDEAFEN_TOOLTIP,

	// Code for the title of the alert box when updating the deafen status of a player.
	DEAFEN_FAILED_TITLE,

	// Code for the header of the alert box when updating the deafen status of a player.
	DEAFEN_FAILED_HEADER,

	// Code for the hang up button tooltip.
	HANG_UP_TOOLTIP,

	// Code for the title of the alert box when removing a player from a channel fails.
	HANG_UP_FAILED_TITLE,

	// Code for the header of the alert box when removing a player from a channel fails.
	HANG_UP_FAILED_HEADER,

	// Code when player try to disconnect from a server.
	DISCONNECT_FROM_SERVER,

	// CHANNEL CONTEXT MENU CODE
	// -----------------------------------------------------------------------------

	// Code to add a channel to the server
	ADD_CHANNEL,

	// Code for the title of the window that add a channel
	ADD_CHANNEL_TITLE,

	// Code to get the channel name
	ADD_CHANNEL_NAME,

	// Code to get the channel name
	ADD_CHANNEL_NAME_PROMPT,

	// Code to get the channel name
	CHANNEL_NAME_TOOLTIP,

	// Code for the header of the alert when player try to add a channel that already exists.
	ADD_CHANNEL_NAME_RESPONSE,

	// Code to remove a channel from the server.
	REMOVE_CHANNEL,

	// Code for the title of the window that remove a channel
	REMOVE_CHANNEL_TITLE,

	// Code for the title of the window that remove a channel
	REMOVE_CHANNEL_CONFIRMATION,

	// Code for the title of the window that remove a channel
	REMOVE_CHANNEL_EXPLANATION,

	// Code for the header of the alert when player try to remove a channel and it fails.
	REMOVE_CHANNEL_RESPONSE,

	// Code to rename a channel in the server.
	RENAME_CHANNEL,

	// Code for the title of the window that rename a channel
	RENAME_CHANNEL_TITLE,

	// Code to get the channel name
	RENAME_CHANNEL_NAME,

	// Code for the header of the alert when player try to rename a channel and it fails.
	RENAME_CHANNEL_NAME_RESPONSE,

	// Code to set the channel sound modifier
	SOUND_MODIFIER,

	// Code for the title of the window that change the sound modifier of a channel
	SOUND_MODIFIER_TITLE,

	// Code to get the sound modifier name
	SOUND_MODIFIER_NAME,

	// Code for the header of the alert when player try to change the sound modifier of a channel and it fails.
	SOUND_MODIFIER_NAME_RESPONSE,

	// PLAYER CONTEXT MENU CODE
	// -----------------------------------------------------------------------------

	// Code for the title of the alert when a player try to mute/unmute a player but it fails.
	CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE_TITLE,

	// Code for the header of the alert when a player try to mute/unmute a player but it fails.
	CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE,

	// Code to kick a player
	KICK_PLAYER,

	// Code for the title of the alert when a player try to kick another player but it fails.
	CANNOT_KICK_PLAYER_RESPONSE_TITLE,

	// Code for the header of the alert when a player try to kick another player but it fails.
	CANNOT_KICK_PLAYER_RESPONSE,

	// Code for the title of the alert when a player try to leave a server and it fails.
	CANNOT_LEAVE_SERVER_RESPONSE_TITLE,

	// Code for the header of the alert when a player try to leave a server and it fails.
	CANNOT_LEAVE_SERVER_RESPONSE,

	// Code that correspond to the word "Enabled"
	ENABLE,

	// Code that correspond to the word "Disabled"
	DISABLE,

	// Code for a normal parameter value tooltip (text field)
	PARAMETER_TOOLTIP,

	// Code for a range parameter value tooltip (text field)
	RANGE_PARAMETER_TOOLTIP,

	// PLAYER STATUS CODE
	// -----------------------------------------------------------------------------

	// Code when the player is online.
	PLAYER_ONLINE,

	// Code when the player is offline.
	PLAYER_OFFLINE;

	@Override
	public String value() {
		return toString();
	}
}
