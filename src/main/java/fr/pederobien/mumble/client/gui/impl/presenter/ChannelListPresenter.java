package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.function.Function;

import fr.pederobien.mumble.client.event.PlayerAddedToChannelEvent;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ChannelView;
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

public class ChannelListPresenter extends PresenterBase implements IObsChannelList {
	private Server server;
	private IChannel selectedChannel;
	private IChannelList channelList;
	private ObservableList<Object> channels;

	public ChannelListPresenter(Server server) {
		this.server = server;
		channels = FXCollections.observableArrayList();
		server.getChannels(response -> managedResponse(response));
	}

	@Override
	public void onChannelAdded(IChannel channel) {
		channels.add(channel);
	}

	@Override
	public void onChannelRemoved(IChannel channel) {
		channels.remove(channel);
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
		return item -> new ChannelView(ChannelPresenter.getOrCreateServerPresenter((IChannel) item)).getRoot();
	}

	public void onChannelSelectedChanged(Object oldChannel, Object newChannel) {
		if (oldChannel != null)
			selectedChannel.removePlayer(response -> removePlayer(response));
		if (newChannel != null) {
			selectedChannel = (IChannel) newChannel;
			selectedChannel.addPlayer(response -> addPlayer(response));
		}
	}

	private void managedResponse(IResponse<IChannelList> response) {
		if (response.hasFailed()) {
			server.getChannels(r -> managedResponse(r));
			return;
		}

		channelList = response.get();
		channelList.addObserver(this);
		channels.addAll(channelList.getChannels().values());
	}

	private void removePlayer(IResponse<PlayerRemovedFromChannelEvent> response) {
		if (response.hasFailed())
			System.out.println(response.getMessage());
	}

	private void addPlayer(IResponse<PlayerAddedToChannelEvent> response) {
		if (response.hasFailed()) {
			dispatch(() -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.titleProperty().bind(getPropertyHelper().languageProperty(EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL_TITLE));
				alert.headerTextProperty().bind(getPropertyHelper().languageProperty(EMessageCode.PLAYER_SHOULD_BE_CONNECTED_BEFORE_CONNECTION_TO_A_CHANNEL));
				alert.showAndWait();
			});
		}
	}
}
