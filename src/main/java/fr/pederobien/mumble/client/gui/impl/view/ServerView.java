package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.javafx.configuration.impl.components.SimpleLabel;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerPresenter;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ServerView extends ViewBase<ServerPresenter, BorderPane> {

	/**
	 * Creates a view in order to display the name, IP address, port number and reachable status of a server.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public ServerView(ServerPresenter presenter) {
		super(presenter, new BorderPane());

		VBox vbox = new VBox(2.5);

		SimpleLabel serverNameLabel = new SimpleLabel();
		serverNameLabel.textProperty().bind(getPresenter().serverNameProperty());
		serverNameLabel.setTextFill(Color.BLACK);

		SimpleLabel serverIpAddress = new SimpleLabel();
		serverIpAddress.textProperty().bind(getPresenter().serverIpAddressProperty());
		serverIpAddress.setTextFill(Color.BLACK);

		vbox.getChildren().addAll(serverNameLabel, serverIpAddress);

		SimpleLabel serverReachableLabel = new SimpleLabel();
		serverReachableLabel.textProperty().bind(getPresenter().serverStatusProperty());
		serverReachableLabel.textFillProperty().bind(getPresenter().textFillProperty());

		getRoot().setPadding(new Insets(5, 5, 5, 5));
		getRoot().setLeft(vbox);
		getRoot().setRight(serverReachableLabel);
	}
}
