package fr.pederobien.mumble.client.gui.impl;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.dictionary.EErrorMessageCode;
import fr.pederobien.mumble.common.impl.ErrorCode;

public enum ErrorCodeWrapper {
	// Code when no errors happened.
	NONE(ErrorCode.NONE, EErrorMessageCode.NONE),

	// Code when a timeout occurs.
	TIMEOUT(ErrorCode.TIMEOUT, EErrorMessageCode.TIMEOUT),

	// Code when player has not the permission to send the request
	PERMISSION_REFUSED(ErrorCode.PERMISSION_REFUSED, EErrorMessageCode.PERMISSION_REFUSED),

	// Code when an unexpected error attempt on the server.
	UNEXPECTED_ERROR(ErrorCode.UNEXPECTED_ERROR, EErrorMessageCode.UNEXPECTED_ERROR),

	// Code when incompatible idc and oid
	INCOMPATIBLE_IDC_OID(ErrorCode.INCOMPATIBLE_IDC_OID, EErrorMessageCode.INCOMPATIBLE_IDC_OID),

	// Code when there are no treatment associated to the given idc.
	IDC_UNKNOWN(ErrorCode.IDC_UNKNOWN, EErrorMessageCode.IDC_UNKNOWN),

	// Code when trying to add a channel whose name is already used.
	CHANNEL_ALREADY_EXISTS(ErrorCode.CHANNEL_ALREADY_EXISTS, EErrorMessageCode.CHANNEL_ALREADY_EXISTS),

	// Code when trying to remove a not existing channel.
	CHANNEL_DOES_NOT_EXISTS(ErrorCode.CHANNEL_DOES_NOT_EXISTS, EErrorMessageCode.CHANNEL_DOES_NOT_EXISTS),

	// Code when the player is not recognized.
	PLAYER_NOT_RECOGNIZED(ErrorCode.PLAYER_NOT_RECOGNIZED, EErrorMessageCode.PLAYER_NOT_RECOGNIZED),

	// Code when trying to add an already registered player in a channel.
	PLAYER_ALREADY_REGISTERED(ErrorCode.PLAYER_ALREADY_REGISTERED, EErrorMessageCode.PLAYER_ALREADY_REGISTERED),

	// Code when cannot be parsed.
	UNKNOWN(ErrorCode.UNKNOWN, EErrorMessageCode.UNKNOWN);

	private static final Map<ErrorCode, ErrorCodeWrapper> codes;
	private ErrorCode errorCode;
	private IMessageCode messageCode;

	private ErrorCodeWrapper(ErrorCode errorCode, IMessageCode messageCode) {
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
	public static ErrorCodeWrapper getByErrorCode(ErrorCode errorCode) {
		return codes.get(errorCode);
	}

	/**
	 * @return The error code associated to this wrapper.
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * @return The message code associated to this wrapper.
	 */
	public IMessageCode getMessageCode() {
		return messageCode;
	}

	static {
		codes = new HashMap<ErrorCode, ErrorCodeWrapper>();
		for (ErrorCodeWrapper wrapper : values())
			codes.put(wrapper.getErrorCode(), wrapper);
	}
}
