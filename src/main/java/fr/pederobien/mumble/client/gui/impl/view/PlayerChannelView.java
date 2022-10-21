package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerChannelPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class PlayerChannelView extends ViewBase<PlayerChannelPresenter, BorderPane> {

	/**
	 * Creates a view in order to display a player registered in a channel. It's mute/deafen status is represented by a mute/deafen
	 * picture.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public PlayerChannelView(PlayerChannelPresenter presenter) {
		super(presenter, new BorderPane());

		Label playerName = new Label();
		playerName.fontProperty().bind(getPresenter().fontProperty());
		playerName.textProperty().bind(getPresenter().playerNameProperty());
		playerName.setTextFill(Color.BLACK);
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

		ContextMenu contextMenu = new ContextMenu();

		// Graphic for muteOrUnmute menuItem
		Label muteOrUnmuteLabel = new Label();
		muteOrUnmuteLabel.fontProperty().bind(getPresenter().fontProperty());
		muteOrUnmuteLabel.textProperty().bind(getPresenter().muteOrUnmuteTextProperty());
		muteOrUnmuteLabel.setTextFill(Color.BLACK);

		MenuItem muteOrUnmute = new MenuItem();
		muteOrUnmute.setGraphic(muteOrUnmuteLabel);
		muteOrUnmute.visibleProperty().bind(getPresenter().muteOrUnmuteVisibleProperty());
		muteOrUnmute.setOnAction(e -> getPresenter().onMuteOrUnmute());
		contextMenu.getItems().add(muteOrUnmute);

		// Graphic for kick menuItem
		Label kickLabel = new Label();
		kickLabel.fontProperty().bind(getPresenter().fontProperty());
		kickLabel.textProperty().bind(getPresenter().kickPlayerTextProperty());
		kickLabel.setTextFill(Color.BLACK);

		MenuItem kick = new MenuItem();
		kick.setGraphic(kickLabel);
		kick.visibleProperty().bind(getPresenter().kickPlayerVisibility());
		kick.setOnAction(e -> getPresenter().onKickPlayer());
		contextMenu.getItems().add(kick);

		getRoot().setOnMouseClicked(e -> displayContextMenu(contextMenu, e));
	}

	private void displayContextMenu(ContextMenu contextMenu, MouseEvent event) {
		if (event.getButton() != MouseButton.SECONDARY)
			return;

		boolean canDisplay = false;
		for (MenuItem menuItem : contextMenu.getItems())
			canDisplay |= menuItem.isVisible();

		if (!canDisplay)
			return;

		contextMenu.show(getRoot(), Side.RIGHT, 0, 0);
	}
}
