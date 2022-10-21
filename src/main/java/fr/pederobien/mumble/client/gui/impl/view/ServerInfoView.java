package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerInfoPresenter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

		Label serverNameLabel = new Label();
		serverNameLabel.fontProperty().bind(getPresenter().fontProperty());
		serverNameLabel.textProperty().bind(getPresenter().serverNameTextProperty());

		TextField serverNameTextField = new TextField();
		serverNameTextField.fontProperty().bind(getPresenter().fontProperty());
		serverNameTextField.promptTextProperty().bind(getPresenter().serverNamePromptProperty());
		serverNameTextField.textProperty().bindBidirectional(getPresenter().serverNameProperty());
		serverNameTextField.borderProperty().bind(getPresenter().serverNameBorderProperty());
		serverNameTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerName(newValue));
		serverNameTextField.tooltipProperty().bind(getPresenter().serverNameTooltipProperty());

		getPresenter().getFormView().addRow(serverNameLabel, serverNameTextField);

		// Server IP address
		// --------------------------------------------------------------------------------------------------

		Label serverIpAddressLabel = new Label();
		serverIpAddressLabel.fontProperty().bind(getPresenter().fontProperty());
		serverIpAddressLabel.textProperty().bind(getPresenter().serverIpAddressTextProperty());

		TextField serverIpAddressTextField = new TextField();
		serverIpAddressTextField.fontProperty().bind(getPresenter().fontProperty());
		serverIpAddressTextField.promptTextProperty().bind(getPresenter().serverIpAddressPromptProperty());
		serverIpAddressTextField.textProperty().bindBidirectional(getPresenter().serverIpAddressProperty());
		serverIpAddressTextField.borderProperty().bind(getPresenter().serverIpAddressBorderProperty());
		serverIpAddressTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerIpAdress(newValue));
		serverIpAddressTextField.tooltipProperty().bind(getPresenter().serverIpAddressTooltipProperty());

		getPresenter().getFormView().addRow(serverIpAddressLabel, serverIpAddressTextField);

		// Server Port number
		// --------------------------------------------------------------------------------------------------

		Label serverPortLabel = new Label();
		serverPortLabel.fontProperty().bind(getPresenter().fontProperty());
		serverPortLabel.textProperty().bind(getPresenter().serverPortTextProperty());

		TextField serverPortTextField = new TextField();
		serverPortTextField.fontProperty().bind(getPresenter().fontProperty());
		serverPortTextField.promptTextProperty().bind(getPresenter().serverPortPromptProperty());
		serverPortTextField.textProperty().bindBidirectional(getPresenter().serverPortProperty());
		serverPortTextField.borderProperty().bind(getPresenter().serverPortBorderProperty());
		serverPortTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerPortNumber(newValue));
		serverPortTextField.tooltipProperty().bind(getPresenter().serverPortTooltipProperty());

		getPresenter().getFormView().addRow(serverPortLabel, serverPortTextField);

		OkCancelStage okCancelStage = new OkCancelStage(getPrimaryStage(), getPresenter());
		okCancelStage.show();
	}
}
