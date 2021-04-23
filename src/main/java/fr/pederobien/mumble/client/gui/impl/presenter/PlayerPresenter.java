package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.interfaces.IAudioConnection;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IPlayer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.observers.IObsPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerPresenter extends PresenterBase implements IObsPlayer {
	private IPlayer player;
	private IAudioConnection audioConnection;

	private StringProperty playerNameProperty;
	private SimpleLanguageProperty playerStatusProperty;
	private SimpleLanguageProperty disconnectFromChannelTextProperty;
	private SimpleLanguageProperty disconnectFromServerTextProperty;
	private BooleanProperty playerConnectedProperty;

	private BooleanProperty playerCanDisconnectFromChannel;

	public PlayerPresenter(Server server) {
		server.getPlayer(response -> playerResponse(response));
		audioConnection = server.getAudioConnection();

		playerNameProperty = new SimpleStringProperty("");
		playerStatusProperty = getPropertyHelper().languageProperty(EMessageCode.PLAYER_OFFLINE);
		disconnectFromChannelTextProperty = getPropertyHelper().languageProperty(EMessageCode.DISCONNECT_FROM_CHANNEL);
		disconnectFromServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.DISCONNECT_FROM_CHANNEL);
		playerConnectedProperty = new SimpleBooleanProperty(false);

		playerCanDisconnectFromChannel = new SimpleBooleanProperty(false);
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
}
