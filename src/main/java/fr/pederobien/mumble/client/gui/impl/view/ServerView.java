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

		Label serverNameLabel = getStyle().createLabel(getPresenter().serverNameProperty());

		Label serverIpAddress = getStyle().createLabel(getPresenter().serverIpAddressProperty());

		vbox.getChildren().addAll(serverNameLabel, serverIpAddress);

		Label serverReachableLabel = getStyle().createLabel(getPresenter().serverStatusProperty());
		serverReachableLabel.textFillProperty().bind(getPresenter().textFillProperty());

		getRoot().setPadding(new Insets(5, 5, 5, 5));
		getRoot().setLeft(vbox);
		getRoot().setRight(serverReachableLabel);
	}
}
