package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PlayerChannelPresenter extends PresenterBase implements IObsPlayer {
	private IOtherPlayer player;
	private StringProperty playerNameProperty;
	private BooleanProperty isPlayerMute, isPlayerDeafen;
	private Image muteImage, deafenImage;

	private SimpleLanguageProperty muteOrUnmuteTextProperty;
	private BooleanProperty muteOrUnmuteVisibleProperty;

	public PlayerChannelPresenter(PlayerPresenter playerPresenter, IOtherPlayer player) {
		this.player = player;

		playerNameProperty = new SimpleStringProperty(player.getName());
		isPlayerMute = new SimpleBooleanProperty(player.isMute());
		isPlayerDeafen = new SimpleBooleanProperty(player.isDeafen());
		player.addObserver(this);

		try {
			muteImage = Environments.loadImage(Variables.MICROPHONE_OFF.getFileName());
			deafenImage = Environments.loadImage(Variables.HEADSET_OFF.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		muteOrUnmuteTextProperty = getPropertyHelper().languageProperty(EMessageCode.MUTE_TOOLTIP);
		muteOrUnmuteVisibleProperty = new SimpleBooleanProperty(!playerPresenter.getPlayer().getName().equals(player.getName()));
	}

	@Override
	public void onConnectionStatusChanged(boolean isOnline) {

	}

	@Override
	public void onAdminStatusChanged(boolean isAdmin) {

	}

	@Override
	public void onChannelChanged(IChannel channel) {

	}

	@Override
	public void onMuteChanged(boolean isMute) {
		dispatch(() -> isPlayerMute.set(isMute));
	}

	@Override
	public void onDeafenChanged(boolean isDeafen) {
		dispatch(() -> isPlayerDeafen.set(isDeafen));
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
		player.setMute(!player.isMute(), response -> onPlayerMuteOrUnmuteResponse(response));
	}

	private void onPlayerMuteOrUnmuteResponse(IResponse<Boolean> response) {
		if (response.hasFailed())
			dispatch(() -> {
				AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
				alertPresenter.setTitle(EMessageCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE_TITLE, player.getName());
				alertPresenter.setHeader(EMessageCode.CANNOT_MUTE_OR_UNMUTE_PLAYER_RESPONSE, player.getName());
				alertPresenter.setContent(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
				alertPresenter.getAlert().showAndWait();
			});
		else
			muteOrUnmuteTextProperty.setCode(player.isMute() ? EMessageCode.UNMUTE_TOOLTIP : EMessageCode.MUTE_TOOLTIP);
	}
}
