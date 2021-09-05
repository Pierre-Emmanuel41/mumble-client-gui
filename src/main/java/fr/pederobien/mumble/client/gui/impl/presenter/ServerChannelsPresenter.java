package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.impl.view.ChannelListView;
import fr.pederobien.mumble.client.gui.impl.view.PlayerView;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;

public class ServerChannelsPresenter extends PresenterBase {
	private PlayerView playerView;
	private ChannelListView channelListView;

	public ServerChannelsPresenter(IMumbleServer server) {
		playerView = new PlayerView(new PlayerPresenter(server));
		channelListView = new ChannelListView(new ChannelListPresenter(server));
	}

	/**
	 * @return The view associated to the player status in game.
	 */
	public PlayerView getPlayerView() {
		return playerView;
	}

	/**
	 * @return The view associated to all registered channels on the server.
	 */
	public ChannelListView getChannelListView() {
		return channelListView;
	}
}
