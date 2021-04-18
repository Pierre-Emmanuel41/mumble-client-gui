package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ServerView extends ViewBase<ServerPresenter, BorderPane> {

	public ServerView(ServerPresenter presenter) {
		super(presenter, new BorderPane());

		VBox vbox = new VBox(2.5);

		Label serverNameLabel = new Label();
		serverNameLabel.textProperty().bind(getPresenter().serverNameProperty());

		Label serverIpAddress = new Label();
		serverIpAddress.textProperty().bind(getPresenter().serverIpAddressProperty());

		vbox.getChildren().addAll(serverNameLabel, serverIpAddress);

		Label serverReachableLabel = new Label();
		serverReachableLabel.textProperty().bind(getPresenter().serverStatusProperty());
		serverReachableLabel.textFillProperty().bind(getPresenter().textFillProperty());

		getRoot().setPadding(new Insets(5, 5, 5, 5));
		getRoot().setLeft(vbox);
		getRoot().setRight(serverReachableLabel);
	}
}
