package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.fxstyle.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.model.Server;
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

	public PlayerPresenter(Server server) {
		server.getPlayer(response -> managedResponse(response));

		playerNameProperty = new SimpleStringProperty("");
		playerStatusProperty = getPropertyHelper().languageProperty(EMessageCode.PLAYER_OFFLINE);
		playerConnectedProperty = new SimpleBooleanProperty(false);
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

	private void managedResponse(IResponse<IPlayer> response) {
		this.player = response.get();
		player.addObserver(this);
		onConnectionStatusChanged(player.isOnline());
	}

	private IMessageCode getPlayerStatusCode() {
		return player.isOnline() ? EMessageCode.PLAYER_ONLINE : EMessageCode.PLAYER_OFFLINE;
	}
}
