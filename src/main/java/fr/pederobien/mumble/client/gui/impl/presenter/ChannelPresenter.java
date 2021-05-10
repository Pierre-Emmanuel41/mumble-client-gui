package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.AddChannelView;
import fr.pederobien.mumble.client.gui.impl.view.PlayerChannelView;
import fr.pederobien.mumble.client.gui.impl.view.RenameChannelView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsChannelPresenter;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsPlayerPresenter;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.IPlayer;
import fr.pederobien.mumble.client.interfaces.observers.IObsChannel;
import fr.pederobien.mumble.client.interfaces.observers.IObsPlayer;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ChannelPresenter extends PresenterBase implements IObsChannel, IObservable<IObsChannelPresenter>, IObsPlayerPresenter {
	private PlayerPresenter playerPresenter;
	private IChannelList channelList;
	private IChannel channel;
	private Observable<IObsChannelPresenter> observers;

	private ObservableList<Object> players;
	private StringProperty channelNameProperty;

	private SimpleLanguageProperty addChannelTextProperty;
	private BooleanProperty addChannelVisibility;

	private SimpleLanguageProperty removeChannelTextProperty;
	private BooleanProperty removeChannelVisibility;

	private SimpleLanguageProperty renameChannelTextProperty;
	private BooleanProperty renameChannelVisibility;

	public ChannelPresenter(PlayerPresenter playerPresenter, IChannelList channelList, IChannel channel) {
		this.playerPresenter = playerPresenter;
		this.channelList = channelList;
		this.channel = channel;
		this.channel.addObserver(this);

		// In order to be notified when the player is defined.
		IPlayer player = playerPresenter.getPlayer();
		if (player == null)
			playerPresenter.addObserver(this);
		else
			player.addObserver(new InternalObsPlayer());

		observers = new Observable<IObsChannelPresenter>();
		players = FXCollections.observableArrayList(channel.getPlayers());
		channelNameProperty = new SimpleStringProperty(channel.getName());

		addChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL);
		addChannelVisibility = new SimpleBooleanProperty(player != null && player.isAdmin());

		removeChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.REMOVE_CHANNEL);
		removeChannelVisibility = new SimpleBooleanProperty(player != null && player.isAdmin());

		renameChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.RENAME_CHANNEL);
		renameChannelVisibility = new SimpleBooleanProperty(player != null && player.isAdmin());
	}

	@Override
	public void addObserver(IObsChannelPresenter obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsChannelPresenter obs) {
		observers.removeObserver(obs);
	}

	@Override
	public void onChannelRename(IChannel channel, String oldName, String newName) {
		dispatch(() -> channelNameProperty.setValue(newName));
	}

	@Override
	public void onPlayerAdded(IChannel channel, IOtherPlayer player) {
		dispatch(() -> players.add(player));
	}

	@Override
	public void onPlayerRemoved(IChannel channel, IOtherPlayer player) {
		dispatch(() -> players.remove(player));
	}

	@Override
	public void onPlayerDefined(IPlayer player) {
		// Observing this player in order to perform gui update when the player admin status changes.
		player.addObserver(new InternalObsPlayer());
		addChannelVisibility.set(player.isAdmin());
		removeChannelVisibility.set(player.isAdmin());
		renameChannelVisibility.set(player.isAdmin());
	}

	/**
	 * @return An observable list that contains all registered players.
	 */
	public ObservableList<Object> getPlayers() {
		return players;
	}

	/**
	 * @return The property that display the channel name.
	 */
	public StringProperty channelNameProperty() {
		return channelNameProperty;
	}

	public <T> Callback<ListView<T>, ListCell<T>> playerViewFactory(Color enteredColor) {
		return listView -> getPropertyHelper().cellView(item -> new PlayerChannelView(new PlayerChannelPresenter(playerPresenter, (IOtherPlayer) item)).getRoot(),
				enteredColor);
	}

	public void onChannelClicked(MouseEvent e) {
		if (e.getButton() != MouseButton.PRIMARY)
			return;

		observers.notifyObservers(obs -> obs.onChannelClicked(channel));
	}

	public StringProperty addChannelTextProperty() {
		return addChannelTextProperty;
	}

	public BooleanProperty addChannelVisibility() {
		return addChannelVisibility;
	}

	public void onAddChannel() {
		new AddChannelView(getPrimaryStage(), new AddChannelPresenter(channelList));
	}

	public StringProperty removeChannelTextProperty() {
		return removeChannelTextProperty;
	}

	public BooleanProperty removeChannelVisibility() {
		return removeChannelVisibility;
	}

	public void onRemoveChannel() {

	}

	public StringProperty renameChannelTextProperty() {
		return renameChannelTextProperty;
	}

	public BooleanProperty renameChannelVisibility() {
		return renameChannelVisibility;
	}

	public void onRenameChannel() {
		new RenameChannelView(getPrimaryStage(), new RenameChannelPresenter(channelList, channel));
	}

	private class InternalObsPlayer implements IObsPlayer {

		@Override
		public void onMuteChanged(boolean isMute) {

		}

		@Override
		public void onDeafenChanged(boolean isDeafen) {

		}

		@Override
		public void onConnectionStatusChanged(boolean isOnline) {

		}

		@Override
		public void onAdminStatusChanged(boolean isAdmin) {
			addChannelVisibility.set(isAdmin);
			removeChannelVisibility.set(isAdmin);
			renameChannelVisibility.set(isAdmin);
		}

		@Override
		public void onChannelChanged(IChannel channel) {

		}
	}
}
