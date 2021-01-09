package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());

		Label emptyServerListLabel = getStyle().createLabel(getPresenter().emptyServerCode());
		emptyServerListLabel.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty());
		getRoot().getChildren().add(emptyServerListLabel);

		ListView<Object> listView = new ListView<Object>(getPresenter().getServers());
		listView.setMaxWidth(600);
		listView.setBackground(Background.EMPTY);
		listView.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty().not());
		listView.setCellFactory(getPresenter().serverCellFactory());
		listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> getPresenter().onServerSelectedChanged(oldValue, newValue));
		listView.setOnMouseClicked(e -> getPresenter().onDoubleClickOnSelectedServer(e));
		getRoot().getChildren().add(listView);
	}
}
