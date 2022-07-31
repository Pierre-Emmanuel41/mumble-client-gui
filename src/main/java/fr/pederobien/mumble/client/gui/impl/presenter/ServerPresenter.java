package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.interfaces.ICode;
import fr.pederobien.mumble.client.player.event.MumbleServerAddressChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleServerJoinPostEvent;
import fr.pederobien.mumble.client.player.event.MumbleServerNameChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleServerReachableStatusChangeEvent;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ServerPresenter extends PresenterBase implements IEventListener {
	private IPlayerMumbleServer server;
	private StringProperty serverNameProperty, serverIpAddressProperty;
	private SimpleLanguageProperty serverReachableStatusProperty;
	private ObjectProperty<Paint> textFillProperty;

	public ServerPresenter(IPlayerMumbleServer server) {
		this.server = server;

		serverNameProperty = new SimpleStringProperty(server.getName());
		serverIpAddressProperty = new SimpleStringProperty(server.getAddress().getAddress().getHostAddress() + ":" + server.getAddress().getPort());
		serverReachableStatusProperty = getPropertyHelper().languageProperty(getServerStateCode());
		textFillProperty = new SimpleObjectProperty<Paint>(getServerReachableStatusColor());

		EventManager.registerListener(this);
	}

	@Override
	public void onCloseRequest() {
		server.close();
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

	@EventHandler
	private void onNameChanged(MumbleServerNameChangePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		serverNameProperty.setValue(event.getServer().getName());
	}

	@EventHandler
	private void onIpAddressChanged(MumbleServerAddressChangePostEvent event) {
		if (!event.getServer().equals(server))
			return;

		serverIpAddressProperty.setValue(event.getServer().getAddress().getAddress().getHostAddress() + ":" + server.getAddress().getPort());
	}

	@EventHandler
	private void onReachableStatusChanged(MumbleServerReachableStatusChangeEvent event) {
		if (!event.getServer().equals(server))
			return;

		dispatch(() -> {
			serverReachableStatusProperty.setCode(getServerStateCode());
			textFillProperty.setValue(getServerReachableStatusColor());
		});
	}

	@EventHandler
	private void onServerJoin(MumbleServerJoinPostEvent event) {
		EventManager.unregisterListener(this);
	}

	/**
	 * @return The code associated to the message to be displayed when the server is reachable or not.
	 */
	private ICode getServerStateCode() {
		return server.isReachable() ? EMessageCode.REACHABLE_SERVER : EMessageCode.UNREACHABLE_SERVER;
	}

	/**
	 * @return The color in which the server status is displayed.
	 */
	private Paint getServerReachableStatusColor() {
		return server.isReachable() ? Color.GREEN : Color.RED;
	}
}
