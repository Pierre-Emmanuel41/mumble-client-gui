package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsServerListPresenter;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.properties.SimpleFontProperty;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;

public class ServerManagementPresenter extends PresenterBase implements IObsServerListPresenter {
	private SimpleLanguageProperty joinServerLanguageProperty, addServerLanguageProperty, editServerLanguageProperty, deleteServerLanguageProperty,
			refreshServersLanguageProperty;

	private SimpleFontProperty fontProperty;
	private BooleanProperty joinServerDisableProperty, addServerDisableProperty, editServerDisableProperty, deleteServerDisableProperty, refreshServersDisableProperty;

	private ServerList serverList;
	private Server selectedServer;

	public ServerManagementPresenter(ServerList serverList) {
		this.serverList = serverList;

		joinServerLanguageProperty = createLanguageProperty(EMessageCode.JOIN_SERVER);
		addServerLanguageProperty = createLanguageProperty(EMessageCode.ADD_SERVER);
		editServerLanguageProperty = createLanguageProperty(EMessageCode.EDIT_SERVER);
		deleteServerLanguageProperty = createLanguageProperty(EMessageCode.DELETE_SERVER);
		refreshServersLanguageProperty = createLanguageProperty(EMessageCode.REFRESH_SERVERS);

		fontProperty = createFontProperty();

		joinServerDisableProperty = new SimpleBooleanProperty(true);
		addServerDisableProperty = new SimpleBooleanProperty(false);
		editServerDisableProperty = new SimpleBooleanProperty(true);
		deleteServerDisableProperty = new SimpleBooleanProperty(true);
		refreshServersDisableProperty = new SimpleBooleanProperty(serverList.getServers().isEmpty());
	}

	@Override
	public void onSelectedServerChanged(Server oldServer, Server newServer) {
		selectedServer = newServer;
		joinServerDisableProperty.setValue(selectedServer == null || !selectedServer.isReachable());
		editServerDisableProperty.setValue(selectedServer == null);
		deleteServerDisableProperty.setValue(selectedServer == null);
	}

	/**
	 * @return The message to display in order to join the server.
	 */
	public StringProperty joinServerLanguageProperty() {
		return joinServerLanguageProperty;
	}

	/**
	 * @return The property in order to enable/disable the "join server" functionality.
	 */
	public BooleanProperty joinServerDisableProperty() {
		return joinServerDisableProperty;
	}

	/**
	 * @return The message to display in order to add a server.
	 */
	public StringProperty addServerLanguageProperty() {
		return addServerLanguageProperty;
	}

	/**
	 * @return The property in order to enable/disable the "add server" functionality.
	 */
	public BooleanProperty addServerDisabledProperty() {
		return addServerDisableProperty;
	}

	/**
	 * @return The message to display in order to edit the server.
	 */
	public StringProperty editServerLanguageProperty() {
		return editServerLanguageProperty;
	}

	/**
	 * @return The property to enable/disable the "edit server" functionality.
	 */
	public BooleanProperty editServerDisableProperty() {
		return editServerDisableProperty;
	}

	/**
	 * @return The message to display in order to delete the server.
	 */
	public StringProperty deleteServerLanguageProperty() {
		return deleteServerLanguageProperty;
	}

	/**
	 * @return The property to enable/disable the "delete server" functionality.
	 */
	public BooleanProperty deleteServerDisableProperty() {
		return deleteServerDisableProperty;
	}

	/**
	 * @return The message to display in order to refresh server status.
	 */
	public StringProperty refreshServersLanguageProperty() {
		return refreshServersLanguageProperty;
	}

	/**
	 * @return The property to enable/disable the "refresh server" functionality.
	 */
	public BooleanProperty refreshServersDisableProperty() {
		return refreshServersDisableProperty;
	}

	/**
	 * @return The font to display messages.
	 */
	public ObjectProperty<Font> fontProperty() {
		return fontProperty;
	}

	public void onJoinServerClicked(ActionEvent event) {
		System.out.println("Joining server");
	}

	public void onAddServerClicked(ActionEvent event) {
		System.out.println("Adding new server");
	}

	public void onEditServerClicked(ActionEvent event) {
		System.out.println("Editing server");
	}

	public void onDeleteServerClicked(ActionEvent event) {
		System.out.println("Deleting server");
	}

	public void onRefreshServersClicked(ActionEvent event) {
		for (Server server : serverList.getServers()) {
			server.disconnect();
			server.connect();
		}
	}
}
