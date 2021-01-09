package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.fxstyle.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServer;
import fr.pederobien.mumble.client.gui.model.Server;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ServerPresenter extends PresenterBase implements IObsServer {
	private static final Map<Server, ServerPresenter> PRESENTERS = new HashMap<Server, ServerPresenter>();
	private Server server;
	private StringProperty serverNameProperty, serverIpAddressProperty;
	private SimpleLanguageProperty serverReachableStatusProperty;
	private ObjectProperty<Paint> textFillProperty;
	private boolean isReachable;

	public static ServerPresenter getOrCreateServerPresenter(Server server) {
		ServerPresenter presenter = PRESENTERS.get(server);
		if (presenter != null)
			return presenter;

		presenter = new ServerPresenter(server);
		PRESENTERS.put(server, presenter);
		return presenter;
	}

	private ServerPresenter(Server server) {
		this.server = server;

		isReachable = server.isReachable();
		server.addObserver(this);

		serverNameProperty = new SimpleStringProperty(server.getName());
		serverIpAddressProperty = new SimpleStringProperty(server.getAddress() + ":" + server.getPort());
		serverReachableStatusProperty = getPropertyHelper().languageProperty(getServerStateCode());
		textFillProperty = new SimpleObjectProperty<Paint>(getServerReachableStatusColor());
	}

	@Override
	public void onNameChanged(Server server, String oldName, String newName) {
		serverNameProperty.setValue(newName);
	}

	@Override
	public void onIpAddressChanged(Server server, String oldAddress, String newAddress) {
		serverIpAddressProperty.setValue(newAddress + ":" + server.getPort());
	}

	@Override
	public void onPortChanged(Server server, int oldPort, int newPort) {
		serverIpAddressProperty.setValue(server.getAddress() + ":" + newPort);
	}

	@Override
	public void onReachableStatusChanged(Server server, boolean isReachable) {
		this.isReachable = isReachable;
		dispatch(() -> {
			serverReachableStatusProperty.setCode(getServerStateCode());
			textFillProperty.setValue(getServerReachableStatusColor());
		});
	}

	@Override
	public void onCloseRequest() {
		server.dispose();
	}

	/**
	 * @return The property to display the server name.
	 */
	public StringProperty serverNameProperty() {
		return serverNameProperty;
	}

	/**
	 * @return The property to display the server Ip address.
	 */
	public StringProperty serverIpAddressProperty() {
		return serverIpAddressProperty;
	}

	/**
	 * @return The message to display the server status.
	 */
	public StringProperty serverStatusProperty() {
		return serverReachableStatusProperty;
	}

	/**
	 * @return The color in which the server status is displayed.
	 */
	public ObjectProperty<Paint> textFillProperty() {
		return textFillProperty;
	}

	/**
	 * @return The code associated to the message to be displayed when the server is reachable or not.
	 */
	private IMessageCode getServerStateCode() {
		return isReachable ? EMessageCode.REACHABLE_SERVER : EMessageCode.UNREACHABLE_SERVER;
	}

	/**
	 * @return The color in which the server status is displayed.
	 */
	private Paint getServerReachableStatusColor() {
		return isReachable ? Color.GREEN : Color.RED;
	}
}
