package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());

		// Label to display when there is no registered server.
		Label emptyServerListLabel = new Label();
		emptyServerListLabel.textProperty().bind(getPresenter().emptyServerTextProperty());
		emptyServerListLabel.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty());
		getRoot().getChildren().add(emptyServerListLabel);

		ListView<Object> serverListView = new ListView<Object>();
		serverListView.setItems(getPresenter().getServers());
		serverListView.setBackground(Background.EMPTY);

		// Visible if the list view is not empty.
		serverListView.visibleProperty().bind(Bindings.greaterThan(Bindings.size(serverListView.getItems()), new SimpleIntegerProperty(0)));
		serverListView.managedProperty().bind(serverListView.visibleProperty());

		serverListView.setOnMouseClicked(e -> getPresenter().onDoubleClickOnSelectedServer(e));
		serverListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> getPresenter().onServerSelectedChanged(oldValue, newValue));
		serverListView.setMaxWidth(600);
		serverListView.setCellFactory(getPresenter().serverViewFactory(Color.web("0x0096c9ff")));

		getRoot().getChildren().add(serverListView);
	}
}
