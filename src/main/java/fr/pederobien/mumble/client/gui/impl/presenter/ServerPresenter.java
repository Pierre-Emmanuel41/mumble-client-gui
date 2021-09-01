package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.event.ServerIpAddressChangePostEvent;
import fr.pederobien.mumble.client.event.ServerNameChangePostEvent;
import fr.pederobien.mumble.client.event.ServerPortNumberChangePostEvent;
import fr.pederobien.mumble.client.event.ServerReachableChangeEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ServerPresenter extends PresenterBase implements IEventListener {
	private static final Map<IMumbleServer, ServerPresenter> PRESENTERS = new HashMap<IMumbleServer, ServerPresenter>();
	private IMumbleServer server;
	private StringProperty serverNameProperty, serverIpAddressProperty;
	private SimpleLanguageProperty serverReachableStatusProperty;
	private ObjectProperty<Paint> textFillProperty;

	public static ServerPresenter getOrCreateServerPresenter(IMumbleServer server) {
		ServerPresenter presenter = PRESENTERS.get(server);
		if (presenter != null)
			return presenter;

		presenter = new ServerPresenter(server);
		PRESENTERS.put(server, presenter);
		return presenter;
	}

	private ServerPresenter(IMumbleServer server) {
		this.server = server;

		EventManager.registerListener(this);

		serverNameProperty = new SimpleStringProperty(server.getName());
		serverIpAddressProperty = new SimpleStringProperty(server.getAddress() + ":" + server.getPort());
		serverReachableStatusProperty = getPropertyHelper().languageProperty(getServerStateCode());
		textFillProperty = new SimpleObjectProperty<Paint>(getServerReachableStatusColor());
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

	@EventHandler(priority = EventPriority.NORMAL)
	private void onNameChanged(ServerNameChangePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		serverNameProperty.setValue(event.getServer().getName());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onIpAddressChanged(ServerIpAddressChangePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		serverIpAddressProperty.setValue(event.getServer().getAddress() + ":" + server.getPort());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPortChanged(ServerPortNumberChangePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		serverIpAddressProperty.setValue(server.getAddress() + ":" + event.getServer().getPort());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onReachableStatusChanged(ServerReachableChangeEvent event) {
		if (!event.getServer().equals(server))
			return;

		dispatch(() -> {
			serverReachableStatusProperty.setCode(getServerStateCode());
			textFillProperty.setValue(getServerReachableStatusColor());
		});
	}

	/**
	 * @return The code associated to the message to be displayed when the server is reachable or not.
	 */
	private IMessageCode getServerStateCode() {
		return server.isReachable() ? EMessageCode.REACHABLE_SERVER : EMessageCode.UNREACHABLE_SERVER;
	}

	/**
	 * @return The color in which the server status is displayed.
	 */
	private Paint getServerReachableStatusColor() {
		return server.isReachable() ? Color.GREEN : Color.RED;
	}
}
