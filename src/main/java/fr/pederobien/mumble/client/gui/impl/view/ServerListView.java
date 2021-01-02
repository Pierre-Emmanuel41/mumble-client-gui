package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {
	private Label emptyServerListLabel;

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());

		getRoot().setPrefHeight(500);
		getRoot().setPrefWidth(1500);

		emptyServerListLabel = new Label();
		emptyServerListLabel.textProperty().bind(getPresenter().emptyServersListProperty());

		if (getPresenter().getServers().isEmpty())
			getRoot().getChildren().add(emptyServerListLabel);
	}
}
