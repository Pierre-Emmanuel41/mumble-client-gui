package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.event.ChannelJoinRequestPostEvent;
import fr.pederobien.mumble.client.gui.event.ChannelJoinRequestPreEvent;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter.ErrorPresenterBuilder;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.AddChannelView;
import fr.pederobien.mumble.client.gui.impl.view.PlayerChannelView;
import fr.pederobien.mumble.client.gui.impl.view.RenameChannelView;
import fr.pederobien.mumble.client.gui.impl.view.SoundModifierView;
import fr.pederobien.mumble.client.player.event.MumbleChannelListChannelAddPostEvent;
import fr.pederobien.mumble.client.player.event.MumbleChannelListChannelRemovePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleChannelNameChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleChannelPlayerListPlayerAddPostEvent;
import fr.pederobien.mumble.client.player.event.MumbleChannelPlayerListPlayerRemovePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleChannelSoundModifierChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumblePlayerAdminChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleServerLeavePostEvent;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.mumble.client.player.interfaces.IChannelList;
import fr.pederobien.mumble.client.player.interfaces.IPlayer;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
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

public class ChannelPresenter extends PresenterBase implements IEventListener {
	private IPlayerMumbleServer server;
	private IChannelList channelList;
	private IChannel channel;
	private ObservableList<Object> players;
	private Map<String, PlayerChannelView> playersViews;
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

	/**
	 * Creates a presenter in order to display the characteristics of the given channel.
	 * 
	 * @param channel The channel to display.
	 */
	public ChannelPresenter(IChannel channel) {
		this.channel = channel;
		this.server = channel.getServer();
		this.channelList = server.getChannels();

		EventManager.registerListener(this);

		players = FXCollections.observableArrayList(channel.getPlayers().toList());
		playersViews = new HashMap<String, PlayerChannelView>();

		channelNameProperty = new SimpleStringProperty(channel.getName());

		addChannelTextProperty = getPropertyHelper().languageProperty(EGuiCode.ADD_CHANNEL);
		addChannelVisibility = new SimpleBooleanProperty(server.getMainPlayer().isAdmin());

		removeChannelTextProperty = getPropertyHelper().languageProperty(EGuiCode.REMOVE_CHANNEL);
		removeChannelVisibility = new SimpleBooleanProperty(server.getMainPlayer().isAdmin() && channelList.toList().size() > 1);

		renameChannelTextProperty = getPropertyHelper().languageProperty(EGuiCode.RENAME_CHANNEL);
		renameChannelVisibility = new SimpleBooleanProperty(server.getMainPlayer().isAdmin());

		soundModifierTextProperty = getPropertyHelper().languageProperty(EGuiCode.SOUND_MODIFIER, channel.getSoundModifier().getName());
		soundModifierVisibility = new SimpleBooleanProperty(server.getMainPlayer().isAdmin());
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
		return listView -> getPropertyHelper().cellView(item -> {
			PlayerChannelView view = playersViews.get(((IPlayer) item).getName());
			if (view == null) {
				view = new PlayerChannelView(new PlayerChannelPresenter((IPlayer) item));
				playersViews.put(((IPlayer) item).getName(), view);
			}
			return view.getRoot();
		}, enteredColor);
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

		ChannelJoinRequestPreEvent preEvent = new ChannelJoinRequestPreEvent(server, server.getMainPlayer().getChannel(), channel);
		ChannelJoinRequestPostEvent postEvent = new ChannelJoinRequestPostEvent(server, channel, server.getMainPlayer().getChannel());
		EventManager.callEvent(preEvent, postEvent);
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
		new AddChannelView(new AddChannelPresenter(channelList));
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
		if (channel.getPlayers().toList().size() == 0)
			channelList.remove(channel.getName(), response -> handleRemoveChannelResponse(response));
		else {
			AlertPresenter alertPresenter = new AlertPresenter(AlertType.CONFIRMATION);
			alertPresenter.title(EGuiCode.REMOVE_CHANNEL_TITLE, channel.getName()).header(EGuiCode.REMOVE_CHANNEL_CONFIRMATION, channel.getName());
			alertPresenter.content(EGuiCode.REMOVE_CHANNEL_EXPLANATION).getAlert().showAndWait().ifPresent(buttonType -> {
				if (buttonType != ButtonType.OK)
					return;
				channelList.remove(channel.getName(), response -> handleRemoveChannelResponse(response));
			});
		}
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
		new RenameChannelView(new RenameChannelPresenter(channelList, channel));
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
		new SoundModifierView(new SoundModifierPresenter(channel));
	}

	@EventHandler
	private void onChannelSoundModifierChange(MumbleChannelSoundModifierChangePostEvent event) {
		if (!event.getChannel().equals(channel))
			return;

		dispatch(() -> soundModifierTextProperty.setCode(EGuiCode.SOUND_MODIFIER, event.getChannel().getSoundModifier().getName()));
	}

	@EventHandler
	private void onChannelRename(MumbleChannelNameChangePostEvent event) {
		if (!event.getChannel().equals(channel))
			return;

		dispatch(() -> channelNameProperty.set(event.getChannel().getName()));
	}

	@EventHandler
	private void onPlayerAdded(MumbleChannelPlayerListPlayerAddPostEvent event) {
		if (!event.getList().getChannel().equals(channel))
			return;

		dispatch(() -> players.add(event.getPlayer()));
	}

	@EventHandler
	private void onPlayerRemoved(MumbleChannelPlayerListPlayerRemovePostEvent event) {
		if (!event.getList().getChannel().equals(channel))
			return;

		dispatch(() -> players.remove(event.getPlayer()));
	}

	@EventHandler
	private void onChannelAdded(MumbleChannelListChannelAddPostEvent event) {
		if (!event.getList().equals(channelList))
			return;

		removeChannelVisibility.set(channelList.toList().size() > 1);
	}

	@EventHandler
	private void onChannelRemoved(MumbleChannelListChannelRemovePostEvent event) {
		if (!event.getList().equals(channelList))
			return;

		removeChannelVisibility.set(channelList.toList().size() > 1);
	}

	@EventHandler
	private void onAdminStatusChanged(MumblePlayerAdminChangePostEvent event) {
		addChannelVisibility.set(event.getPlayer().isAdmin());
		removeChannelVisibility.set(event.getPlayer().isAdmin() && channelList.toList().size() > 1);
		renameChannelVisibility.set(event.getPlayer().isAdmin());
		soundModifierVisibility.set(event.getPlayer().isAdmin());
	}

	@EventHandler
	private void onServerLeave(MumbleServerLeavePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		EventManager.unregisterListener(this);
	}

	private void handleRemoveChannelResponse(IResponse response) {
		ErrorPresenterBuilder builder = ErrorPresenterBuilder.of(AlertType.ERROR);
		builder.title(EGuiCode.REMOVE_CHANNEL_TITLE, channel.getName());
		builder.header(EGuiCode.REMOVE_CHANNEL_RESPONSE, channel.getName());
		builder.error(response);
		builder.showAndWait();
	}
}
