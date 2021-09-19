package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.event.ChannelAddPostEvent;
import fr.pederobien.mumble.client.event.ChannelNameChangePostEvent;
import fr.pederobien.mumble.client.event.ChannelRemovePostEvent;
import fr.pederobien.mumble.client.event.PlayerAddToChannelPostEvent;
import fr.pederobien.mumble.client.event.PlayerAdminStatusChangeEvent;
import fr.pederobien.mumble.client.event.PlayerRemoveFromChannelPostEvent;
import fr.pederobien.mumble.client.event.ServerLeavePostEvent;
import fr.pederobien.mumble.client.event.SoundModifierNameChangePostEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.AddChannelView;
import fr.pederobien.mumble.client.gui.impl.view.PlayerChannelView;
import fr.pederobien.mumble.client.gui.impl.view.RenameChannelView;
import fr.pederobien.mumble.client.gui.impl.view.SoundModifierView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsChannelPresenter;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
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

public class ChannelPresenter extends PresenterBase implements IEventListener, IObservable<IObsChannelPresenter> {
	private IMumbleServer mumbleServer;
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

	private SimpleLanguageProperty soundModifierTextProperty;
	private BooleanProperty soundModifierVisibility;

	public ChannelPresenter(IMumbleServer mumbleServer, IChannel channel) {
		this.mumbleServer = mumbleServer;
		this.channelList = mumbleServer.getChannelList();
		this.channel = channel;

		EventManager.registerListener(this);

		observers = new Observable<IObsChannelPresenter>();
		players = FXCollections.observableArrayList(channel.getPlayers().values());
		channelNameProperty = new SimpleStringProperty(channel.getName());

		addChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL);
		addChannelVisibility = new SimpleBooleanProperty(mumbleServer.getPlayer().isAdmin());

		removeChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.REMOVE_CHANNEL);
		removeChannelVisibility = new SimpleBooleanProperty(mumbleServer.getPlayer().isAdmin() && channelList.getChannels().size() > 1);

		renameChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.RENAME_CHANNEL);
		renameChannelVisibility = new SimpleBooleanProperty(mumbleServer.getPlayer().isAdmin());

		soundModifierTextProperty = getPropertyHelper().languageProperty(EMessageCode.SOUND_MODIFIER, channel.getSoundModifier().getName());
		soundModifierVisibility = new SimpleBooleanProperty(mumbleServer.getPlayer().isAdmin());
	}

	@Override
	public void addObserver(IObsChannelPresenter obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsChannelPresenter obs) {
		observers.removeObserver(obs);
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
		return listView -> getPropertyHelper().cellView(item -> new PlayerChannelView(new PlayerChannelPresenter(mumbleServer, (IOtherPlayer) item)).getRoot(),
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
		alertPresenter.title(EMessageCode.REMOVE_CHANNEL_TITLE, channel.getName()).header(EMessageCode.REMOVE_CHANNEL_CONFIRMATION, channel.getName());
		alertPresenter.content(EMessageCode.REMOVE_CHANNEL_EXPLANATION).getAlert().showAndWait().ifPresent(buttonType -> {
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

	/**
	 * @return The property that display "Sound modifier (channel modifier name)".
	 */
	public StringProperty soundModifierTextProperty() {
		return soundModifierTextProperty;
	}

	/**
	 * @return The property for the visibility of the "Sound modifier" component.
	 */
	public BooleanProperty soundModifierVisibility() {
		return soundModifierVisibility;
	}

	public void onSetSoundModifier() {
		new SoundModifierView(getPrimaryStage(), new SoundModifierPresenter(channel));
	}

	@EventHandler
	private void onSoundModifierNameChange(SoundModifierNameChangePostEvent event) {
		if (!event.getSoundModifier().equals(channel.getSoundModifier()))
			return;

		dispatch(() -> soundModifierTextProperty.setCode(EMessageCode.SOUND_MODIFIER, event.getSoundModifier().getName()));
	}

	@EventHandler
	private void onChannelRename(ChannelNameChangePostEvent event) {
		if (!event.getChannel().equals(channel))
			return;

		dispatch(() -> channelNameProperty.set(event.getChannel().getName()));
	}

	@EventHandler
	private void onPlayerAdded(PlayerAddToChannelPostEvent event) {
		if (!event.getChannel().equals(channel))
			return;

		dispatch(() -> players.add(event.getPlayer()));
	}

	@EventHandler
	private void onPlayerRemoved(PlayerRemoveFromChannelPostEvent event) {
		if (!event.getChannel().equals(channel))
			return;

		dispatch(() -> players.remove(event.getPlayer()));
	}

	@EventHandler
	private void onChannelAdded(ChannelAddPostEvent event) {
		if (!event.getChannelList().equals(channelList))
			return;

		removeChannelVisibility.set(channelList.getChannels().size() > 1);
	}

	@EventHandler
	private void onChannelRemoved(ChannelRemovePostEvent event) {
		if (!event.getChannelList().equals(channelList))
			return;

		removeChannelVisibility.set(channelList.getChannels().size() > 1);
	}

	@EventHandler
	private void onAdminStatusChanged(PlayerAdminStatusChangeEvent event) {
		addChannelVisibility.set(event.isAdmin());
		removeChannelVisibility.set(event.isAdmin() && channelList.getChannels().size() > 1);
		renameChannelVisibility.set(event.isAdmin());
		soundModifierVisibility.set(event.isAdmin());
	}

	@EventHandler
	private void onServerLeave(ServerLeavePostEvent event) {
		if (!event.getServer().equals(mumbleServer))
			return;

		EventManager.unregisterListener(this);
	}

	private void channelRemoveResponse(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR,
				p -> p.title(EMessageCode.REMOVE_CHANNEL_TITLE, channel.getName()).header(EMessageCode.REMOVE_CHANNEL_RESPONSE, channel.getName()));
	}
}
