package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.mumble.client.event.ChannelAddPostEvent;
import fr.pederobien.mumble.client.event.ChannelRemovePostEvent;
import fr.pederobien.mumble.client.event.ServerLeavePostEvent;
import fr.pederobien.mumble.client.event.ServerReachableChangeEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.ChannelJoinRequestPostEvent;
import fr.pederobien.mumble.client.gui.impl.view.ChannelView;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.mumble.client.interfaces.IResponse;
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
	private IMumbleServer mumbleServer;
	private IChannelList channelList;
	private ObservableList<Object> channels;
	private Map<IChannel, ChannelView> channelViews;

	public ChannelListPresenter(IMumbleServer mumbleServer) {
		this.mumbleServer = mumbleServer;
		channels = FXCollections.observableArrayList();
		channelViews = new HashMap<IChannel, ChannelView>();
		channelList = mumbleServer.getChannelList();

		for (Map.Entry<String, IChannel> entry : channelList)
			channels.add(entry.getValue());

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
	private void onChannelAdded(ChannelAddPostEvent event) {
		if (!event.getChannelList().equals(channelList))
			return;

		dispatch(() -> channels.add(event.getChannel()));
	}

	@EventHandler
	private void onChannelRemoved(ChannelRemovePostEvent event) {
		if (!event.getChannelList().equals(channelList))
			return;

		dispatch(() -> channels.remove(event.getChannel()));
	}

	@EventHandler
	public void onJoinChannel(ChannelJoinRequestPostEvent event) {
		if (mumbleServer.getPlayer().getChannel() != null)
			mumbleServer.getPlayer().getChannel().removePlayer(response -> removePlayer(response));
		event.getCurrentChannel().addPlayer(r -> addPlayer(r));
	}

	@EventHandler
	private void onReachableStatusChanged(ServerReachableChangeEvent event) {
		if (!event.getServer().equals(mumbleServer))
			return;

		if (event.isReachable()) {
			event.getServer().join(response -> manageJoinResponse(response));
			channelList = event.getServer().getChannelList();
		} else {
			dispatch(() -> channels.clear());
		}
	}

	@EventHandler
	private void onServerLeave(ServerLeavePostEvent event) {
		if (!event.getServer().equals(mumbleServer))
			return;

		EventManager.unregisterListener(this);
	}

	private void manageJoinResponse(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, EMessageCode.SERVER_JOIN_FAILED_TITLE, EMessageCode.SERVER_JOIN_FAILED_HEADER);
	}

	private void removePlayer(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, EMessageCode.HANG_UP_FAILED_TITLE, EMessageCode.HANG_UP_FAILED_HEADER);
	}

	private void addPlayer(IResponse response) {
		handleRequestFailed(response, AlertType.INFORMATION, EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL_TITLE,
				EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL);
	}
}
