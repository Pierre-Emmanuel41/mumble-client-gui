package fr.pederobien.mumble.client.gui.impl;

import fr.pederobien.dictionary.interfaces.ICode;

public enum EErrorMessageCode implements ICode {

	/**
	 * Code when no errors occurs.
	 */
	NONE,

	/**
	 * Code when a timeout occurs.
	 */
	TIMEOUT,

	/**
	 * Code when player has not the permission to send the request.
	 */
	PERMISSION_REFUSED,

	/**
	 * Code when an unexpected error attempt on the server.
	 */
	UNEXPECTED_ERROR,

	/**
	 * Code when the request is malformed.
	 */
	REQUEST_MALFORMED,

	/**
	 * Code when the version is not supported.
	 */
	INCOMPATIBLE_VERSION,

	/**
	 * Code when there are no treatment associated to the given identifier.
	 */
	IDENTIFIER_UNKNOWN,

	/**
	 * Code when server plugins cancelled a request.
	 */
	REQUEST_CANCELLED,

	/**
	 * Code when a client try to join a server whereas it has already joined the server.
	 */
	SERVER_ALREADY_JOINED,

	/**
	 * Code when the player tries to add a channel whose the name is already used.
	 */
	CHANNEL_ALREADY_EXISTS,

	/**
	 * Code when the channel does not exist.
	 */
	CHANNEL_NOT_FOUND,

	/**
	 * Code when the player does not exist.
	 */
	PLAYER_NOT_FOUND,

	/**
	 * Code when the player involved in the request does not match with the player's mumble client.
	 */
	PLAYER_DOES_NOT_MATCH,

	/**
	 * Code when the client of a player has not joined the server yet.
	 */
	PLAYER_CLIENT_NOT_JOINED,

	/**
	 * Code when The player try to rename a player.
	 */
	PLAYER_ALREADY_EXISTS,

	/**
	 * Code when the player try to add an already registered player in a channel.
	 */
	PLAYER_ALREADY_REGISTERED,

	/**
	 * Code when player tries to mute/unmute another player that is not in same channel.
	 */
	PLAYERS_IN_DIFFERENT_CHANNELS,

	/**
	 * Code when player is not registered in a channel.
	 */
	PLAYER_NOT_REGISTERED,

	/**
	 * Code when the parameter of a sound modifier does not exist.
	 */
	PARAMETER_NOT_FOUND,

	/**
	 * Code when there is no minimum defined for a parameter.
	 */
	PARAMETER_WITHOUT_MIN,

	/**
	 * Code when there is no maximum defined for a parameter.
	 */
	PARAMETER_WITHOUT_MAX,

	/**
	 * Code when the parameter value is out of range.
	 */
	PARAMETER_VALUE_OUT_OF_RANGE,

	/**
	 * Code when the sound modifier does not exist.
	 */
	SOUND_MODIFIER_DOES_NOT_EXIST,

	/**
	 * Code when the error code cannot be parsed.
	 */
	UNKNOWN,

	;

	@Override
	public String getCode() {
		return toString();
	}
}
