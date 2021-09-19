package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.event.PlayerChannelChangePostEvent;
import fr.pederobien.mumble.client.event.PlayerDeafenChangePostEvent;
import fr.pederobien.mumble.client.event.PlayerMuteChangePostEvent;
import fr.pederobien.mumble.client.event.PlayerOnlineStatusChangeEvent;
import fr.pederobien.mumble.client.event.ServerLeavePostEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.mumble.client.interfaces.IPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerPresenter extends PresenterBase implements IEventListener {
	private IMumbleServer server;
	private IPlayer player;

	private StringProperty playerNameProperty;
	private SimpleLanguageProperty playerStatusProperty;
	private SimpleLanguageProperty disconnectFromServerTextProperty;
	private BooleanProperty playerConnectedProperty;
	private BooleanProperty playerCanDisconnectFromChannel;

	private SimpleTooltipProperty muteOrUnmuteTooltipProperty;
	private SimpleTooltipProperty deafenOrUndeafenTooltipProperty;
	private SimpleTooltipProperty hangupTooltipProperty;

	private double fitHeight;
	private Image unmuteImage, muteImage, deafenImage, undeafenImage, hangupImage;
	private ObjectProperty<Node> muteOrUnmuteGraphicProperty;
	private ObjectProperty<Node> deafenOrUndeafenGraphicProperty;
	private ObjectProperty<Node> hangupGraphicProperty;

	public PlayerPresenter(IMumbleServer server) {
		this.server = server;

		EventManager.registerListener(this);

		player = server.getPlayer();

		playerNameProperty = new SimpleStringProperty("");
		playerStatusProperty = getPropertyHelper().languageProperty(EMessageCode.PLAYER_OFFLINE);
		disconnectFromServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.DISCONNECT_FROM_SERVER);
		playerConnectedProperty = new SimpleBooleanProperty(false);
		playerCanDisconnectFromChannel = new SimpleBooleanProperty(false);

		muteOrUnmuteTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.MUTE_TOOLTIP);
		deafenOrUndeafenTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.DEAFEN_TOOLTIP);
		hangupTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.HANG_UP_TOOLTIP);

		try {
			unmuteImage = Environments.loadImage(Variables.MICROPHONE_UNMUTE.getFileName());
			muteImage = Environments.loadImage(Variables.MICROPHONE_MUTE.getFileName());
			muteOrUnmuteGraphicProperty = new SimpleObjectProperty<Node>();
			deafenImage = Environments.loadImage(Variables.HEADSET_DEAFEN.getFileName());
			undeafenImage = Environments.loadImage(Variables.HEADSET_UNDEAFEN.getFileName());
			deafenOrUndeafenGraphicProperty = new SimpleObjectProperty<Node>();
			hangupImage = Environments.loadImage(Variables.HANG_UP.getFileName());
			hangupGraphicProperty = new SimpleObjectProperty<Node>();
		} catch (IOException e) {
			e.printStackTrace();
		}

		updatePlayerProperties(player.isOnline());
	}

	/**
	 * @return The property that displays the player name.
	 */
	public StringProperty playerNameProperty() {
		return playerNameProperty;
	}

	/**
	 * @return The property that display the player connection status.
	 */
	public StringProperty playerStatusProperty() {
		return playerStatusProperty;
	}

	/**
	 * The height of the bounding box within which the mute/unmute and deafen/undeafen images are resized as necessary to fit. If set
	 * to a value <= 0, then the intrinsic height of the image will be used as the {@code fitHeight}.
	 *
	 * @param fitHeight the fitHeight of deafen, undeafen, mute and unmute images.
	 */
	public void setFitHeight(double fitHeight) {
		this.fitHeight = fitHeight;
		updateMuteOrUnmute();
		updateDeafenOrUndeafen();

		ImageView hangupView = new ImageView(hangupImage);
		hangupView.setPreserveRatio(true);
		hangupView.setFitHeight(fitHeight);
		hangupGraphicProperty.set(hangupView);
	}

	/**
	 * @return The object property that contains an image that represent the mute state of the player. If the player is not mute then
	 *         the image is a mic, otherwise the image is a barred mic.
	 */
	public ObjectProperty<Node> muteOrUnmuteGraphicProperty() {
		return muteOrUnmuteGraphicProperty;
	}

	/**
	 * @return The object property that contains the tooltip of the component that mute or unmute a player. The text is automatically
	 *         updated when the player is mute or unmute.
	 */
	public ObjectProperty<Tooltip> muteOrUnmuteTooltipProperty() {
		return muteOrUnmuteTooltipProperty;
	}

	/**
	 * Change the mute state of the player. If the player was previously mute then calling this method unmute it. If the player was
	 * previously unmute then calling this method mute it.
	 */
	public void onMuteOrUnmute() {
		player.setMute(!player.isMute());
	}

	/**
	 * @return The object property that contains an image that represent the deafen state of the player. If the player is not deafen
	 *         then the image is a headset, otherwise the image is a barred headset.
	 */
	public ObjectProperty<Node> deafenOrUndeafenGraphicProperty() {
		return deafenOrUndeafenGraphicProperty;
	}

	/**
	 * @return The object property that contains the tooltip of the component that deafen or undeafen a player. The text is
	 *         automatically updated when the player is deafen or undeafen.
	 */
	public ObjectProperty<Tooltip> deafenOrUndeafenTooltipProperty() {
		return deafenOrUndeafenTooltipProperty;
	}

	/**
	 * Change the deafen state of the player. If the player was previously deafen then calling this method undeafen it. If the player
	 * was previously undeafen then calling this method deafen it.
	 */
	public void onDeafenOrUndeafen() {
		player.setDeafen(!player.isDeafen());
	}

	/**
	 * @return True if the player is connected, false otherwise.
	 */
	public BooleanProperty playerConnectedProperty() {
		return playerConnectedProperty;
	}

	/**
	 * @return The object property that contains an image that represent a barred phone in order to quit the channel.
	 */
	public ObjectProperty<Node> hangupGraphicProperty() {
		return hangupGraphicProperty;
	}

	/**
	 * @return The object property that contains the tooltip of the component that allow the player to leave a channel.
	 */
	public ObjectProperty<Tooltip> hangupTooltipProperty() {
		return hangupTooltipProperty;
	}

	/**
	 * Disconnect the player from its channel.
	 */
	public void disconnectFromChannel() {
		player.getChannel().removePlayer(response -> removePlayer(response));
	}

	/**
	 * @return True if the player is registered in a channel, false otherwise.
	 */
	public BooleanProperty playerCanDisconnectFromChannelProperty() {
		return playerCanDisconnectFromChannel;
	}

	/**
	 * @return The text to display on the component that allow the player to leave the server.
	 */
	public StringProperty disconnectFromServerTextProperty() {
		return disconnectFromServerTextProperty;
	}

	/**
	 * @return The client mumble player.
	 */
	public IPlayer getPlayer() {
		return player;
	}

	/**
	 * Disconnect the player from the server. Calling this method send a request to the server and also update the user interface.
	 */
	public void disconnectFromServer() {
		server.leave(response -> manageServerLeaveResponse(response));
	}

	@EventHandler
	private void onPlayerOnlineStatusChange(PlayerOnlineStatusChangeEvent event) {
		if (!event.getPlayer().equals(player))
			return;

		updatePlayerProperties(event.isOnline());
	}

	@EventHandler
	private void onChannelChanged(PlayerChannelChangePostEvent event) {
		if (!event.getPlayer().equals(player))
			return;

		playerCanDisconnectFromChannel.setValue(event.getPlayer().getChannel() != null);
	}

	@EventHandler
	private void onMuteChanged(PlayerMuteChangePostEvent event) {
		if (!event.getPlayer().equals(player))
			return;

		updateMuteOrUnmute();
	}

	@EventHandler
	private void onDeafenChanged(PlayerDeafenChangePostEvent event) {
		if (!event.getPlayer().equals(player))
			return;

		updateDeafenOrUndeafen();
	}

	@EventHandler
	private void onServerLeave(ServerLeavePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		EventManager.unregisterListener(this);
		getPrimaryStage().getScene().setRoot(new MainView(new MainPresenter()).getRoot());
	}

	private IMessageCode getPlayerStatusCode() {
		return player.isOnline() ? EMessageCode.PLAYER_ONLINE : EMessageCode.PLAYER_OFFLINE;
	}

	private void removePlayer(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, EMessageCode.HANG_UP_FAILED_TITLE, EMessageCode.HANG_UP_FAILED_HEADER);
	}

	private void updateMuteOrUnmute() {
		boolean isMute = player == null ? false : player.isMute();
		ImageView imageView = new ImageView(isMute ? muteImage : unmuteImage);
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(fitHeight);
		dispatch(() -> {
			muteOrUnmuteGraphicProperty.set(imageView);
			muteOrUnmuteTooltipProperty.setMessageCode(isMute ? EMessageCode.UNMUTE_TOOLTIP : EMessageCode.MUTE_TOOLTIP);
		});
	}

	private void updateDeafenOrUndeafen() {
		boolean isDeafen = player == null ? false : player.isDeafen();
		ImageView imageView = new ImageView(isDeafen ? deafenImage : undeafenImage);
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(fitHeight);
		dispatch(() -> {
			deafenOrUndeafenGraphicProperty.set(imageView);
			deafenOrUndeafenTooltipProperty.setMessageCode(isDeafen ? EMessageCode.UNDEAFEN_TOOLTIP : EMessageCode.DEAFEN_TOOLTIP);
		});
	}

	private void updatePlayerProperties(boolean isOnline) {
		dispatch(() -> {
			playerNameProperty.setValue(player.getName());
			playerStatusProperty.setCode(getPlayerStatusCode());
			playerConnectedProperty.setValue(isOnline);
		});
	}

	private void manageServerLeaveResponse(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, p -> p.title(EMessageCode.CANNOT_LEAVE_SERVER_RESPONSE_TITLE).header(EMessageCode.CANNOT_LEAVE_SERVER_RESPONSE));
	}
}
