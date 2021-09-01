package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.mumble.client.event.ChannelAddPostEvent;
import fr.pederobien.mumble.client.event.ChannelRemovePostEvent;
import fr.pederobien.mumble.client.event.PlayerAddedToChannelEvent;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.view.ChannelView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsChannelPresenter;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ChannelListPresenter extends PresenterBase implements IEventListener, IObsMumbleServer, IObsChannelPresenter {
	private PlayerPresenter playerPresenter;
	private IMumbleServer server;
	private IChannel selectedChannel;
	private IChannelList channelList;
	private ObservableList<Object> channels;
	private Map<IChannel, ChannelView> channelViews;

	public ChannelListPresenter(PlayerPresenter playerPresenter, IMumbleServer server) {
		this.playerPresenter = playerPresenter;
		this.server = server;
		channels = FXCollections.observableArrayList();
		channelViews = new HashMap<IChannel, ChannelView>();

		EventManager.registerListener(this);

		server.addObserver(this);
		server.getChannels(response -> manageChannelsResponse(response));
	}

	@Override
	public void onNameChanged(IMumbleServer server, String oldName, String newName) {

	}

	@Override
	public void onIpAddressChanged(IMumbleServer server, String oldAddress, String newAddress) {

	}

	@Override
	public void onPortChanged(IMumbleServer server, int oldPort, int newPort) {

	}

	@Override
	public void onReachableStatusChanged(IMumbleServer server, boolean isReachable) {
		if (isReachable) {
			server.join(response -> manageJoinResponse(response));
			server.getChannels(response -> manageChannelsResponse(response));
		} else {
			dispatch(() -> channels.clear());
		}
	}

	@Override
	public void onChannelClicked(IChannel channel) {
		if (selectedChannel == null) {
			selectedChannel = channel;
			selectedChannel.addPlayer(response -> addPlayer(response));
		} else {
			selectedChannel.removePlayer(response -> {
				removePlayer(response);
				selectedChannel = channel;
				selectedChannel.addPlayer(r -> addPlayer(r));
			});
		}
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
			// System.out.println("Item : " + ((IChannel) item).getName());
			ChannelView view = channelViews.get(item);
			if (view == null) {
				ChannelPresenter presenter = new ChannelPresenter(playerPresenter, channelList, (IChannel) item);
				presenter.addObserver(this);
				view = new ChannelView(presenter);
				channelViews.put((IChannel) item, view);
			}
			return view.getRoot();
		}, enteredColor);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onChannelAdded(ChannelAddPostEvent event) {
		dispatch(() -> channels.add(event.getChannel()));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onChannelRemoved(ChannelRemovePostEvent event) {
		dispatch(() -> channels.remove(event.getChannel()));
	}

	private void manageChannelsResponse(IResponse<IChannelList> response) {
		if (response.hasFailed())
			System.out.println(response.getErrorCode().getMessage());
	}

	private void manageJoinResponse(IResponse<Boolean> response) {
		if (response.hasFailed())
			System.out.println(response.getErrorCode().getMessage());
	}

	private void removePlayer(IResponse<PlayerRemovedFromChannelEvent> response) {
		if (response.hasFailed())
			System.out.println(response.getErrorCode().getMessage());
	}

	private void addPlayer(IResponse<PlayerAddedToChannelEvent> response) {
		if (response.hasFailed())
			dispatch(() -> {
				AlertPresenter alertPresenter = new AlertPresenter(AlertType.INFORMATION);
				alertPresenter.setTitle(EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL_TITLE);
				alertPresenter.setHeader(EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL);
				alertPresenter.setContent(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
				alertPresenter.getAlert().showAndWait();
			});
	}
}
