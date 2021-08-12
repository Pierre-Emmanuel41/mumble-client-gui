package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.ServerChannelsView;
import fr.pederobien.mumble.client.gui.impl.view.ServerInfoView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsServerListPresenter;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.mumble.client.interfaces.IResponse;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

public class ServerManagementPresenter extends PresenterBase implements IObsServerListPresenter {
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
	private IMumbleServer selectedServer;

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
	}

	@Override
	public void onSelectedServerChanged(IMumbleServer oldServer, IMumbleServer newServer) {
		selectedServer = newServer;
		joinServerDisableProperty.setValue(selectedServer == null || !selectedServer.isReachable());
		editServerDisableProperty.setValue(selectedServer == null);
		deleteServerDisableProperty.setValue(selectedServer == null);
	}

	@Override
	public void onDoubleClickOnServer(IMumbleServer server) {
		onJoin();
	}

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
		if (!selectedServer.isReachable())
			return;

		selectedServer.join(response -> joinServerResponse(response));
	}

	/**
	 * Creates a new window in which the user can add a new mumble server.
	 */
	public void onAdd() {
		new ServerInfoView(getPrimaryStage(), new ServerInfoPresenter(serverList, ServerList.createNewDefaultServer()) {
			@Override
			protected void onOkButtonClicked(IMumbleServer server, String name, String address, int port) {
				performChangesOnServer(server, name, address, port);
				serverList.add(server);
			}
		});
	}

	/**
	 * Creates a new window in which the user can edit a mumble server.
	 */
	public void onEdit() {
		selectedServer.close();
		new ServerInfoView(getPrimaryStage(), new ServerInfoPresenter(serverList, selectedServer) {
			@Override
			protected void onOkButtonClicked(IMumbleServer server, String name, String address, int port) {
				performChangesOnServer(server, name, address, port);
				// Server is closed but parameter are the same
				if (!server.isReachable())
					server.open();
			}

			@Override
			public boolean onCancelButtonClicked() {
				selectedServer.open();
				return super.onCancelButtonClicked();
			}
		});
	}

	/**
	 * Dispose the selected server and removes it from the server list.
	 */
	public void onDelete() {
		selectedServer.dispose();
		serverList.remove(selectedServer);
	}

	private void performChangesOnServer(IMumbleServer server, String name, String address, int port) {
		server.setName(name);
		server.setAddress(address);
		server.setPort(port);
	}

	private void joinServerResponse(IResponse<Boolean> response) {
		dispatch(() -> {
			getPrimaryStage().getScene().setRoot(new ServerChannelsView(new ServerChannelsPresenter(selectedServer)).getRoot());
			serverList.getServers().forEach(server -> {
				if (!server.equals(selectedServer))
					server.close();
			});
		});
	}
}
