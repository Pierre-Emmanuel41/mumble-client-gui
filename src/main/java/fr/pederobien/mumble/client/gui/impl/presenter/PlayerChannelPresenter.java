package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.observers.IObsPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class PlayerChannelPresenter extends PresenterBase implements IObsPlayer {
	private PlayerPresenter playerPresenter;
	private StringProperty playerNameProperty;
	private BooleanProperty isPlayerMute, isPlayerDeafen;
	private Image muteImage, deafenImage;

	public PlayerChannelPresenter(PlayerPresenter playerPresenter, IOtherPlayer player) {
		this.playerPresenter = playerPresenter;
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
}
