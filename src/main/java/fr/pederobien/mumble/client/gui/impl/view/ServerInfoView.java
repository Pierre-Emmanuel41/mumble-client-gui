package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.javafx.configuration.impl.components.SimpleLabel;
import fr.pederobien.javafx.configuration.impl.components.SimpleTextField;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerInfoPresenter;
import javafx.scene.layout.GridPane;

public class ServerInfoView extends ViewBase<ServerInfoPresenter, GridPane> {

	/**
	 * Creates a view in order to modify the name, IP address and port number of a server.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public ServerInfoView(ServerInfoPresenter presenter) {
		super(presenter, new GridPane());

		// Server Name
		// -------------------------------------------------------------------------------------------------

		SimpleTextField serverNameTextField = new SimpleTextField();
		serverNameTextField.setPromptText(EGuiCode.SERVER_NAME_PROMPT);
		serverNameTextField.setTooltip(EGuiCode.SERVER_NAME_TOOLTIP);
		serverNameTextField.textProperty().bindBidirectional(getPresenter().serverNameProperty());
		serverNameTextField.borderProperty().bind(getPresenter().serverNameBorderProperty());
		serverNameTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerName(newValue));

		getPresenter().getFormView().addRow(new SimpleLabel(EGuiCode.SERVER_NAME), serverNameTextField);

		// Server IP address
		// --------------------------------------------------------------------------------------------------

		SimpleTextField serverIpAddressTextField = new SimpleTextField();
		serverIpAddressTextField.setPromptText(EGuiCode.SERVER_IP_ADDRESS_PROMPT);
		serverIpAddressTextField.setTooltip(EGuiCode.SERVER_IP_ADDRESS_TOOLTIP);
		serverIpAddressTextField.textProperty().bindBidirectional(getPresenter().serverIpAddressProperty());
		serverIpAddressTextField.borderProperty().bind(getPresenter().serverIpAddressBorderProperty());
		serverIpAddressTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerIpAdress(newValue));

		getPresenter().getFormView().addRow(new SimpleLabel(EGuiCode.SERVER_IP_ADDRESS), serverIpAddressTextField);

		// Server Port number
		// --------------------------------------------------------------------------------------------------

		SimpleTextField serverPortTextField = new SimpleTextField();
		serverPortTextField.setPromptText(EGuiCode.SERVER_PORT_NUMBER_PROMPT);
		serverPortTextField.setTooltip(EGuiCode.SERVER_PORT_NUMBER_TOOLTIP);
		serverPortTextField.textProperty().bindBidirectional(getPresenter().serverPortProperty());
		serverPortTextField.borderProperty().bind(getPresenter().serverPortBorderProperty());
		serverPortTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerPortNumber(newValue));

		getPresenter().getFormView().addRow(new SimpleLabel(EGuiCode.SERVER_PORT_NUMBER), serverPortTextField);

		OkCancelStage okCancelStage = new OkCancelStage(getPrimaryStage(), getPresenter());
		okCancelStage.show();
	}
}
