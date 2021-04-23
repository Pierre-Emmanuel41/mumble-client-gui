package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerView extends ViewBase<PlayerPresenter, VBox> {

	public PlayerView(PlayerPresenter presenter) {
		super(presenter, new VBox());

		HBox playerInfo = new HBox();

		Label playerName = new Label();
		playerName.textProperty().bind(getPresenter().playerNameProperty());
		playerName.visibleProperty().bind(getPresenter().playerConnectedProperty());
		playerName.managedProperty().bind(getPresenter().playerConnectedProperty());
		playerInfo.getChildren().add(playerName);
		HBox.setMargin(playerName, new Insets(0, 5, 0, 0));

		Label playerStatus = new Label();
		playerStatus.textProperty().bind(getPresenter().playerStatusProperty());
		playerInfo.getChildren().add(playerStatus);
		HBox.setMargin(playerStatus, new Insets(0, 5, 0, 0));

		getRoot().getChildren().add(playerInfo);

		Button disconnectButton = new Button();
		disconnectButton.textProperty().bind(getPresenter().disconnectFromChannelTextProperty());
		disconnectButton.setOnAction(e -> getPresenter().disconnectFromChannel());
		disconnectButton.visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		disconnectButton.managedProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		getRoot().getChildren().add(disconnectButton);
		VBox.setMargin(disconnectButton, new Insets(5, 5, 0, 0));

		Button disconnectFromServerButton = new Button();
		disconnectFromServerButton.textProperty().bind(getPresenter().disconnectFromServerTextProperty());
		disconnectFromServerButton.setOnAction(e -> getPresenter().disconnectFromServer());
		getRoot().getChildren().add(disconnectFromServerButton);
		VBox.setMargin(disconnectFromServerButton, new Insets(5, 5, 0, 0));
	}
}
