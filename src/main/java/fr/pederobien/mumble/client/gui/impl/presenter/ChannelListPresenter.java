package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.ChannelJoinRequestPostEvent;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter;
import fr.pederobien.mumble.client.gui.impl.view.ChannelView;
import fr.pederobien.mumble.client.player.event.MumbleChannelListChannelAddPostEvent;
import fr.pederobien.mumble.client.player.event.MumbleChannelListChannelRemovePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleServerLeavePostEvent;
import fr.pederobien.mumble.client.player.exceptions.PlayerNotOnlineException;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.mumble.client.player.interfaces.IChannelList;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ChannelListPresenter extends PresenterBase implements IEventListener {
	private IPlayerMumbleServer mumbleServer;
	private IChannelList channelList;
	private ObservableList<Object> channels;
	private Map<IChannel, ChannelView> channelViews;

	public ChannelListPresenter(IPlayerMumbleServer mumbleServer) {
		this.mumbleServer = mumbleServer;
		channels = FXCollections.observableArrayList();
		channelViews = new HashMap<IChannel, ChannelView>();
		channelList = mumbleServer.getChannels();

		for (IChannel channel : channelList)
			channels.add(channel);

		EventManager.registerListener(this);
	}

	/**
	 * @return An observable list that contains all registered channels.
	 */
	public ObservableList<Object> getChannels() {
		return channels;
	}

	/**
	 * Get the factory that creates channel view.
	 * 
	 * @param <T>          The type of object (should be {@link IChannel}).
	 * @param enteredColor The color when the mouse enters the cell.
	 * 
	 * @return The channel view factory in charge to create {@link ChannelView} and {@link ChannelPresenter}.
	 * 
	 * @throws ClassCastException If the type of object used as model to create its view is not IChannel.
	 */
	public <T> Callback<ListView<T>, ListCell<T>> channelViewFactory(Color enteredColor) {
		return listView -> getPropertyHelper().cellView(item -> {
			ChannelView view = channelViews.get(item);
			if (view == null) {
				view = new ChannelView(new ChannelPresenter(mumbleServer, (IChannel) item));
				channelViews.put((IChannel) item, view);
			}
			return view.getRoot();
		}, enteredColor);
	}

	@EventHandler
	private void onChannelAdded(MumbleChannelListChannelAddPostEvent event) {
		if (!event.getList().equals(channelList))
			return;

		dispatch(() -> channels.add(event.getChannel()));
	}

	@EventHandler
	private void onChannelRemoved(MumbleChannelListChannelRemovePostEvent event) {
		if (!event.getList().equals(channelList))
			return;

		dispatch(() -> channels.remove(event.getChannel()));
	}

	@EventHandler
	public void onJoinChannel(ChannelJoinRequestPostEvent event) {
		if (mumbleServer.getMainPlayer().getChannel() != null)
			mumbleServer.getMainPlayer().getChannel().getPlayers().leave(response -> handleRemovePlayerResponse(response));

		try {
			event.getCurrentChannel().getPlayers().join(response -> handleAddPlayerResponse(response));
		} catch (PlayerNotOnlineException e) {
			ErrorPresenter.showAndWait(AlertType.ERROR, EMessageCode.FAIL_TO_JOIN_A_CHANNEL_TITLE, EMessageCode.FAIL_TO_JOIN_A_CHANNEL_HEADER,
					EMessageCode.PLAYER_NOT_CONNECTED_IN_GAME);
		}
	}

	@EventHandler
	private void onServerLeave(MumbleServerLeavePostEvent event) {
		if (!event.getServer().equals(mumbleServer))
			return;

		EventManager.unregisterListener(this);
	}

	private void handleRemovePlayerResponse(IResponse response) {
		ErrorPresenter.showAndWait(AlertType.ERROR, EMessageCode.HANG_UP_FAILED_TITLE, EMessageCode.HANG_UP_FAILED_HEADER, response);
	}

	private void handleAddPlayerResponse(IResponse response) {
		ErrorPresenter.showAndWait(AlertType.ERROR, EMessageCode.FAIL_TO_JOIN_A_CHANNEL_TITLE, EMessageCode.FAIL_TO_JOIN_A_CHANNEL_HEADER, response);
	}
}
