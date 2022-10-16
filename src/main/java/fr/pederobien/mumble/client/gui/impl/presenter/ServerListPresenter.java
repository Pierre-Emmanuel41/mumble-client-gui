package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.SelectedServerChangePostEvent;
import fr.pederobien.mumble.client.gui.event.SelectedServerChangePreEvent;
import fr.pederobien.mumble.client.gui.event.ServerJoinRequestPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerJoinRequestPreEvent;
import fr.pederobien.mumble.client.gui.event.ServerListAddServerPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerListRemoveServerPostEvent;
import fr.pederobien.mumble.client.gui.impl.properties.ListCellView;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.ServerView;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.event.MumbleServerJoinPostEvent;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ServerListPresenter extends PresenterBase implements IEventListener {
	private ServerList serverList;
	private ObservableList<Object> servers;
	private Map<IPlayerMumbleServer, ServerView> serverViews;
	private BooleanProperty emptyServersListVisibilityProperty;
	private SimpleLanguageProperty emptyServerTextProperty;

	public ServerListPresenter(ServerList serverList) {
		this.serverList = serverList;
		EventManager.registerListener(this);
		servers = FXCollections.observableArrayList(serverList.getServers());
		serverViews = new HashMap<IPlayerMumbleServer, ServerView>();

		emptyServersListVisibilityProperty = new SimpleBooleanProperty(serverList.getServers().isEmpty());
		emptyServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.EMPTY_SERVER_LIST);
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
	public StringProperty emptyServerTextProperty() {
		return emptyServerTextProperty;
	}

	/**
	 * @return The property to display the message when there is no registered server.
	 */
	public BooleanProperty emptyServersListVisibilityProperty() {
		return emptyServersListVisibilityProperty;
	}

	public <T> Callback<ListView<T>, ListCell<T>> serverViewFactory(Color enteredColor) {
		return listView -> getPropertyHelper().cellView(item -> {
			ServerView view = serverViews.get(item);
			if (view == null) {
				view = new ServerView(new ServerPresenter((IPlayerMumbleServer) item));
				serverViews.put((IPlayerMumbleServer) item, view);
			}
			return view.getRoot();
		}, enteredColor);
	}

	/**
	 * Action to perform when the selected server has changed.
	 * 
	 * @param oldServer The old selected server.
	 * @param newServer The new selected server.
	 */
	public void onServerSelectedChanged(Object oldServer, Object newServer) {
		SelectedServerChangePreEvent preEvent = new SelectedServerChangePreEvent(serverList, (IPlayerMumbleServer) oldServer, (IPlayerMumbleServer) newServer);
		Runnable update = () -> serverList.setSelectedServer((IPlayerMumbleServer) newServer);
		SelectedServerChangePostEvent postEvent = new SelectedServerChangePostEvent(serverList, (IPlayerMumbleServer) newServer, (IPlayerMumbleServer) oldServer);
		EventManager.callEvent(preEvent, update, postEvent);
	}

	/**
	 * Action to perform when the user double clicked on a server.
	 * 
	 * @param event The event which occurred.
	 */
	public void onDoubleClickOnSelectedServer(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !(event.getTarget() instanceof ListCellView)) {
			ServerJoinRequestPreEvent preEvent = new ServerJoinRequestPreEvent(serverList.getSelectedServer());
			ServerJoinRequestPostEvent postEvent = new ServerJoinRequestPostEvent(serverList.getSelectedServer());
			EventManager.callEvent(preEvent, postEvent);
		}
	}

	@EventHandler
	private void onServerJoin(MumbleServerJoinPostEvent event) {
		EventManager.unregisterListener(this);
	}

	@EventHandler
	private void onServerAdd(ServerListAddServerPostEvent event) {
		if (!event.getServerList().equals(serverList))
			return;

		servers.add(event.getServer());
		emptyServersListVisibilityProperty.setValue(servers.isEmpty());
	}

	@EventHandler
	private void onServerRemove(ServerListRemoveServerPostEvent event) {
		if (!event.getServerList().equals(serverList))
			return;

		servers.remove(event.getServer());
		emptyServersListVisibilityProperty.setValue(servers.isEmpty());
	}
}
