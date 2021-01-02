package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.interfaces.IObsServerList;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.properties.SimpleFontProperty;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ServerListPresenter extends PresenterBase implements IObsServerList {
	private ObservableList<Server> servers;
	private SimpleLanguageProperty emptyServersListLanguageProperty;
	private SimpleFontProperty emptyServersListFontProperty;

	public ServerListPresenter(Stage primaryStage, ServerList serverList) {
		super(primaryStage);

		serverList.addObserver(this);
		servers = FXCollections.observableArrayList(serverList.getServers());
		emptyServersListLanguageProperty = createLanguageProperty(EMessageCode.EMPTY_SERVER_LIST);
		emptyServersListFontProperty = createFontProperty();
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

	/**
	 * @return The message to display when there is no registered server.
	 */
	public StringProperty emptyServersListLanguageProperty() {
		return emptyServersListLanguageProperty;
	}

	/**
	 * @return The font to display the message when there is no registered server.
	 */
	public ObjectProperty<Font> emptyServersListFontProperty() {
		return emptyServersListFontProperty;
	}
}
