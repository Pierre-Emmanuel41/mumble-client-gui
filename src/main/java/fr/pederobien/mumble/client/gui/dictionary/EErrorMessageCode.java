package fr.pederobien.mumble.client.gui.dictionary;

import fr.pederobien.dictionary.interfaces.IMessageCode;

public enum EErrorMessageCode implements IMessageCode {
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

	// Code when cannot be parsed.
	UNKNOWN;

	@Override
	public String value() {
		return toString();
	}

}
