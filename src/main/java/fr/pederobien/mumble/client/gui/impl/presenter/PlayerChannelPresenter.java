package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.mumble.client.event.OtherPlayerDeafenPostEvent;
import fr.pederobien.mumble.client.event.OtherPlayerMutePostEvent;
import fr.pederobien.mumble.client.event.PlayerAdminStatusChangePostEvent;
import fr.pederobien.mumble.client.event.PlayerSpeakEvent;
import fr.pederobien.mumble.client.event.ServerLeavePostEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class PlayerChannelPresenter extends PresenterBase implements IEventListener {
	private IMumbleServer mumbleServer;
	private IOtherPlayer otherPlayer;
	private StringProperty playerNameProperty;
	private BooleanProperty isPlayerMute, isPlayerDeafen;
	private Image muteImage, deafenImage;

	private SimpleLanguageProperty muteOrUnmuteTextProperty;
	private BooleanProperty muteOrUnmuteVisibleProperty;

	private SimpleLanguageProperty kickPlayerTextProperty;
	private BooleanProperty kickPlayerVisiblity;

	public PlayerChannelPresenter(IMumbleServer mumbleServer, IOtherPlayer otherPlayer) {
		this.mumbleServer = mumbleServer;
		this.otherPlayer = otherPlayer;

		playerNameProperty = new SimpleStringProperty(otherPlayer.getName());
		isPlayerMute = new SimpleBooleanProperty(otherPlayer.isMute());
		isPlayerDeafen = new SimpleBooleanProperty(otherPlayer.isDeafen());

		try {
			muteImage = Environments.loadImage(Variables.MICROPHONE_OFF.getFileName());
			deafenImage = Environments.loadImage(Variables.HEADSET_OFF.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		EventManager.registerListener(this);

		muteOrUnmuteTextProperty = getPropertyHelper().languageProperty(EMessageCode.MUTE_TOOLTIP);
		muteOrUnmuteVisibleProperty = new SimpleBooleanProperty(!isMainPlayer());

		kickPlayerTextProperty = getPropertyHelper().languageProperty(EMessageCode.KICK_PLAYER, otherPlayer.getName());
		kickPlayerVisiblity = new SimpleBooleanProperty(!isMainPlayer() && mumbleServer.getPlayer().isAdmin());
	}

	/**
	 * @return The property that display the player name.
	 */
	public StringProperty playerNameProperty() {
		return playerNameProperty;
	}

	/**
	 * @return The image when the player is mute.
	 */
	public Image muteImage() {
		return muteImage;
	}

	/**
	 * @return The property for the visibility of the {@link #muteImage()} component.
	 */
	public BooleanProperty isPlayerMute() {
		return isPlayerMute;
	}

	/**
	 * @return The image when the player is deafen.
	 */
	public Image deafenImage() {
		return deafenImage;
	}

	/**
	 * @return The property for the visibility of the {@link #deafenImage()} component.
	 */
	public BooleanProperty isPlayerDeafen() {
		return isPlayerDeafen;
	}

	// ContextMenu properties -------------------------------------------------------------

	/**
	 * @return The property the display "Mute" if the player is not mute or "Unmute" if the player is mute.
	 */
	public StringProperty muteOrUnmuteTextProperty() {
		return muteOrUnmuteTextProperty;
	}

	/**
	 * The user can only mute the other player for this view.
	 * 
	 * @return The property for the visibility of the "MuteOrUnmute" component.
	 */
	public BooleanProperty muteOrUnmuteVisibleProperty() {
		return muteOrUnmuteVisibleProperty;
	}

	/**
	 * Action to run when the user click on the "MuteOrUnmute" component. It will send a request to the server. An alert can be
	 * displayed if the request fails.
	 */
	public void onMuteOrUnmute() {
		otherPlayer.setMute(!otherPlayer.isMute(), response -> onPlayerMuteOrUnmuteResponse(response));
	}

	/**
	 * @return The property that display "Kick".
	 */
	public StringProperty kickPlayerTextProperty() {
		return kickPlayerTextProperty;
	}

	/**
	 * The user must be an administrator in game in order to kick another player.
	 * 
	 * @return The property for the visibility of the "Kick" component.
	 */
	public BooleanProperty kickPlayerVisibility() {
		return kickPlayerVisiblity;
	}

	/**
	 * Action to run when the user click on the "Kick" component. It will send a request to the server. An alert can be displayed if
	 * the request fails.
	 */
	public void onKickPlayer() {
		otherPlayer.kick(response -> onKickPlayerResponse(response));
	}

	private void onPlayerMuteOrUnmuteResponse(IResponse response) {
		if (response.hasFailed())
			dispatch(() -> {
				AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
				alertPresenter.title(EMessageCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE_TITLE, otherPlayer.getName());
				alertPresenter.header(EMessageCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE, otherPlayer.getName());
				alertPresenter.content(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
				alertPresenter.getAlert().showAndWait();
			});
		else
			muteOrUnmuteTextProperty.setCode(otherPlayer.isMute() ? EMessageCode.UNMUTE_TOOLTIP : EMessageCode.MUTE_TOOLTIP);
	}

	@EventHandler
	private void onAdminStatusChanged(PlayerAdminStatusChangePostEvent event) {
		if (!event.getPlayer().equals(mumbleServer.getPlayer()))
			return;

		dispatch(() -> kickPlayerVisiblity.set(!isMainPlayer() && mumbleServer.getPlayer().isAdmin()));
	}

	@EventHandler
	private void onMuteChanged(OtherPlayerMutePostEvent event) {
		if (!event.getPlayer().equals(otherPlayer))
			return;

		dispatch(() -> isPlayerMute.set(event.isMute()));
	}

	@EventHandler
	private void onDeafenChanged(OtherPlayerDeafenPostEvent event) {
		if (!event.getPlayer().equals(otherPlayer))
			return;

		dispatch(() -> isPlayerDeafen.set(event.isDeafen()));
	}

	@EventHandler
	private void onPlayerSpeak(PlayerSpeakEvent event) {
		if (!event.getPlayer().equals(otherPlayer))
			return;
	}

	@EventHandler
	private void onServerLeave(ServerLeavePostEvent event) {
		if (!event.getServer().equals(mumbleServer))
			return;

		EventManager.unregisterListener(this);
	}

	private void onKickPlayerResponse(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, p -> p.title(EMessageCode.CANNOT_KICK_PLAYER_RESPONSE_TITLE, otherPlayer.getName())
				.header(EMessageCode.CANNOT_KICK_PLAYER_RESPONSE, otherPlayer.getName()));
	}

	private boolean isMainPlayer() {
		return mumbleServer.getPlayer().getName().equals(otherPlayer.getName());
	}
}
