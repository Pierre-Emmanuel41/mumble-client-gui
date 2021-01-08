package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ServerInfoView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsServerListPresenter;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.properties.SimpleFontProperty;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;

public class ServerManagementPresenter extends PresenterBase implements IObsServerListPresenter {
	private SimpleFontProperty fontProperty;
	private SimpleLanguageProperty joinServerLanguageProperty, addServerLanguageProperty, editServerLanguageProperty, deleteServerLanguageProperty;
	private BooleanProperty joinServerDisableProperty, addServerDisableProperty, editServerDisableProperty, deleteServerDisableProperty;

	private ServerList serverList;
	private Server selectedServer;

	public ServerManagementPresenter(ServerList serverList) {
		this.serverList = serverList;

		fontProperty = getPropertyHelper().fontProperty();

		joinServerLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.JOIN_SERVER);
		addServerLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_SERVER);
		editServerLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.EDIT_SERVER);
		deleteServerLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.DELETE_SERVER);

		joinServerDisableProperty = new SimpleBooleanProperty(true);
		addServerDisableProperty = new SimpleBooleanProperty(false);
		editServerDisableProperty = new SimpleBooleanProperty(true);
		deleteServerDisableProperty = new SimpleBooleanProperty(true);
	}

	@Override
	public void onSelectedServerChanged(Server oldServer, Server newServer) {
		selectedServer = newServer;
		joinServerDisableProperty.setValue(selectedServer == null || !selectedServer.isReachable());
		editServerDisableProperty.setValue(selectedServer == null);
		deleteServerDisableProperty.setValue(selectedServer == null);
	}

	/**
	 * @return The font property to display messages.
	 */
	public ObjectProperty<Font> fontProperty() {
		return fontProperty;
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

	public void onJoinServerClicked() {
		System.out.println("Joining server");
	}

	public void onAddServerClicked() {
		new ServerInfoView(getPrimaryStage(), new ServerInfoPresenter(serverList, new Server()) {
			@Override
			protected void onOkButtonClicked(Server server, String name, String address, int port) {
				server.setName(name);
				server.setAddress(address);
				server.setPort(port);
				serverList.add(server);
			}
		});
	}

	public void onEditServerClicked() {
		System.out.println("Editing server");
	}

	public void onDeleteServerClicked() {
		serverList.remove(selectedServer);
	}
}
