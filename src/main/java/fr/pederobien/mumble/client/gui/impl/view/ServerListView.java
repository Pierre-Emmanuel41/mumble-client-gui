package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());
	}
}
