package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class ServerView extends ViewBase<ServerPresenter, FlowPane> {
	private Label serverNameLabel;
	private Label serverReachableLabel;

	public ServerView(ServerPresenter presenter) {
		super(presenter, new FlowPane());

		serverNameLabel = new Label();
		serverNameLabel.textProperty().bind(getPresenter().serverNameProperty());
		serverNameLabel.fontProperty().bind(getPresenter().fontProperty());

		serverReachableLabel = new Label();
		serverReachableLabel.textProperty().bind(getPresenter().serverStatusProperty());
		serverReachableLabel.textFillProperty().bind(getPresenter().textFillProperty());
		serverReachableLabel.fontProperty().bind(getPresenter().fontProperty());

		getRoot().getChildren().add(serverNameLabel);
	}
}
