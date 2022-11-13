package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.impl.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ChannelListView;
import fr.pederobien.mumble.client.gui.impl.view.MainPlayerView;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;

public class ServerDetailsPresenter extends PresenterBase {
	private MainPlayerView mainPlayerView;
	private ChannelListView channelListView;

	/**
	 * Creates a presenter in order to display the characteristics of the given server.
	 * 
	 * @param server The server associated to this presenter.
	 */
	public ServerDetailsPresenter(IPlayerMumbleServer server) {
		setPrimaryStageTitle(EMessageCode.SERVER_WINDOW_TITLE, server.getName(), server.getAddress().getAddress().getHostAddress(), server.getAddress().getPort());
		mainPlayerView = new MainPlayerView(new MainPlayerPresenter(server.getMainPlayer()));
		channelListView = new ChannelListView(new ChannelListPresenter(server));
	}

	/**
	 * @return The view associated to the player status in game.
	 */
	public MainPlayerView getPlayerView() {
		return mainPlayerView;
	}

	/**
	 * @return The view associated to all registered channels on the server.
	 */
	public ChannelListView getChannelListView() {
		return channelListView;
	}
}
