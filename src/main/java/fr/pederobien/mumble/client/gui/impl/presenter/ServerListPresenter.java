package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ServerListPresenter extends PresenterBase {
	private ObservableList<Server> servers;

	public ServerListPresenter(Stage primaryStage, ServerList serverList) {
		super(primaryStage);
		servers = FXCollections.observableArrayList(serverList.getServers());
	}

	/**
	 * @return An observable list that contains all registered servers.
	 */
	public ObservableList<Server> getServers() {
		return servers;
	}
}
