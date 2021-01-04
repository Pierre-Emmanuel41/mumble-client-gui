package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsServer;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.properties.SimpleFontProperty;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ServerPresenter extends PresenterBase implements IObsServer {
	private StringProperty serverNameProperty, serverIpAddressProperty;
	private SimpleLanguageProperty serverReachableStatusProperty;
	private SimpleFontProperty fontProperty;
	private ObjectProperty<Paint> textFillProperty;
	private boolean isReachable;

	public ServerPresenter(Stage primaryStage, Server server) {
		super(primaryStage);

		isReachable = server.isReachable();

		server.addObserver(this);
		serverNameProperty = new SimpleStringProperty(server.getName());
		serverIpAddressProperty = new SimpleStringProperty(server.getAddress() + ":" + server.getPort());
		serverReachableStatusProperty = createLanguageProperty(getServerStateCode());
		textFillProperty = new SimpleObjectProperty<Paint>(getServerReachableStatusColor());

		fontProperty = createFontProperty();
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
	 * @return The font property to display message.
	 */
	public ObjectProperty<Font> fontProperty() {
		return fontProperty;
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
