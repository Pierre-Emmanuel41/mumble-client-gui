package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ServerChannelsView;
import fr.pederobien.mumble.client.gui.impl.view.ServerInfoView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsServerListPresenter;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ServerManagementPresenter extends PresenterBase implements IObsServerListPresenter {
	private BooleanProperty joinServerDisableProperty, addServerDisableProperty, editServerDisableProperty, deleteServerDisableProperty;

	private ServerList serverList;
	private Server selectedServer;

	public ServerManagementPresenter(ServerList serverList) {
		this.serverList = serverList;

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

	@Override
	public void onDoubleClickOnServer(Server server) {
		onJoin();
	}

	/**
	 * @return The code associated to the message to display in order to join the server.
	 */
	public IMessageCode joinCode() {
		return EMessageCode.JOIN_SERVER;
	}

	/**
	 * @return The property in order to enable/disable the "join server" functionality.
	 */
	public BooleanProperty joinDisableProperty() {
		return joinServerDisableProperty;
	}

	/**
	 * @return The code associated to the message to display in order to add a server.
	 */
	public IMessageCode addCode() {
		return EMessageCode.ADD_SERVER;
	}

	/**
	 * @return The property in order to enable/disable the "add server" functionality.
	 */
	public BooleanProperty addDisableProperty() {
		return addServerDisableProperty;
	}

	/**
	 * @return The code associated to the message to display in order to edit the server.
	 */
	public IMessageCode editCode() {
		return EMessageCode.EDIT_SERVER;
	}

	/**
	 * @return The property to enable/disable the "edit server" functionality.
	 */
	public BooleanProperty editDisableProperty() {
		return editServerDisableProperty;
	}

	/**
	 * @return The code associated to the message to display in order to delete the server.
	 */
	public IMessageCode deleteCode() {
		return EMessageCode.DELETE_SERVER;
	}

	/**
	 * @return The property to enable/disable the "delete server" functionality.
	 */
	public BooleanProperty deleteDisableProperty() {
		return deleteServerDisableProperty;
	}

	public void onJoin() {
		getPrimaryStage().getScene().setRoot(new ServerChannelsView(new ServerChannelsPresenter(selectedServer)).getRoot());
	}

	public void onAdd() {
		new ServerInfoView(getPrimaryStage(), new ServerInfoPresenter(serverList, new Server()) {
			@Override
			protected void onOkButtonClicked(Server server, String name, String address, int port) {
				performChangesOnServer(server, name, address, port);
				serverList.add(server);
			}
		});
	}

	public void onEdit() {
		new ServerInfoView(getPrimaryStage(), new ServerInfoPresenter(serverList, selectedServer) {
			@Override
			protected void onOkButtonClicked(Server server, String name, String address, int port) {
				performChangesOnServer(server, name, address, port);
			}
		});
	}

	public void onDelete() {
		serverList.remove(selectedServer);
	}

	private void performChangesOnServer(Server server, String name, String address, int port) {
		server.setName(name);
		server.setAddress(address);
		server.setPort(port);
	}
}
