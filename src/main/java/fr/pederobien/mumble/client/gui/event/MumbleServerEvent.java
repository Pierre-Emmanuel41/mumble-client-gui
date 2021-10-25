package fr.pederobien.mumble.client.gui.event;

import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public class MumbleServerEvent extends MumbleGuiEvent {
	private IMumbleServer mumbleServer;

	/**
	 * Creates a mumble server event.
	 * 
	 * @param mumbleServer The server source involved in this event.
	 */
	public MumbleServerEvent(IMumbleServer mumbleServer) {
		this.mumbleServer = mumbleServer;
	}

	/**
	 * @return The mumble server involved in this event.
	 */
	public IMumbleServer getMumbleServer() {
		return mumbleServer;
	}
}
