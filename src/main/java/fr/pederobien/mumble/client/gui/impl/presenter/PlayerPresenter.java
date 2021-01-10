package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerPresenter extends PresenterBase {
	private static final Map<String, PlayerPresenter> PRESENTERS = new HashMap<String, PlayerPresenter>();

	private StringProperty playerNameProperty;

	public static PlayerPresenter getOrCreatePlayerPresenter(String player) {
		PlayerPresenter presenter = PRESENTERS.get(player);
		if (presenter != null)
			return presenter;

		presenter = new PlayerPresenter(player);
		PRESENTERS.put(player, presenter);
		return presenter;
	}

	private PlayerPresenter(String player) {
		playerNameProperty = new SimpleStringProperty(player);
	}

	/**
	 * @return The property that display the player name.
	 */
	public StringProperty playerNameProperty() {
		return playerNameProperty;
	}
}
