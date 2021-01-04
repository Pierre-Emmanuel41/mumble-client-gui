package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());

		Label emptyServerListLabel = new Label();
		emptyServerListLabel.textProperty().bind(getPresenter().emptyServersListLanguageProperty());
		emptyServerListLabel.fontProperty().bind(getPresenter().emptyServersListFontProperty());
		emptyServerListLabel.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty());
		getRoot().getChildren().add(emptyServerListLabel);

		ListView<Object> listView = new ListView<Object>(getPresenter().getServers());
		listView.setMaxWidth(600);
		listView.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty().not());
		listView.setCellFactory(getPresenter().serverCellFactory());
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> getPresenter().onServerSelectedChanged(oldValue, newValue));
		getRoot().getChildren().add(listView);
	}
}
