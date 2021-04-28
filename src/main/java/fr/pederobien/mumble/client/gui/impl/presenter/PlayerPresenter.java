package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.IOException;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerPresenter extends PresenterBase implements IObsPlayer {
	private IPlayer player;
	private IAudioConnection audioConnection;

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

	public PlayerPresenter(Server server) {
		server.getPlayer(response -> playerResponse(response));
		audioConnection = server.getAudioConnection();

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
		if (channel == null)
			audioConnection.disconnect();
		else {
			player.setMute(false);
			player.setDeafen(false);
			audioConnection.connect();
		}

		playerCanDisconnectFromChannel.setValue(channel != null);
	}

	@Override
	public void onMuteChanged(boolean isMute) {
		updateMuteOrUnmute();
	}

	@Override
	public void onDeafenChanged(boolean isDeafen) {
		updateDeafenOrUndeafen();
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
		updateDeafenOrUndeafen();

		ImageView hangupView = new ImageView(hangupImage);
		hangupView.setPreserveRatio(true);
		hangupView.setFitHeight(fitHeight);
		hangupGraphicProperty.set(hangupView);
	}

	public ObjectProperty<Node> muteOrUnmuteGraphicProperty() {
		return muteOrUnmuteGraphicProperty;
	}

	public ObjectProperty<Tooltip> muteOrUnmuteTooltipProperty() {
		return muteOrUnmuteTooltipProperty;
	}

	public void onMuteOrUnmute() {
		player.setMute(!player.isMute());
	}

	public ObjectProperty<Node> deafenOrUndeafenGraphicProperty() {
		return deafenOrUndeafenGraphicProperty;
	}

	public ObjectProperty<Tooltip> deafenOrUndeafenTooltipProperty() {
		return deafenOrUndeafenTooltipProperty;
	}

	public void onDeafenOrUndeafen() {
		player.setDeafen(!player.isDeafen());
	}

	/**
	 * @return True if the player is connected, false otherwise.
	 */
	public BooleanProperty playerConnectedProperty() {
		return playerConnectedProperty;
	}

	public ObjectProperty<Node> hangupGraphicProperty() {
		return hangupGraphicProperty;
	}

	public ObjectProperty<Tooltip> hangupTooltipProperty() {
		return hangupTooltipProperty;
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
}
