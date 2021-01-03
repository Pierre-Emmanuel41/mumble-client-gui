package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ServerView extends ViewBase<ServerPresenter, BorderPane> {
	private Label serverNameLabel;
	private Label serverIpAddress;
	private Label serverReachableLabel;

	public ServerView(ServerPresenter presenter) {
		super(presenter, new BorderPane());

		VBox vbox = new VBox(2.5);

		serverNameLabel = new Label();
		serverNameLabel.textProperty().bind(getPresenter().serverNameProperty());
		serverNameLabel.fontProperty().bind(getPresenter().fontProperty());

		serverIpAddress = new Label();
		serverIpAddress.textProperty().bind(getPresenter().serverIpAddressProperty());
		serverIpAddress.fontProperty().bind(getPresenter().fontProperty());

		vbox.getChildren().addAll(serverNameLabel, serverIpAddress);

		serverReachableLabel = new Label();
		serverReachableLabel.textProperty().bind(getPresenter().serverStatusProperty());
		serverReachableLabel.textFillProperty().bind(getPresenter().textFillProperty());
		serverReachableLabel.fontProperty().bind(getPresenter().fontProperty());

		getRoot().setPadding(new Insets(5, 5, 5, 5));
		getRoot().setLeft(vbox);
		getRoot().setRight(serverReachableLabel);
	}
}
