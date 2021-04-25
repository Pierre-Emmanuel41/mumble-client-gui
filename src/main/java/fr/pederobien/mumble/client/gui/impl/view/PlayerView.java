package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerView extends ViewBase<PlayerPresenter, VBox> {

	public PlayerView(PlayerPresenter presenter) {
		super(presenter, new VBox());

		HBox playerInfo = new HBox();
		playerInfo.setAlignment(Pos.CENTER_LEFT);

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

		// Setting image size
		getPresenter().setFitHeight(20);

		Button muteOrUnmuteButton = new Button();
		muteOrUnmuteButton.setBackground(Background.EMPTY);
		muteOrUnmuteButton.graphicProperty().bind(getPresenter().muteOrUnmuteGraphicProperty());
		muteOrUnmuteButton.setOnAction(e -> getPresenter().onMuteOrUnmute());
		muteOrUnmuteButton.visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		muteOrUnmuteButton.tooltipProperty().bind(getPresenter().muteOrUnmuteTooltipProperty());
		playerInfo.getChildren().add(muteOrUnmuteButton);
		HBox.setMargin(muteOrUnmuteButton, new Insets(0, 5, 0, 0));

		Button hangupButton = new Button();
		hangupButton.setBackground(Background.EMPTY);
		hangupButton.graphicProperty().bind(getPresenter().hangupGraphicProperty());
		hangupButton.setOnAction(e -> getPresenter().disconnectFromChannel());
		hangupButton.visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		hangupButton.tooltipProperty().bind(getPresenter().hangupTooltipProperty());
		playerInfo.getChildren().add(hangupButton);

		getRoot().getChildren().add(playerInfo);

		Button disconnectFromServerButton = new Button();
		disconnectFromServerButton.textProperty().bind(getPresenter().disconnectFromServerTextProperty());
		disconnectFromServerButton.setOnAction(e -> getPresenter().disconnectFromServer());
		getRoot().getChildren().add(disconnectFromServerButton);
		VBox.setMargin(disconnectFromServerButton, new Insets(5, 5, 0, 0));
	}
}
