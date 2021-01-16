package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.fxstyle.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.event.PlayerRemovedFromChannelEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.model.Server;
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

	private StringProperty playerNameProperty;
	private SimpleLanguageProperty playerStatusProperty;
	private BooleanProperty playerConnectedProperty;

	private BooleanProperty playerCanDisconnectFromChannel;

	public PlayerPresenter(Server server) {
		server.getPlayer(response -> playerResponse(response));

		playerNameProperty = new SimpleStringProperty("");
		playerStatusProperty = getPropertyHelper().languageProperty(EMessageCode.PLAYER_OFFLINE);
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

	/**
	 * @return The code associated to the message to display in order to disconnect the player from its channel.
	 */
	public IMessageCode disconnectFromChannelCode() {
		return EMessageCode.DISCONNECT_FROM_CHANNEL;
	}

	/**
	 * Disconnect the player from its channel.
	 */
	public void disconnect() {
		player.getChannel().removePlayer(response -> removePlayerResponse(response));
	}

	/**
	 * @return True if the player is registered in a channel, false otherwise.
	 */
	public BooleanProperty playerCanDisconnectFromChannelProperty() {
		return playerCanDisconnectFromChannel;
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
