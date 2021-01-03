package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, ScrollPane> {
	private Label emptyServerListLabel;

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new ScrollPane());

		getRoot().setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		getRoot().setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		StackPane root = new StackPane();
		root.setPrefSize(1500, 500);

		getRoot().setContent(root);

		emptyServerListLabel = new Label();
		emptyServerListLabel.textProperty().bind(getPresenter().emptyServersListLanguageProperty());
		emptyServerListLabel.fontProperty().bind(getPresenter().emptyServersListFontProperty());

		if (getPresenter().getServers().isEmpty())
			root.getChildren().add(emptyServerListLabel);
	}
}
