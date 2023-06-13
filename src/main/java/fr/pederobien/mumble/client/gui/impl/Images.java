package fr.pederobien.mumble.client.gui.impl;

public enum Images {

	/**
	 * File that correspond to a not muted microphone.
	 */
	MICROPHONE_UNMUTE("Unmute.png"),

	/**
	 * File that correspond to a muted microphone.
	 */
	MICROPHONE_MUTE("Mute.png"),

	/**
	 * File that correspond to the microphone off.
	 */
	MICROPHONE_OFF("MicroOff.png"),

	/**
	 * File that correspond to a not deafened headset.
	 */
	HEADSET_UNDEAFEN("Undeafen.png"),

	/**
	 * File that correspond to a deafened headset.
	 */
	HEADSET_DEAFEN("Deafen.png"),

	/**
	 * File that correspond to the headset off.
	 */
	HEADSET_OFF("HeadsetOff.png"),

	/**
	 * File that correspond to the hang up.
	 */
	HANG_UP("Hangup.png");
	;

	private String name;

	private Images(String name) {
		this.name = name;
	}

	/**
	 * @return The name of the file associated to this image.
	 */
	public String getName() {
		return name;
	}
}
