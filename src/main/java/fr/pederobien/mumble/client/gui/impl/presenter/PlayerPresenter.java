package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.interfaces.IAudioConnection;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerPresenter extends PresenterBase implements IObsPlayer {
	private IPlayer player;
	private IAudioConnection audioConnection;

	private StringProperty playerNameProperty;
	private SimpleLanguageProperty playerStatusProperty;
	private SimpleLanguageProperty disconnectFromChannelTextProperty;
	private SimpleLanguageProperty disconnectFromServerTextProperty;
	private BooleanProperty playerConnectedProperty;
	private BooleanProperty playerCanDisconnectFromChannel;

	private boolean isMute;
	private double fitHeight;
	private Image unmuteImage, muteImage;
	private ObjectProperty<Node> muteOrUnmuteGraphicProperty;

	public PlayerPresenter(Server server) {
		server.getPlayer(response -> playerResponse(response));
		audioConnection = server.getAudioConnection();

		playerNameProperty = new SimpleStringProperty("");
		playerStatusProperty = getPropertyHelper().languageProperty(EMessageCode.PLAYER_OFFLINE);
		disconnectFromChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.DISCONNECT_FROM_CHANNEL);
		disconnectFromServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.DISCONNECT_FROM_CHANNEL);
		playerConnectedProperty = new SimpleBooleanProperty(false);
		playerCanDisconnectFromChannel = new SimpleBooleanProperty(false);

		isMute = false;
		try {
			unmuteImage = Environments.loadImage(Variables.MICROPHONE_UNMUTE.getFileName());
			muteImage = Environments.loadImage(Variables.MICROPHONE_MUTE.getFileName());
			muteOrUnmuteGraphicProperty = new SimpleObjectProperty<Node>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionStatusChanged(boolean isOnline) {
		dispatch(() -> {
			playerNameProperty.setValue(player.getName());
			playerStatusProperty.setCode(getPlayerStatusCode());
			playerConnectedProperty.setValue(isOnline);
		});
	}

	@Override
	public void onAdminStatusChanged(boolean isAdmin) {

	}

	@Override
	public void onChannelChanged(IChannel channel) {
		playerCanDisconnectFromChannel.setValue(channel != null);
		if (channel == null)
			audioConnection.disconnect();
		else
			audioConnection.connect();
		isMute = false;
		updateMuteOrUnmute();
	}

	@Override
	public void onMuteChanged(boolean isMute) {
		this.isMute = isMute;
		updateMuteOrUnmute();
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

	public void setFitHeight(double fitHeight) {
		this.fitHeight = fitHeight;
		updateMuteOrUnmute();
	}

	public ObjectProperty<Node> muteOrUnmuteGraphicProperty() {
		return muteOrUnmuteGraphicProperty;
	}

	public void onMuteOrUnmute() {
		isMute = !isMute;
		if (isMute)
			audioConnection.pauseMicrophone();
		else
			audioConnection.resumeMicrophone();
	}

	/**
	 * @return True if the player is connected, false otherwise.
	 */
	public BooleanProperty playerConnectedProperty() {
		return playerConnectedProperty;
	}

	public StringProperty disconnectFromChannelTextProperty() {
		return disconnectFromChannelTextProperty;
	}

	/**
	 * Disconnect the player from its channel.
	 */
	public void disconnectFromChannel() {
		player.getChannel().removePlayer(response -> removePlayerResponse(response));
	}

	/**
	 * @return True if the player is registered in a channel, false otherwise.
	 */
	public BooleanProperty playerCanDisconnectFromChannelProperty() {
		return playerCanDisconnectFromChannel;
	}

	public StringProperty disconnectFromServerTextProperty() {
		return disconnectFromServerTextProperty;
	}

	/**
	 * @return The client mumble player.
	 */
	public IPlayer getPlayer() {
		return player;
	}

	public void disconnectFromServer() {
		if (player.getChannel() != null)
			disconnectFromChannel();
		getPrimaryStage().getScene().setRoot(new MainView(new MainPresenter()).getRoot());
	}

	private void playerResponse(IResponse<IPlayer> response) {
		this.player = response.get();
		player.addObserver(this);
		onConnectionStatusChanged(player.isOnline());
	}

	private IMessageCode getPlayerStatusCode() {
		return player.isOnline() ? EMessageCode.PLAYER_ONLINE : EMessageCode.PLAYER_OFFLINE;
	}

	private void removePlayerResponse(IResponse<PlayerRemovedFromChannelEvent> response) {
		if (response.hasFailed())
			System.out.println(response.getErrorCode().getMessage());
	}

	private void updateMuteOrUnmute() {
		ImageView imageView = new ImageView(isMute ? muteImage : unmuteImage);
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(fitHeight);
		dispatch(() -> muteOrUnmuteGraphicProperty.set(imageView));
	}
}
