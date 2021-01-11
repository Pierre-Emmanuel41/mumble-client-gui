package fr.pederobien.mumble.client.gui.interfaces.observers.presenter;

import fr.pederobien.mumble.client.interfaces.IChannel;

public interface IObsChannelPresenter {

	/**
	 * Notify this observer the given channel has been clicked by the user.
	 * 
	 * @param channel The clicked channel.
	 */
	void onChannelClicked(IChannel channel);
}
