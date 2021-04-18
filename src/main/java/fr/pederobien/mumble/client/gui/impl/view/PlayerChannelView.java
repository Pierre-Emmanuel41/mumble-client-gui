package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerChannelPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PlayerChannelView extends ViewBase<PlayerChannelPresenter, GridPane> {

	public PlayerChannelView(PlayerChannelPresenter presenter) {
		super(presenter, new GridPane());

		Label playerName = new Label();
		playerName.textProperty().bind(getPresenter().playerNameProperty());
		getRoot().add(playerName, 0, 0);
	}
}
