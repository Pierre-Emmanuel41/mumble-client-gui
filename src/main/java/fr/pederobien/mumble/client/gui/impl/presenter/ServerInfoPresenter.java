package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.regex.Pattern;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class ServerInfoPresenter extends PresenterBase {
	private ServerList serverList;
	private Server server;
	private InternalObserver observer;

	private StringProperty serverNameProperty;
	private ObjectProperty<Border> serverNameBorderProperty;
	private StringProperty serverIpAddressProperty;
	private ObjectProperty<Border> serverIpAddressBorderProperty;
	private StringProperty serverPortProperty;
	private ObjectProperty<Border> serverPortBorderProperty;
	private BooleanProperty okDisableProperty;
	private Stage stage;

	public ServerInfoPresenter(ServerList serverList, Server server) {
		this.serverList = serverList;
		this.server = server;

		serverNameBorderProperty = new SimpleObjectProperty<Border>(null);
		serverNameProperty = new SimpleStringProperty(server.getName().equals(Server.DEFAULT_NAME) ? null : server.getName());

		serverIpAddressBorderProperty = new SimpleObjectProperty<Border>(null);
		serverIpAddressProperty = new SimpleStringProperty(server.getAddress().equals(Server.DEFAULT_ADDRESS) ? null : server.getAddress());

		serverPortBorderProperty = new SimpleObjectProperty<Border>(null);
		serverPortProperty = new SimpleStringProperty(server.getPort() == Server.DEFAULT_PORT ? null : "" + server.getPort());

		okDisableProperty = new SimpleBooleanProperty(true);

		observer = new InternalObserver();
		serverNameProperty.addListener(observer);
		serverIpAddressProperty.addListener(observer);
		serverPortProperty.addListener(observer);

		observer.updateOkDisableProperty();
		stage = new Stage();
		stage.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE)
				stage.close();
		});
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
	protected abstract void onOkButtonClicked(Server server, String name, String address, int port);

	/**
	 * @return The stage on which graphical components are displayed.
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @return The code associated to the title of the server info view.
	 */
	public IMessageCode titleCode() {
		return EMessageCode.ADD_NEW_SERVER_TITLE;
	}

	// Server name
	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * @return The code associated to the server name message.
	 */
	public IMessageCode serverNameCode() {
		return EMessageCode.SERVER_NAME;
	}

	/**
	 * @return The property to change the border for the server name input.
	 */
	public ObjectProperty<Border> serverNameBorderProperty() {
		return serverNameBorderProperty;
	}

	/**
	 * @return The code associated to the prompt text of the text field that receive the server name.
	 */
	public IMessageCode serverNamePromptCode() {
		return EMessageCode.SERVER_NAME_PROMPT;
	}

	/**
	 * @return The code associated to the text of the tooltip for the text field that receive the server name.
	 */
	public IMessageCode serverNameTooltipCode() {
		return EMessageCode.SERVER_NAME_TOOLTIP;
	}

	/**
	 * @return The property to receive the server name given by the user.
	 */
	public StringProperty serverNameProperty() {
		return serverNameProperty;
	}

	// Server ip address
	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * @return The code associated to the server ip address message.
	 */
	public IMessageCode serverIpAddressCode() {
		return EMessageCode.SERVER_IP_ADDRESS;
	}

	/**
	 * @return The property to change the border for the server ip adress input.
	 */
	public ObjectProperty<Border> serverIpAddressBorderProperty() {
		return serverIpAddressBorderProperty;
	}

	/**
	 * @return The code associated to the prompt text of the text field that receive the server ip address.
	 */
	public IMessageCode serverIpAddressPromptCode() {
		return EMessageCode.SERVER_IP_ADDRESS_PROMPT;
	}

	/**
	 * @return The code associated to the text of the tooltip for the text field that receive the server ip address.
	 */
	public IMessageCode serverIpAddressTooltipCode() {
		return EMessageCode.SERVER_IP_ADDRESS_TOOLTIP;
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
	 * @return The code associated to the server port number message.
	 */
	public IMessageCode serverPortCode() {
		return EMessageCode.SERVER_PORT_NUMBER;
	}

	/**
	 * @return The property to change the border for the server port number input.
	 */
	public ObjectProperty<Border> serverPortBorderProperty() {
		return serverPortBorderProperty;
	}

	/**
	 * @return The code associated to the prompt text of the text field that receive the server port number.
	 */
	public IMessageCode serverPortPromptCode() {
		return EMessageCode.SERVER_PORT_NUMBER_PROMPT;
	}

	/**
	 * @return The code associated to the text of the tooltip for the text field that receive the server port number.
	 */
	public IMessageCode serverPortTooltipCode() {
		return EMessageCode.SERVER_PORT_NUMBER_TOOLTIP;
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
	 * @return The code associated to the ok message.
	 */
	public IMessageCode okCode() {
		return EMessageCode.OK;
	}

	/**
	 * @return The property in order to enable/disable the "add server" functionality.
	 */
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return The code associated to the cancel message.
	 */
	public IMessageCode cancelCode() {
		return EMessageCode.CANCEL;
	}

	public void ok(ActionEvent event) {
		if (okDisableProperty.get())
			return;
		onOkButtonClicked(server, serverNameProperty().get(), serverIpAddressProperty().get(), Integer.parseInt(serverPortProperty().get()));
		stage.close();
	}

	/**
	 * Action to perform when the cancel button is clicked
	 * 
	 * @param event The event thrown event.
	 */
	public void cancel(ActionEvent event) {
		stage.close();
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
