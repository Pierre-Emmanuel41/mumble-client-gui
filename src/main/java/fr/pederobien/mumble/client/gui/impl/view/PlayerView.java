package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.fxstyle.impl.wrapper.ButtonWrapper;
import fr.pederobien.mumble.client.gui.impl.presenter.PlayerPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerView extends ViewBase<PlayerPresenter, VBox> {

	public PlayerView(PlayerPresenter presenter) {
		super(presenter, new VBox());

		HBox playerInfo = new HBox();
		Label playerName = getStyle().createLabel(getPresenter().playerNameProperty());
		playerName.visibleProperty().bind(getPresenter().playerConnectedProperty());
		playerName.managedProperty().bind(getPresenter().playerConnectedProperty());
		playerInfo.getChildren().add(playerName);
		HBox.setMargin(playerName, new Insets(0, 5, 0, 0));

		Label playerStatus = getStyle().createLabel(getPresenter().playerStatusProperty());
		playerInfo.getChildren().add(playerStatus);
		HBox.setMargin(playerStatus, new Insets(0, 5, 0, 0));

		getRoot().getChildren().add(playerInfo);

		ButtonWrapper disconnectWrapper = getStyle().createButton(getPresenter().disconnectFromChannelCode());
		disconnectWrapper.onAction(e -> getPresenter().disconnect());
		disconnectWrapper.get().visibleProperty().bind(getPresenter().playerCanDisconnectFromChannelProperty());
		getRoot().getChildren().add(disconnectWrapper.get());
		VBox.setMargin(disconnectWrapper.get(), new Insets(5, 5, 0, 0));
	}
}
