package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.event.ChannelRemovedEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
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
import fr.pederobien.mumble.client.interfaces.IResponse;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

	// Context Menu ----------------------------------------------------------------------------------------------------------------
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
		removeChannelVisibility = new SimpleBooleanProperty(player != null && player.isAdmin() && channelList.getChannels().size() > 1);

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
		removeChannelVisibility.set(player.isAdmin() && channelList.getChannels().size() > 1);
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

	/**
	 * Get the factory that creates player view.
	 * 
	 * @param <T>          The type of object (should be {@link IOtherPlayer}).
	 * @param enteredColor The color when the mouse enters the cell.
	 * 
	 * @return The player view factory in charge to create {@link PlayerChannelView} and {@link PlayerChannelPresenter}.
	 * 
	 * @throws ClassCastException If the type of object used as model to create its view is not IOtherPlayer.
	 */
	public <T> Callback<ListView<T>, ListCell<T>> playerViewFactory(Color enteredColor) {
		return listView -> getPropertyHelper().cellView(item -> new PlayerChannelView(new PlayerChannelPresenter(playerPresenter, (IOtherPlayer) item)).getRoot(),
				enteredColor);
	}

	/**
	 * Action to run when a channel has been selected by the player. If the button used by the user is not the primary button, then
	 * nothing is done otherwise, it notify each observer by calling {@link IObsChannelPresenter#onChannelClicked(IChannel)}.
	 * 
	 * @param event The mouse event used to know if the {@link MouseButton} involved in the event is {@link MouseButton#PRIMARY}.
	 */
	public void onChannelClicked(MouseEvent event) {
		if (event.getButton() != MouseButton.PRIMARY)
			return;

		observers.notifyObservers(obs -> obs.onChannelClicked(channel));
	}

	/**
	 * @return The property that display "Add channel".
	 */
	public StringProperty addChannelTextProperty() {
		return addChannelTextProperty;
	}

	/**
	 * The user can add a channel is and only if he is an administrator in game.
	 * 
	 * @return The property for the visibility of the "add channel" component.
	 */
	public BooleanProperty addChannelVisibility() {
		return addChannelVisibility;
	}

	/**
	 * Action to run when the component "Add channel" has been clicked. It will display a new window ({@link AddChannelView}) in which
	 * the user can write the name of the new channel.
	 */
	public void onAddChannel() {
		new AddChannelView(getPrimaryStage(), new AddChannelPresenter(channelList));
	}

	/**
	 * @return The property that display "Remove".
	 */
	public StringProperty removeChannelTextProperty() {
		return removeChannelTextProperty;
	}

	/**
	 * The user can remove a channel if he is an administrator in game and if there is more than one channel left.
	 * 
	 * @return The property for the visibility of the "Remove" component.
	 */
	public BooleanProperty removeChannelVisibility() {
		return removeChannelVisibility;
	}

	/**
	 * Action to run when the user click on the "Remove" component. It will display an alert box in order to let the user confirm the
	 * deletion.
	 */
	public void onRemoveChannel() {
		AlertPresenter alertPresenter = new AlertPresenter(AlertType.CONFIRMATION);
		alertPresenter.setTitle(EMessageCode.REMOVE_CHANNEL_TITLE, channel.getName());
		alertPresenter.setHeader(EMessageCode.REMOVE_CHANNEL_CONFIRMATION, channel.getName());
		alertPresenter.setContent(EMessageCode.REMOVE_CHANNEL_EXPLANATION);
		alertPresenter.getAlert().showAndWait().ifPresent(buttonType -> {
			if (buttonType != ButtonType.OK)
				return;
			channelList.removeChannel(channel.getName(), response -> channelRemoveResponse(response));
		});
	}

	/**
	 * @return The property that display "Rename channel".
	 */
	public StringProperty renameChannelTextProperty() {
		return renameChannelTextProperty;
	}

	/**
	 * @return The property for the visibility of the "Rename channel" component.
	 */
	public BooleanProperty renameChannelVisibility() {
		return renameChannelVisibility;
	}

	/**
	 * Action to run when the user click on the "Rename channel" component. It will display a new window ({@link RenameChannelView})
	 * in which the user can write the new channel name.
	 */
	public void onRenameChannel() {
		new RenameChannelView(getPrimaryStage(), new RenameChannelPresenter(channelList, channel));
	}

	private void channelRemoveResponse(IResponse<ChannelRemovedEvent> response) {
		if (!response.hasFailed())
			return;

		dispatch(() -> {
			AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
			alertPresenter.setTitle(EMessageCode.REMOVE_CHANNEL_TITLE, channel.getName());
			alertPresenter.setHeader(EMessageCode.REMOVE_CHANNEL_RESPONSE, channel.getName());
			alertPresenter.setContent(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
			alertPresenter.getAlert().show();
		});
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
