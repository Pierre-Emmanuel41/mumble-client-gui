package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.interfaces.IObsServerList;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ServerListPresenter extends PresenterBase implements IObsServerList {
	private ObservableList<Server> servers;

	public ServerListPresenter(Stage primaryStage, ServerList serverList) {
		super(primaryStage);
		serverList.addObserver(this);
		servers = FXCollections.observableArrayList(serverList.getServers());
	}

	@Override
	public void onServerAdded(Server server) {
		servers.add(server);
	}

	@Override
	public void onServerRemoved(Server server) {
		servers.remove(server);
	}

	/**
	 * @return An observable list that contains all registered servers.
	 */
	public ObservableList<Server> getServers() {
		return servers;
	}
}
