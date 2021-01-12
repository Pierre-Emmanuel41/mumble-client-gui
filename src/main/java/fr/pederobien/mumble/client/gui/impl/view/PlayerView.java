package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PlayerView extends ViewBase<PlayerPresenter, HBox> {

	public PlayerView(PlayerPresenter presenter) {
		super(presenter, new HBox());

		Label playerName = getStyle().createLabel(getPresenter().playerNameProperty());
		playerName.visibleProperty().bind(getPresenter().playerConnectedProperty());
		playerName.managedProperty().bind(getPresenter().playerConnectedProperty());
		getRoot().getChildren().add(playerName);
		HBox.setMargin(playerName, new Insets(0, 5, 0, 0));

		Label playerStatus = getStyle().createLabel(getPresenter().playerStatusProperty());
		getRoot().getChildren().add(playerStatus);
		HBox.setMargin(playerStatus, new Insets(0, 0, 0, 5));
	}
}
