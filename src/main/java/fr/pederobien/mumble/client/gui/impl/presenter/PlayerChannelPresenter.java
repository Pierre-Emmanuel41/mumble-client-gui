package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerChannelPresenter extends PresenterBase {
	private static final Map<String, PlayerChannelPresenter> PRESENTERS = new HashMap<String, PlayerChannelPresenter>();

	private StringProperty playerNameProperty;

	public static PlayerChannelPresenter getOrCreatePlayerPresenter(String player) {
		PlayerChannelPresenter presenter = PRESENTERS.get(player);
		if (presenter != null)
			return presenter;

		presenter = new PlayerChannelPresenter(player);
		PRESENTERS.put(player, presenter);
		return presenter;
	}

	private PlayerChannelPresenter(String player) {
		playerNameProperty = new SimpleStringProperty(player);
	}

	/**
	 * @return The property that display the player name.
	 */
	public StringProperty playerNameProperty() {
		return playerNameProperty;
	}
}
