package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	private static final Map<String, PlayerChannelPresenter> PRESENTERS = new HashMap<String, PlayerChannelPresenter>();

	private PlayerPresenter playerPresenter;
	private StringProperty playerNameProperty;
	private BooleanProperty isPlayerMute;
	private Image muteImage;

	public static PlayerChannelPresenter getOrCreatePlayerPresenter(PlayerPresenter playerPresenter, IOtherPlayer player) {
		PlayerChannelPresenter presenter = PRESENTERS.get(player.getName());
		if (presenter != null)
			return presenter;

		presenter = new PlayerChannelPresenter(playerPresenter, player);
		PRESENTERS.put(player.getName(), presenter);
		return presenter;
	}

	private PlayerChannelPresenter(PlayerPresenter playerPresenter, IOtherPlayer player) {
		this.playerPresenter = playerPresenter;
		playerNameProperty = new SimpleStringProperty(player.getName());
		isPlayerMute = new SimpleBooleanProperty(false);

		try {
			muteImage = Environments.loadImage(Variables.MICROPHONE_OFF.getFileName());
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

	/**
	 * @return The property that display the player name.
	 */
	public StringProperty playerNameProperty() {
		return playerNameProperty;
	}

	public Image getMuteImage() {
		return muteImage;
	}

	public BooleanProperty isPlayerMute() {
		return isPlayerMute;
	}
}
