package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.javafx.configuration.impl.GuiHelper;
import fr.pederobien.javafx.configuration.impl.properties.SimpleLanguageProperty;
import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.Images;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter.ErrorPresenterBuilder;
import fr.pederobien.mumble.client.player.event.MumblePlayerAdminChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleServerLeavePostEvent;
import fr.pederobien.mumble.client.player.interfaces.IPlayer;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import fr.pederobien.vocal.client.event.VocalPlayerDeafenStatusChangePostEvent;
import fr.pederobien.vocal.client.event.VocalPlayerMuteStatusChangePostEvent;
import fr.pederobien.vocal.client.event.VocalPlayerSpeakPostEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class PlayerChannelPresenter extends PresenterBase implements IEventListener {
	private IPlayerMumbleServer server;
	private IPlayer player;
	private StringProperty playerNameProperty;
	private BooleanProperty isPlayerMute, isPlayerDeafen;
	private Image muteImage, deafenImage;

	private SimpleLanguageProperty muteOrUnmuteTextProperty;
	private BooleanProperty muteOrUnmuteVisibleProperty;

	private BooleanProperty kickPlayerVisiblity;

	/**
	 * Creates a presenter in order to display the characteristics of the given player registered in a channel.
	 * 
	 * @param player The player associated to this presenter.
	 */
	public PlayerChannelPresenter(IPlayer player) {
		this.player = player;
		this.server = player.getServer();

		playerNameProperty = new SimpleStringProperty(player.getName());
		isPlayerMute = new SimpleBooleanProperty(player.isMute());
		isPlayerDeafen = new SimpleBooleanProperty(player.isDeafen());

		try {
			muteImage = GuiHelper.loadImage(Images.MICROPHONE_OFF.getName());
			deafenImage = GuiHelper.loadImage(Images.HEADSET_OFF.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		muteOrUnmuteTextProperty = getPropertyHelper().newLanguageProperty(EGuiCode.MUTE_TOOLTIP);
		muteOrUnmuteVisibleProperty = new SimpleBooleanProperty(!isMainPlayer());
		kickPlayerVisiblity = new SimpleBooleanProperty(!isMainPlayer() && server.getMainPlayer().isAdmin());

		EventManager.registerListener(this);
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
		player.setMute(!player.isMute(), response -> onPlayerMuteOrUnmuteResponse(response));
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
		player.kick(response -> handleKickPlayerResponse(response));
	}

	private void onPlayerMuteOrUnmuteResponse(IResponse response) {
		if (response.hasFailed())
			dispatch(() -> {
				AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
				alertPresenter.title(EGuiCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE_TITLE, player.getName());
				alertPresenter.header(EGuiCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE, player.getName());
				alertPresenter.content(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
				alertPresenter.getAlert().showAndWait();
			});
		else
			muteOrUnmuteTextProperty.setCode(player.isMute() ? EGuiCode.UNMUTE_TOOLTIP : EGuiCode.MUTE_TOOLTIP);
	}

	@EventHandler
	private void onAdminStatusChanged(MumblePlayerAdminChangePostEvent event) {
		if (!event.getPlayer().equals(server.getMainPlayer()))
			return;

		dispatch(() -> kickPlayerVisiblity.set(!isMainPlayer() && server.getMainPlayer().isAdmin()));
	}

	@EventHandler
	private void onMuteChanged(VocalPlayerMuteStatusChangePostEvent event) {
		if (!event.getPlayer().getName().equals(player.getName()))
			return;

		dispatch(() -> isPlayerMute.set(event.getPlayer().isMute()));
	}

	@EventHandler
	private void onDeafenChanged(VocalPlayerDeafenStatusChangePostEvent event) {
		if (!event.getPlayer().getName().equals(player.getName()))
			return;

		dispatch(() -> isPlayerDeafen.set(event.getPlayer().isDeafen()));
	}

	@EventHandler
	private void onPlayerSpeak(VocalPlayerSpeakPostEvent event) {
		if (!event.getPlayer().getName().equals(player.getName()))
			return;
	}

	@EventHandler
	private void onServerLeave(MumbleServerLeavePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		EventManager.unregisterListener(this);
	}

	private void handleKickPlayerResponse(IResponse response) {
		ErrorPresenterBuilder builder = ErrorPresenterBuilder.of(AlertType.ERROR);
		builder.title(EGuiCode.CANNOT_KICK_PLAYER_RESPONSE_TITLE, player.getName());
		builder.header(EGuiCode.CANNOT_KICK_PLAYER_RESPONSE, player.getName());
		builder.error(response);
		builder.showAndWait();
	}

	private boolean isMainPlayer() {
		return server.getMainPlayer().getName().equals(player.getName());
	}
}
