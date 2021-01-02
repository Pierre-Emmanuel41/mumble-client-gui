package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.interfaces.IObsServer;
import fr.pederobien.mumble.client.gui.model.Server;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class ServerPresenter extends PresenterBase implements IObsServer {
	private StringProperty serverNameProperty;
	private BooleanProperty isReachableProperty;

	public ServerPresenter(Stage primaryStage, Server server) {
		super(primaryStage);

		server.addObserver(this);
		serverNameProperty = new SimpleStringProperty(this, "serverName", server.getName());
		isReachableProperty = new SimpleBooleanProperty(this, "isReachable", server.isReachable());
	}

	/**
	 * @return The property to display the server name.
	 */
	public StringProperty serverNameProperty() {
		return serverNameProperty;
	}

	/**
	 * @return The property to display the reachable state of the server.
	 */
	public BooleanProperty isReachable() {
		return isReachableProperty;
	}

	/**
	 * @return The code associated to the message to be displayed when the server is reachable or not.
	 */
	public IMessageCode serverStateCode() {
		return isReachable().get() ? EMessageCode.REACHABLE_SERVER : EMessageCode.UNREACHABLE_SERVER;
	}

	@Override
	public void onNameChanged(Server server, String oldName, String newName) {
		serverNameProperty.setValue(newName);
	}

	@Override
	public void onIpAddressChanged(Server server, String oldAddress, String newAddress) {

	}

	@Override
	public void onPortChanged(Server server, int oldPort, int newPort) {

	}

	@Override
	public void onReachableStatusChanged(Server server, boolean isReachable) {
		isReachableProperty.setValue(isReachable);
	}
}
