package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.regex.Pattern;

import fr.pederobien.javafx.configuration.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public abstract class ServerInfoPresenter extends OkCancelPresenter {
	private ServerList serverList;
	private IPlayerMumbleServer server;
	private InternalObserver observer;

	private SimpleLanguageProperty titleTextProperty;

	// Server name ---------------------------------------------
	private StringProperty serverNameProperty;
	private ObjectProperty<Border> serverNameBorderProperty;

	// Server Ip address -----------------------------------------
	private StringProperty serverIpAddressProperty;
	private ObjectProperty<Border> serverIpAddressBorderProperty;

	// Server port number ----------------------------------------
	private StringProperty serverPortProperty;
	private ObjectProperty<Border> serverPortBorderProperty;

	// Buttons ---------------------------------------------------
	private BooleanProperty okDisableProperty;

	public ServerInfoPresenter(ServerList serverList, IPlayerMumbleServer server) {
		this.serverList = serverList;
		this.server = server;

		server.close();
		titleTextProperty = getPropertyHelper().newLanguageProperty(EGuiCode.ADD_NEW_SERVER_TITLE);

		serverNameProperty = new SimpleStringProperty(server.getName().equals(ServerList.DEFAULT_SERVER_NAME) ? null : server.getName());
		serverNameBorderProperty = new SimpleObjectProperty<Border>(null);

		serverIpAddressProperty = new SimpleStringProperty(
				server.getAddress().getAddress().getHostAddress().equals(ServerList.DEFAULT_SERVER_ADDRESS) ? null : server.getAddress().getAddress().getHostAddress());
		serverIpAddressBorderProperty = new SimpleObjectProperty<Border>(null);

		serverPortProperty = new SimpleStringProperty(server.getAddress().getPort() == ServerList.DEFAULT_SERVER_PORT ? null : "" + server.getAddress().getPort());
		serverPortBorderProperty = new SimpleObjectProperty<Border>(null);

		okDisableProperty = new SimpleBooleanProperty(true);

		observer = new InternalObserver();
		serverNameProperty.addListener(observer);
		serverIpAddressProperty.addListener(observer);
		serverPortProperty.addListener(observer);

		observer.updateOkDisableProperty();
	}

	/**
	 * Action to perform when the user clicks on the ok button. There is no need to check the given parameter because the user can
	 * click if the parameter are invalid.
	 * 
	 * @param server  The original server.
	 * @param name    The server name.
	 * @param address The server ip address.
	 * @param port    The server port number.
	 */
	protected abstract void onOkButtonClicked(IPlayerMumbleServer server, String name, String address, int port);

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public void onClosing() {
		server.open();
	}

	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		onOkButtonClicked(server, serverNameProperty().get(), serverIpAddressProperty().get(), Integer.parseInt(serverPortProperty().get()));
		return true;
	}

	// Server name
	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * @return The property to change the border for the server name input.
	 */
	public ObjectProperty<Border> serverNameBorderProperty() {
		return serverNameBorderProperty;
	}

	/**
	 * @return The property to receive the server name given by the user.
	 */
	public StringProperty serverNameProperty() {
		return serverNameProperty;
	}

	// Server IP address
	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * @return The property to change the border for the server ip adress input.
	 */
	public ObjectProperty<Border> serverIpAddressBorderProperty() {
		return serverIpAddressBorderProperty;
	}

	/**
	 * @return The property to receive the server ip address given by the user.
	 */
	public StringProperty serverIpAddressProperty() {
		return serverIpAddressProperty;
	}

	// Server port
	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * @return The property to change the border for the server port number input.
	 */
	public ObjectProperty<Border> serverPortBorderProperty() {
		return serverPortBorderProperty;
	}

	/**
	 * @return The property to receive the server port number given by the user.
	 */
	public StringProperty serverPortProperty() {
		return serverPortProperty;
	}

	// Buttons
	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * @return The property in order to enable/disable the "add server" functionality.
	 */
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * Try to validate the server name when the focus on the server name text field changes.
	 * 
	 * @param focusValue true if the text field has the focus, false otherwise.
	 */
	public void validateServerName(boolean focusValue) {
		changeBorder(focusValue, serverNameBorderProperty, observer.isNameOk);
	}

	/**
	 * Try to validate the server ip address when the focus on the server ip address text field changes.
	 * 
	 * @param focusValue true if the text field has the focus, false otherwise.
	 */
	public void validateServerIpAdress(boolean focusValue) {
		changeBorder(focusValue, serverIpAddressBorderProperty, observer.isIpOk);
	}

	/**
	 * Try to validate the server port number when the focus on the server port number text field changes.
	 * 
	 * @param focusValue true if the text field has the focus, false otherwise.
	 */
	public void validateServerPortNumber(boolean focusValue) {
		changeBorder(focusValue, serverPortBorderProperty, observer.isPortOk);
	}

	private void changeBorder(boolean focusValue, ObjectProperty<Border> property, boolean condition) {
		if (focusValue) {
			property.set(Border.EMPTY);
			return;
		}

		if (condition)
			property.set(Border.EMPTY);
		else
			property.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
	}

	private class InternalObserver implements ChangeListener<String> {
		private boolean isNameOk, isIpOk, isPortOk;
		private String single = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
		private Pattern pattern = Pattern.compile(single + "\\." + single + "\\." + single + "\\." + single);

		public InternalObserver() {
			isNameOk = onServerNameChanged(serverNameProperty.get());
			isIpOk = onServerIpChanged(serverIpAddressProperty.get());
			isPortOk = onServerPortChanged(serverPortProperty.get());
			updateOkDisableProperty();
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if (observable.equals(serverNameProperty))
				isNameOk = onServerNameChanged(newValue);
			else if (observable.equals(serverIpAddressProperty))
				isIpOk = onServerIpChanged(newValue);
			else
				isPortOk = onServerPortChanged(newValue);

			updateOkDisableProperty();
		}

		private boolean onServerNameChanged(String newName) {
			boolean isValid = newName != null && newName.length() > 5;
			if (!isValid)
				return false;
			return isValid &= !serverList.getServers().stream().filter(s -> !s.equals(server) && s.getName().equals(newName.trim())).findFirst().isPresent();
		}

		private boolean onServerIpChanged(String newIp) {
			return newIp != null && pattern.matcher(newIp).matches();
		}

		private boolean onServerPortChanged(String newPort) {
			try {
				int number = Integer.parseInt(newPort);
				return 0 <= number && number <= 65535;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		private void updateOkDisableProperty() {
			okDisableProperty.setValue(!(isNameOk && isIpOk && isPortOk));
		}
	}
}
