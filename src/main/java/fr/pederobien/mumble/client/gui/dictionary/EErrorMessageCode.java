package fr.pederobien.mumble.client.gui.dictionary;

import fr.pederobien.mumble.client.gui.interfaces.ICode;

public enum EErrorMessageCode implements ICode {
	// Code when no errors happened.
	NONE,

	// Code when a timeout occurs.
	TIMEOUT,

	// Code when player has not the permission to send the request
	PERMISSION_REFUSED,

	// Code when an unexpected error attempt on the server.
	UNEXPECTED_ERROR,

	// Code when incompatible idc and oid
	INCOMPATIBLE_IDC_OID,

	// Code when there are no treatment associated to the given idc.
	IDC_UNKNOWN,

	// Code when trying to add a channel whose name is already used.
	CHANNEL_ALREADY_EXISTS,

	// Code when trying to remove a not existing channel.
	CHANNEL_DOES_NOT_EXISTS,

	// Code when the player is not recognized.
	PLAYER_NOT_RECOGNIZED,

	// Code when trying to add an already registered player in a channel.
	PLAYER_ALREADY_REGISTERED,

	// Code when trying to mute/unmute a player whereas the player name is wrong.
	BAD_PLAYER_NAME,

	// Code when player tries to mute/unmute another player that is not in same channel.
	PLAYERS_IN_DIFFERENT_CHANNELS,

	// Code when player is not registered in a channel.
	PLAYER_NOT_REGISTERED,

	// Code when cannot be parsed.
	UNKNOWN;

	@Override
	public String value() {
		return toString();
	}

}
