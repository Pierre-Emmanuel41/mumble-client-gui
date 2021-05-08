package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsPlayerPresenter;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.IPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsCommonPlayer;
import fr.pederobien.mumble.client.interfaces.observers.IObsPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PlayerChannelPresenter extends PresenterBase implements IObsPlayerPresenter {
	private PlayerPresenter playerPresenter;
	private IOtherPlayer otherPlayer;
	private StringProperty playerNameProperty;
	private BooleanProperty isPlayerMute, isPlayerDeafen;
	private Image muteImage, deafenImage;

	private SimpleLanguageProperty muteOrUnmuteTextProperty;
	private BooleanProperty muteOrUnmuteVisibleProperty;

	private SimpleLanguageProperty kickPlayerTextProperty;
	private BooleanProperty kickPlayerVisiblity;

	public PlayerChannelPresenter(PlayerPresenter playerPresenter, IOtherPlayer otherPlayer) {
		this.playerPresenter = playerPresenter;
		this.otherPlayer = otherPlayer;

		// In order to be notified when the player is defined.
		IPlayer player = playerPresenter.getPlayer();
		if (player == null)
			playerPresenter.addObserver(this);
		else
			player.addObserver(new InternalPlayerObserver());

		// Observing this other player in order to perform gui update when the player mute and deafen status changes.
		otherPlayer.addObserver(new InternalOtherPlayerObserver());

		playerNameProperty = new SimpleStringProperty(otherPlayer.getName());
		isPlayerMute = new SimpleBooleanProperty(otherPlayer.isMute());
		isPlayerDeafen = new SimpleBooleanProperty(otherPlayer.isDeafen());

		try {
			muteImage = Environments.loadImage(Variables.MICROPHONE_OFF.getFileName());
			deafenImage = Environments.loadImage(Variables.HEADSET_OFF.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		muteOrUnmuteTextProperty = getPropertyHelper().languageProperty(EMessageCode.MUTE_TOOLTIP);
		muteOrUnmuteVisibleProperty = new SimpleBooleanProperty(player != null && player.isAdmin());

		kickPlayerTextProperty = getPropertyHelper().languageProperty(EMessageCode.KICK_PLAYER, player.getName());
		kickPlayerVisiblity = new SimpleBooleanProperty(player != null && player.isAdmin());
	}

	@Override
	public void onPlayerDefined(IPlayer player) {
		// Observing this player in order to perform gui update when the player admin status changes.
		player.addObserver(new InternalPlayerObserver());
		muteOrUnmuteVisibleProperty.set(!playerPresenter.getPlayer().getName().equals(player.getName()));
		kickPlayerVisiblity.set(!playerPresenter.getPlayer().getName().equals(player.getName()) && playerPresenter.getPlayer().isAdmin());
	}

	/**
	 * @return The property that display the player name.
	 */
	public StringProperty playerNameProperty() {
		return playerNameProperty;
	}

	public void onPlayerNameClick(MouseEvent event) {
		if (event.getButton() != MouseButton.SECONDARY)
			return;

	}

	public Image muteImage() {
		return muteImage;
	}

	public BooleanProperty isPlayerMute() {
		return isPlayerMute;
	}

	public Image deafenImage() {
		return deafenImage;
	}

	public BooleanProperty isPlayerDeafen() {
		return isPlayerDeafen;
	}

	// ContextMenu properties -------------------------------------------------------------

	public StringProperty muteOrUnmuteTextProperty() {
		return muteOrUnmuteTextProperty;
	}

	public BooleanProperty muteOrUnmuteVisibleProperty() {
		return muteOrUnmuteVisibleProperty;
	}

	public void onMuteOrUnmute() {
		otherPlayer.setMute(!otherPlayer.isMute(), response -> onPlayerMuteOrUnmuteResponse(response));
	}

	public StringProperty kickPlayerTextProperty() {
		return kickPlayerTextProperty;
	}

	public BooleanProperty kickPlayerVisibility() {
		return kickPlayerVisiblity;
	}

	public void onKickPlayer() {
		otherPlayer.kick(response -> onKickPlayerResponse(response));
	}

	private void onPlayerMuteOrUnmuteResponse(IResponse<Boolean> response) {
		if (response.hasFailed())
			dispatch(() -> {
				AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
				alertPresenter.setTitle(EMessageCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE_TITLE, otherPlayer.getName());
				alertPresenter.setHeader(EMessageCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE, otherPlayer.getName());
				alertPresenter.setContent(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
				alertPresenter.getAlert().showAndWait();
			});
		else
			muteOrUnmuteTextProperty.setCode(otherPlayer.isMute() ? EMessageCode.UNMUTE_TOOLTIP : EMessageCode.MUTE_TOOLTIP);
	}

	private void onKickPlayerResponse(IResponse<Boolean> response) {
		if (!response.hasFailed())
			return;

		dispatch(() -> {
			AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
			alertPresenter.setTitle(EMessageCode.CANNOT_KICK_PLAYER_RESPONSE_TITLE, otherPlayer.getName());
			alertPresenter.setHeader(EMessageCode.CANNOT_KICK_PLAYER_RESPONSE, otherPlayer.getName());
			alertPresenter.setContent(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
			alertPresenter.getAlert().showAndWait();
		});
	}

	private class InternalPlayerObserver implements IObsPlayer {

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
			dispatch(() -> kickPlayerVisiblity.set(!playerPresenter.getPlayer().getName().equals(otherPlayer.getName()) && playerPresenter.getPlayer().isAdmin()));
		}

		@Override
		public void onChannelChanged(IChannel channel) {

		}
	}

	private class InternalOtherPlayerObserver implements IObsCommonPlayer {

		@Override
		public void onMuteChanged(boolean isMute) {
			dispatch(() -> isPlayerMute.set(isMute));
		}

		@Override
		public void onDeafenChanged(boolean isDeafen) {
			dispatch(() -> isPlayerDeafen.set(isDeafen));
		}
	}
}
