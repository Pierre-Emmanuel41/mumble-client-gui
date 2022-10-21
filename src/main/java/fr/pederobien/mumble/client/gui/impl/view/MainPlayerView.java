package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.MainPlayerPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainPlayerView extends ViewBase<MainPlayerPresenter, VBox> {

	/**
	 * Creates a view in order to display the details of the main player of a server.
	 * 
	 * @param presenter the presenter associated to this view.
	 */
	public MainPlayerView(MainPlayerPresenter presenter) {
		super(presenter, new VBox());

		HBox playerInfo = new HBox();
		playerInfo.setAlignment(Pos.CENTER_LEFT);

		Label playerName = new Label();
		playerName.fontProperty().bind(getPresenter().fontProperty());
		playerName.textProperty().bind(getPresenter().playerNameProperty());
		playerName.visibleProperty().bind(getPresenter().playerConnectedProperty());
		playerName.managedProperty().bind(getPresenter().playerConnectedProperty());
		playerInfo.getChildren().add(playerName);
		HBox.setMargin(playerName, new Insets(0, 5, 0, 0));

		Label playerStatus = new Label();
		playerStatus.fontProperty().bind(getPresenter().fontProperty());
		playerStatus.textProperty().bind(getPresenter().playerStatusProperty());
		playerInfo.getChildren().add(playerStatus);
		HBox.setMargin(playerStatus, new Insets(0, 5, 0, 0));

		// Setting image size
		getPresenter().setFitHeight(20);

		Button muteOrUnmuteButton = new Button();
		muteOrUnmuteButton.fontProperty().bind(getPresenter().fontProperty());
		muteOrUnmuteButton.setBackground(Background.EMPTY);
		muteOrUnmuteButton.graphicProperty().bind(getPresenter().muteOrUnmuteGraphicProperty());
		muteOrUnmuteButton.setOnAction(e -> getPresenter().onMuteOrUnmute());
		muteOrUnmuteButton.visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		muteOrUnmuteButton.tooltipProperty().bind(getPresenter().muteOrUnmuteTooltipProperty());
		playerInfo.getChildren().add(muteOrUnmuteButton);
		HBox.setMargin(muteOrUnmuteButton, new Insets(0, 5, 0, 0));

		Button deafenOrUndeafenButton = new Button();
		deafenOrUndeafenButton.fontProperty().bind(getPresenter().fontProperty());
		deafenOrUndeafenButton.setBackground(Background.EMPTY);
		deafenOrUndeafenButton.graphicProperty().bind(getPresenter().deafenOrUndeafenGraphicProperty());
		deafenOrUndeafenButton.setOnAction(e -> getPresenter().onDeafenOrUndeafen());
		deafenOrUndeafenButton.visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		deafenOrUndeafenButton.tooltipProperty().bind(getPresenter().deafenOrUndeafenTooltipProperty());
		playerInfo.getChildren().add(deafenOrUndeafenButton);
		HBox.setMargin(deafenOrUndeafenButton, new Insets(0, 5, 0, 0));

		Button hangupButton = new Button();
		hangupButton.fontProperty().bind(getPresenter().fontProperty());
		hangupButton.setBackground(Background.EMPTY);
		hangupButton.graphicProperty().bind(getPresenter().hangupGraphicProperty());
		hangupButton.setOnAction(e -> getPresenter().disconnectFromChannel());
		hangupButton.visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		hangupButton.tooltipProperty().bind(getPresenter().hangupTooltipProperty());
		playerInfo.getChildren().add(hangupButton);

		getRoot().getChildren().add(playerInfo);

		Button disconnectFromServerButton = new Button();
		disconnectFromServerButton.fontProperty().bind(getPresenter().fontProperty());
		disconnectFromServerButton.textProperty().bind(getPresenter().disconnectFromServerTextProperty());
		disconnectFromServerButton.setOnAction(e -> getPresenter().disconnectFromServer());
		getRoot().getChildren().add(disconnectFromServerButton);
		VBox.setMargin(disconnectFromServerButton, new Insets(5, 5, 0, 0));
	}
}
