package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PlayerView extends ViewBase<PlayerPresenter, GridPane> {

	public PlayerView(PlayerPresenter presenter) {
		super(presenter, new GridPane());

		Label playerName = getStyle().createLabel(getPresenter().playerNameProperty());
		getRoot().add(playerName, 0, 0);
	}
}
