package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import fr.pederobien.mumble.client.event.PlayerAddedToChannelEvent;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.view.ChannelView;
import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServer;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsChannelPresenter;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsChannelList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ChannelListPresenter extends PresenterBase implements IObsChannelList, IObsServer, IObsChannelPresenter {
	private Server server;
	private IChannel selectedChannel;
	private IChannelList channelList;
	private ObservableList<Object> channels;
	private Map<IChannel, ChannelView> channelViews;

	public ChannelListPresenter(Server server) {
		this.server = server;
		channels = FXCollections.observableArrayList();
		channelViews = new HashMap<IChannel, ChannelView>();

		server.addObserver(this);
		server.getChannels(response -> manageResponse(response));
	}

	@Override
	public void onChannelAdded(IChannel channel) {
		dispatch(() -> channels.add(channel));
	}

	@Override
	public void onChannelRemoved(IChannel channel) {
		dispatch(() -> channels.remove(channel));
	}

	@Override
	public void onNameChanged(Server server, String oldName, String newName) {

	}

	@Override
	public void onIpAddressChanged(Server server, String oldAddress, String newAddress) {

	}

	@Override
	public void onPortChanged(Server server, int oldPort, int newPort) {

	}

	@Override
	public void onReachableStatusChanged(Server server, boolean isReachable) {
		if (isReachable)
			server.getChannels(response -> dispatch(() -> manageResponse(response)));
		else {
			dispatch(() -> {
				channels.clear();
				if (channelList != null)
					channelList.removeObserver(this);
			});
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
	 * @return The channel view constructor.
	 */
	public Function<Object, Parent> channelCellFactory() {
		return item -> {
			ChannelView view = channelViews.get(item);
			if (view == null) {
				ChannelPresenter presenter = new ChannelPresenter((IChannel) item);
				presenter.addObserver(this);
				view = new ChannelView(presenter);
				channelViews.put((IChannel) item, view);
			}
			return view.getRoot();
		};
	}

	private void manageResponse(IResponse<IChannelList> response) {
		if (response.hasFailed()) {
			server.getChannels(r -> dispatch(() -> manageResponse(r)));
			return;
		}

		channelList = response.get();
		channelList.addObserver(this);
		channels.addAll(channelList.getChannels());
	}

	private void removePlayer(IResponse<PlayerRemovedFromChannelEvent> response) {
		if (response.hasFailed())
			System.out.println(response.getErrorCode().getMessage());
	}

	private void addPlayer(IResponse<PlayerAddedToChannelEvent> response) {
		if (response.hasFailed()) {
			dispatch(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.titleProperty().bind(getPropertyHelper().languageProperty(EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL_TITLE));
				alert.headerTextProperty().bind(getPropertyHelper().languageProperty(EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL));
				alert.contentTextProperty().bind(getPropertyHelper().languageProperty(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode()));
				alert.showAndWait();
			});
		}
	}
}