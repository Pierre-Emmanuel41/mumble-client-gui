package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerChannelPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class PlayerChannelView extends ViewBase<PlayerChannelPresenter, BorderPane> {

	public PlayerChannelView(PlayerChannelPresenter presenter) {
		super(presenter, new BorderPane());

		Label playerName = new Label();
		playerName.textProperty().bind(getPresenter().playerNameProperty());
		getRoot().setLeft(playerName);

		HBox playerStatus = new HBox();

		ImageView muteView = new ImageView(getPresenter().muteImage());
		muteView.visibleProperty().bind(getPresenter().isPlayerMute());
		muteView.managedProperty().bind(getPresenter().isPlayerMute());
		muteView.setPreserveRatio(true);
		muteView.setFitHeight(15);
		playerStatus.getChildren().add(muteView);
		HBox.setMargin(muteView, new Insets(0, 5, 0, 0));

		ImageView deafenView = new ImageView(getPresenter().deafenImage());
		deafenView.visibleProperty().bind(getPresenter().isPlayerDeafen());
		deafenView.managedProperty().bind(getPresenter().isPlayerDeafen());
		deafenView.setPreserveRatio(true);
		deafenView.setFitHeight(15);
		playerStatus.getChildren().add(deafenView);
		HBox.setMargin(deafenView, new Insets(0, 0, 0, 5));

		getRoot().setRight(playerStatus);
	}
}
