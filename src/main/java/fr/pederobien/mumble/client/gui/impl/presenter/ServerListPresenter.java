package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ServerView;
import fr.pederobien.mumble.client.gui.interfaces.IObsServerList;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.properties.SimpleFontProperty;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ServerListPresenter extends PresenterBase implements IObsServerList {
	private ObservableList<Object> servers;
	private SimpleLanguageProperty emptyServersListLanguageProperty;
	private SimpleFontProperty emptyServersListFontProperty;
	private BooleanProperty emptyServersListVisibilityProperty;
	private Server selectedServer;

	public ServerListPresenter(Stage primaryStage, ServerList serverList) {
		super(primaryStage);

		serverList.addObserver(this);
		servers = FXCollections.observableArrayList(serverList.getServers());
		emptyServersListLanguageProperty = createLanguageProperty(EMessageCode.EMPTY_SERVER_LIST);
		emptyServersListFontProperty = createFontProperty();
		emptyServersListVisibilityProperty = new SimpleBooleanProperty(serverList.getServers().isEmpty());
	}

	@Override
	public void onServerAdded(Server server) {
		servers.add(server);
		emptyServersListVisibilityProperty.setValue(servers.isEmpty());
	}

	@Override
	public void onServerRemoved(Server server) {
		servers.remove(server);
		emptyServersListVisibilityProperty.setValue(servers.isEmpty());
	}

	/**
	 * @return An observable list that contains all registered servers.
	 */
	public ObservableList<Object> getServers() {
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

	/**
	 * @return The property to display the message when there is no registered server.
	 */
	public BooleanProperty emptyServersListVisibilityProperty() {
		return emptyServersListVisibilityProperty;
	}

	/**
	 * @return The factory in order to get the view associated to a server.
	 */
	public Callback<ListView<Object>, ListCell<Object>> serverCellFactory() {
		return listView -> {
			return new ListCell<Object>() {

				@Override
				protected void updateItem(Object item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					} else
						setGraphic(new ServerView(new ServerPresenter(getPrimaryStage(), (Server) item)).getRoot());
				}
			};
		};
	}

	/**
	 * Action to perform when the selected server has changed.
	 * 
	 * @param oldServer The old selected server.
	 * @param newServer The new selected server.
	 */
	public void onServerSelectedChanged(Object oldServer, Object newServer) {
		this.selectedServer = (Server) newServer;
		System.out.println("Selected server : " + selectedServer);
	}

	/**
	 * @return The server currently selected in the list.
	 */
	public Server getSelectedServer() {
		return selectedServer;
	}
}
