package fr.pederobien.mumble.client.gui.impl;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.messenger.interfaces.IErrorCode;
import fr.pederobien.mumble.client.gui.dictionary.EErrorMessageCode;
import fr.pederobien.mumble.client.gui.interfaces.ICode;
import fr.pederobien.mumble.common.impl.MumbleErrorCode;

public enum ErrorCodeWrapper {

	/**
	 * Code when no errors occurs.
	 */
	NONE(MumbleErrorCode.NONE, EErrorMessageCode.NONE),

	/**
	 * Code when a timeout occurs.
	 */
	TIMEOUT(MumbleErrorCode.TIMEOUT, EErrorMessageCode.TIMEOUT),

	/**
	 * Code when player has not the permission to send the request.
	 */
	PERMISSION_REFUSED(MumbleErrorCode.PERMISSION_REFUSED, EErrorMessageCode.PERMISSION_REFUSED),

	/**
	 * Code when an unexpected error attempt on the server.
	 */
	UNEXPECTED_ERROR(MumbleErrorCode.UNEXPECTED_ERROR, EErrorMessageCode.UNEXPECTED_ERROR),

	/**
	 * Code when the request is malformed.
	 */
	REQUEST_MALFORMED(MumbleErrorCode.REQUEST_MALFORMED, EErrorMessageCode.REQUEST_MALFORMED),

	/**
	 * Code when the version is not supported.
	 */
	INCOMPATIBLE_VERSION(MumbleErrorCode.INCOMPATIBLE_VERSION, EErrorMessageCode.INCOMPATIBLE_VERSION),

	/**
	 * Code when there are no treatment associated to the given identifier.
	 */
	IDENTIFIER_UNKNOWN(MumbleErrorCode.IDENTIFIER_UNKNOWN, EErrorMessageCode.IDENTIFIER_UNKNOWN),

	/**
	 * Code when server plugins cancelled a request.
	 */
	REQUEST_CANCELLED(MumbleErrorCode.REQUEST_CANCELLED, EErrorMessageCode.REQUEST_CANCELLED),

	/**
	 * Code when a client try to join a server whereas it has already joined the server.
	 */
	SERVER_ALREADY_JOINED(MumbleErrorCode.SERVER_ALREADY_JOINED, EErrorMessageCode.SERVER_ALREADY_JOINED),

	/**
	 * Code when the player tries to add a channel whose the name is already used.
	 */
	CHANNEL_ALREADY_EXISTS(MumbleErrorCode.CHANNEL_ALREADY_EXISTS, EErrorMessageCode.CHANNEL_ALREADY_EXISTS),

	/**
	 * Code when the channel does not exist.
	 */
	CHANNEL_NOT_FOUND(MumbleErrorCode.CHANNEL_NOT_FOUND, EErrorMessageCode.CHANNEL_NOT_FOUND),

	/**
	 * Code when the player does not exist.
	 */
	PLAYER_NOT_FOUND(MumbleErrorCode.PLAYER_NOT_FOUND, EErrorMessageCode.PLAYER_NOT_FOUND),

	/**
	 * Code when the player involved in the request does not match with the player's mumble client.
	 */
	PLAYER_DOES_NOT_MATCH(MumbleErrorCode.PLAYER_DOES_NOT_MATCH, EErrorMessageCode.PLAYER_DOES_NOT_MATCH),

	/**
	 * Code when the client of a player has not joined the server yet.
	 */
	PLAYER_CLIENT_NOT_JOINED(MumbleErrorCode.PLAYER_CLIENT_NOT_JOINED, EErrorMessageCode.PLAYER_CLIENT_NOT_JOINED),

	/**
	 * Code when The player try to rename a player.
	 */
	PLAYER_ALREADY_EXISTS(MumbleErrorCode.PLAYER_ALREADY_EXISTS, EErrorMessageCode.PLAYER_ALREADY_EXISTS),

	/**
	 * Code when the player try to add an already registered player in a channel.
	 */
	PLAYER_ALREADY_REGISTERED(MumbleErrorCode.PLAYER_ALREADY_REGISTERED, EErrorMessageCode.PLAYER_ALREADY_REGISTERED),

	/**
	 * Code when player tries to mute/unmute another player that is not in same channel.
	 */
	PLAYERS_IN_DIFFERENT_CHANNELS(MumbleErrorCode.PLAYERS_IN_DIFFERENT_CHANNELS, EErrorMessageCode.PLAYERS_IN_DIFFERENT_CHANNELS),

	/**
	 * Code when player is not registered in a channel.
	 */
	PLAYER_NOT_REGISTERED(MumbleErrorCode.PLAYER_NOT_REGISTERED, EErrorMessageCode.PLAYER_NOT_REGISTERED),

	/**
	 * Code when the parameter of a sound modifier does not exist.
	 */
	PARAMETER_NOT_FOUND(MumbleErrorCode.PARAMETER_NOT_FOUND, EErrorMessageCode.PARAMETER_NOT_FOUND),

	/**
	 * Code when there is no minimum defined for a parameter.
	 */
	PARAMETER_WITHOUT_MIN(MumbleErrorCode.PARAMETER_WITHOUT_MIN, EErrorMessageCode.PARAMETER_WITHOUT_MIN),

	/**
	 * Code when there is no maximum defined for a parameter.
	 */
	PARAMETER_WITHOUT_MAX(MumbleErrorCode.PARAMETER_WITHOUT_MAX, EErrorMessageCode.PARAMETER_WITHOUT_MAX),

	/**
	 * Code when the parameter value is out of range.
	 */
	PARAMETER_VALUE_OUT_OF_RANGE(MumbleErrorCode.PARAMETER_VALUE_OUT_OF_RANGE, EErrorMessageCode.PARAMETER_VALUE_OUT_OF_RANGE),

	/**
	 * Code when the sound modifier does not exist.
	 */
	SOUND_MODIFIER_DOES_NOT_EXIST(MumbleErrorCode.SOUND_MODIFIER_DOES_NOT_EXIST, EErrorMessageCode.SOUND_MODIFIER_DOES_NOT_EXIST),

	/**
	 * Code when the error code cannot be parsed.
	 */
	UNKNOWN(MumbleErrorCode.UNKNOWN, EErrorMessageCode.UNKNOWN),

	;

	private static final Map<MumbleErrorCode, ErrorCodeWrapper> codes;
	private MumbleErrorCode errorCode;
	private ICode messageCode;

	private ErrorCodeWrapper(MumbleErrorCode errorCode, ICode messageCode) {
		this.errorCode = errorCode;
		this.messageCode = messageCode;
	}

	/**
	 * Find the wrapper associated to the given code.
	 * 
	 * @param errorCode The code used to retrieve the wrapper.
	 * 
	 * @return The wrapper is it exists or null.
	 */
	public static ErrorCodeWrapper getByErrorCode(IErrorCode errorCode) {
		return codes.get(errorCode);
	}

	/**
	 * @return The error code associated to this wrapper.
	 */
	public MumbleErrorCode getMumbleErrorCode() {
		return errorCode;
	}

	/**
	 * @return The message code associated to this wrapper.
	 */
	public ICode getMessageCode() {
		return messageCode;
	}

	static {
		codes = new HashMap<MumbleErrorCode, ErrorCodeWrapper>();
		for (ErrorCodeWrapper wrapper : values())
			codes.put(wrapper.getMumbleErrorCode(), wrapper);
	}
}
