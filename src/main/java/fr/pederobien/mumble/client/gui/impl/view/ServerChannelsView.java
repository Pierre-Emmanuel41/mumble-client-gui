package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerChannelsPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class ServerChannelsView extends ViewBase<ServerChannelsPresenter, BorderPane> {

	public ServerChannelsView(ServerChannelsPresenter presenter) {
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
