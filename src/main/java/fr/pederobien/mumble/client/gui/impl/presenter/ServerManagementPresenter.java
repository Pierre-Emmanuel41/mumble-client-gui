package fr.pederobien.mumble.client.gui.impl.presenter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.SelectedServerChangePostEvent;
import fr.pederobien.mumble.client.gui.event.ServerJoinRequestPostEvent;
import fr.pederobien.mumble.client.gui.event.ServerJoinRequestPreEvent;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.ServerDetailsView;
import fr.pederobien.mumble.client.gui.impl.view.ServerInfoView;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.event.MumbleServerJoinPostEvent;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;

public class ServerManagementPresenter extends PresenterBase implements IEventListener {
	// Join server -----------------------------------------------------
	private SimpleLanguageProperty joinServerTextProperty;
	private BooleanProperty joinServerDisableProperty;

	// Add server -----------------------------------------------------
	private SimpleLanguageProperty addServerTextProperty;
	private BooleanProperty addServerDisableProperty;

	// Edit server -----------------------------------------------------
	private SimpleLanguageProperty editServerTextProperty;
	private BooleanProperty editServerDisableProperty;

	// Delete server -----------------------------------------------------
	private SimpleLanguageProperty deleteServerTextProperty;
	private BooleanProperty deleteServerDisableProperty;

	private ServerList serverList;

	/**
	 * Creates a presenter in order to add/remove/modify a server.
	 * 
	 * @param serverList The server list to update.
	 */
	public ServerManagementPresenter(ServerList serverList) {
		this.serverList = serverList;

		joinServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.JOIN_SERVER);
		joinServerDisableProperty = new SimpleBooleanProperty(true);

		addServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_SERVER);
		addServerDisableProperty = new SimpleBooleanProperty(false);

		editServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.EDIT_SERVER);
		editServerDisableProperty = new SimpleBooleanProperty(true);

		deleteServerTextProperty = getPropertyHelper().languageProperty(EMessageCode.DELETE_SERVER);
		deleteServerDisableProperty = new SimpleBooleanProperty(true);

		EventManager.registerListener(this);
	}

	/**
	 * @return The property that display "Join"
	 */
	public StringProperty joinServerTextProperty() {
		return joinServerTextProperty;
	}

	/**
	 * @return The property in order to enable/disable the "join server" functionality.
	 */
	public BooleanProperty joinDisableProperty() {
		return joinServerDisableProperty;
	}

	/**
	 * @return The property that display "Add"
	 */
	public StringProperty addServerTextProperty() {
		return addServerTextProperty;
	}

	/**
	 * @return The property in order to enable/disable the "add server" functionality.
	 */
	public BooleanProperty addDisableProperty() {
		return addServerDisableProperty;
	}

	/**
	 * @return The property that display "Edit"
	 */
	public StringProperty editServerTextProperty() {
		return editServerTextProperty;
	}

	/**
	 * @return The property to enable/disable the "edit server" functionality.
	 */
	public BooleanProperty editDisableProperty() {
		return editServerDisableProperty;
	}

	/**
	 * @return The property that display "Delete"
	 */
	public StringProperty deleteServerTextProperty() {
		return deleteServerTextProperty;
	}

	/**
	 * @return The property to enable/disable the "delete server" functionality.
	 */
	public BooleanProperty deleteDisableProperty() {
		return deleteServerDisableProperty;
	}

	/**
	 * Send a request to the server in order to join the selected server and update the user interface.
	 */
	public void onJoin() {
		if (!serverList.getSelectedServer().isReachable())
			return;

		ServerJoinRequestPreEvent preEvent = new ServerJoinRequestPreEvent(serverList.getSelectedServer());
		ServerJoinRequestPostEvent postEvent = new ServerJoinRequestPostEvent(serverList.getSelectedServer());
		EventManager.callEvent(preEvent, postEvent);
	}

	/**
	 * Creates a new window in which the user can add a new mumble server.
	 */
	public void onAdd() {
		new ServerInfoView(new ServerInfoPresenter(serverList, ServerList.createNewDefaultServer()) {
			@Override
			protected void onOkButtonClicked(IPlayerMumbleServer server, String name, String address, int port) {
				performChangesOnServer(server, name, address, port);
				serverList.add(server);
			}
		});
	}

	/**
	 * Creates a new window in which the user can edit a mumble server.
	 */
	public void onEdit() {
		new ServerInfoView(new ServerInfoPresenter(serverList, serverList.getSelectedServer()) {
			@Override
			protected void onOkButtonClicked(IPlayerMumbleServer server, String name, String address, int port) {
				performChangesOnServer(server, name, address, port);
			}
		});
	}

	/**
	 * Dispose the selected server and removes it from the server list.
	 */
	public void onDelete() {
		serverList.getSelectedServer().close();
		serverList.remove(serverList.getSelectedServer());
	}

	@EventHandler
	private void onServerJoinRequestEvent(ServerJoinRequestPostEvent event) {
		if (!event.getServer().isReachable())
			return;

		event.getServer().join(response -> handleJoinServerResponse(response));
	}

	@EventHandler
	private void onServerJoin(MumbleServerJoinPostEvent event) {
		if (!event.getServer().equals(serverList.getSelectedServer()))
			return;

		dispatch(() -> {
			getPrimaryStage().getScene().setRoot(new ServerDetailsView(new ServerDetailsPresenter(serverList.getSelectedServer())).getRoot());
			serverList.getServers().forEach(server -> {
				if (!server.equals(serverList.getSelectedServer()))
					server.close();
			});
		});
		EventManager.unregisterListener(this);
	}

	@EventHandler
	private void onSelectedServerChange(SelectedServerChangePostEvent event) {
		joinServerDisableProperty.setValue(serverList.getSelectedServer() == null || !serverList.getSelectedServer().isReachable());
		editServerDisableProperty.setValue(serverList.getSelectedServer() == null);
		deleteServerDisableProperty.setValue(serverList.getSelectedServer() == null);
	}

	private void performChangesOnServer(IPlayerMumbleServer server, String name, String address, int port) {
		server.setName(name);
		try {
			server.setAddress(new InetSocketAddress(InetAddress.getByName(address), port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private void handleJoinServerResponse(IResponse response) {
		ErrorPresenter.showAndWait(AlertType.ERROR, EMessageCode.SERVER_JOIN_FAILED_TITLE, EMessageCode.SERVER_JOIN_FAILED_HEADER, response);
	}
}
