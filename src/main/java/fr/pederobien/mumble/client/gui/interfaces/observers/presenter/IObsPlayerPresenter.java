package fr.pederobien.mumble.client.gui.interfaces.observers.presenter;

import fr.pederobien.mumble.client.interfaces.IPlayer;

public interface IObsPlayerPresenter {

	/**
	 * Notify this observer the player is defined.
	 * 
	 * @param player The player defined by the server.
	 */
	void onPlayerDefined(IPlayer player);
}
