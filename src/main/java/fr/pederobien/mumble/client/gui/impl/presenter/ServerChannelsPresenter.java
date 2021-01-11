package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.impl.view.ChannelListView;
import fr.pederobien.mumble.client.gui.model.Server;

public class ServerChannelsPresenter extends PresenterBase {
	private ChannelListView channelListView;

	public ServerChannelsPresenter(Server server) {
		channelListView = new ChannelListView(new ChannelListPresenter(server));
	}

	/**
	 * @return The view associated to all registered channels on the server.
	 */
	public ChannelListView getChannelListView() {
		return channelListView;
	}

}
