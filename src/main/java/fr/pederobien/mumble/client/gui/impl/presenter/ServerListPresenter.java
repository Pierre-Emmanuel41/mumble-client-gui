package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ListCellView;
import fr.pederobien.mumble.client.gui.impl.view.ServerView;
import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServerList;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsServerListPresenter;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ServerListPresenter extends PresenterBase implements IObsServerList, IObservable<IObsServerListPresenter> {
	private ObservableList<Object> servers;
	private BooleanProperty emptyServersListVisibilityProperty;
	private Server selectedServer;
	private Observable<IObsServerListPresenter> observers;

	public ServerListPresenter(ServerList serverList) {
		serverList.addObserver(this);
		servers = FXCollections.observableArrayList(serverList.getServers());

		emptyServersListVisibilityProperty = new SimpleBooleanProperty(serverList.getServers().isEmpty());
		observers = new Observable<IObsServerListPresenter>();
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

	@Override
	public void addObserver(IObsServerListPresenter obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsServerListPresenter obs) {
		observers.removeObserver(obs);
	}

	/**
	 * @return An observable list that contains all registered servers.
	 */
	public ObservableList<Object> getServers() {
		return servers;
	}

	/**
	 * @return The code associated to the message to display when there is no registered server.
	 */
	public IMessageCode emptyServerCode() {
		return EMessageCode.EMPTY_SERVER_LIST;
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
			return new ListCellView<Object>(item -> new ServerView(ServerPresenter.getOrCreateServerPresenter((Server) item)).getRoot());
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
		observers.notifyObservers(obs -> obs.onSelectedServerChanged((Server) oldServer, (Server) newServer));
	}

	/**
	 * @return The server currently selected in the list.
	 */
	public Server getSelectedServer() {
		return selectedServer;
	}

	/**
	 * Action to perform when the user double clicked on a server.
	 * 
	 * @param event The event which occurred.
	 */
	public void onDoubleClickOnSelectedServer(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
			observers.notifyObservers(obs -> obs.onDoubleClickOnServer(selectedServer));
	}
}
