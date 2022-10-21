package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerDetailsPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class ServerDetailsView extends ViewBase<ServerDetailsPresenter, BorderPane> {

	/**
	 * Creates a view in order to display in details the configuration of a server.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public ServerDetailsView(ServerDetailsPresenter presenter) {
		super(presenter, new BorderPane());

		BorderPane left = new BorderPane();
		left.setPrefWidth(300);
		left.setCenter(getPresenter().getChannelListView().getRoot());
		left.setBottom(getPresenter().getPlayerView().getRoot());
		BorderPane.setMargin(getPresenter().getPlayerView().getRoot(), new Insets(5, 0, 20, 10));

		getRoot().setLeft(left);
		BorderPane.setAlignment(left, Pos.CENTER);
	}
}
