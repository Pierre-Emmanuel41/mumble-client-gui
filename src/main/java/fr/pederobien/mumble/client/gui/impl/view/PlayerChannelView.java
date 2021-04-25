package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PlayerChannelPresenter;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class PlayerChannelView extends ViewBase<PlayerChannelPresenter, BorderPane> {

	public PlayerChannelView(PlayerChannelPresenter presenter) {
		super(presenter, new BorderPane());

		Label playerName = new Label();
		playerName.textProperty().bind(getPresenter().playerNameProperty());
		getRoot().setLeft(playerName);

		ImageView muteView = new ImageView(getPresenter().getMuteImage());
		muteView.visibleProperty().bind(getPresenter().isPlayerMute());
		muteView.managedProperty().bind(getPresenter().isPlayerMute());
		muteView.setPreserveRatio(true);
		muteView.setFitHeight(15);
		getRoot().setRight(muteView);
	}
}
