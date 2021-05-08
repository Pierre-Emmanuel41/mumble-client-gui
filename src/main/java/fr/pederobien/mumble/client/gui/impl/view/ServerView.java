package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ServerView extends ViewBase<ServerPresenter, BorderPane> {

	public ServerView(ServerPresenter presenter) {
		super(presenter, new BorderPane());

		VBox vbox = new VBox(2.5);

		Label serverNameLabel = new Label();
		serverNameLabel.fontProperty().bind(getPresenter().fontProperty());
		serverNameLabel.textProperty().bind(getPresenter().serverNameProperty());
		serverNameLabel.setTextFill(Color.BLACK);

		Label serverIpAddress = new Label();
		serverIpAddress.fontProperty().bind(getPresenter().fontProperty());
		serverIpAddress.textProperty().bind(getPresenter().serverIpAddressProperty());
		serverIpAddress.setTextFill(Color.BLACK);

		vbox.getChildren().addAll(serverNameLabel, serverIpAddress);

		Label serverReachableLabel = new Label();
		serverReachableLabel.fontProperty().bind(getPresenter().fontProperty());
		serverReachableLabel.textProperty().bind(getPresenter().serverStatusProperty());
		serverReachableLabel.textFillProperty().bind(getPresenter().textFillProperty());

		getRoot().setPadding(new Insets(5, 5, 5, 5));
		getRoot().setLeft(vbox);
		getRoot().setRight(serverReachableLabel);
	}
}
