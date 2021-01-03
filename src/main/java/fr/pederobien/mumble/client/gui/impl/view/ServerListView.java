package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.FlowPane;

public class ServerListView extends ViewBase<ServerListPresenter, ScrollPane> {
	private Label emptyServerListLabel;

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new ScrollPane());

		getRoot().setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		getRoot().setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		FlowPane root = new FlowPane();
		getRoot().setContent(root);

		emptyServerListLabel = new Label();
		emptyServerListLabel.textProperty().bind(getPresenter().emptyServersListLanguageProperty());
		emptyServerListLabel.fontProperty().bind(getPresenter().emptyServersListFontProperty());
		emptyServerListLabel.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty());

		if (getPresenter().getServers().isEmpty())
			root.getChildren().add(emptyServerListLabel);

		ListView<Object> listView = new ListView<Object>(getPresenter().getServers());
		listView.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty().not());
		listView.setCellFactory(getPresenter().serverCellFactory());
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> getPresenter().onServerSelectedChanged(oldValue, newValue));

		root.getChildren().add(listView);
	}
}
