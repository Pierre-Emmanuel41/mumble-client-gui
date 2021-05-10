package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerInfoPresenter;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class ServerInfoView extends ViewBase<ServerInfoPresenter, GridPane> implements IOkCancelView {
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;
	private double maxLabelWidth;

	private Label serverNameLabel, serverIpAddressLabel, serverPortLabel;
	private TextField serverNameTextField, serverIpAddressTextField, serverPortTextField;

	public ServerInfoView(ServerInfoPresenter presenter) {
		super(presenter, new GridPane());

		// Server Name
		// -------------------------------------------------------------------------------------------------

		FlowPane serverName = new FlowPane();
		serverName.setPrefWidth(490);

		serverNameLabel = new Label();
		serverNameLabel.fontProperty().bind(getPresenter().fontProperty());
		serverNameLabel.textProperty().bind(getPresenter().serverNameTextProperty());
		serverName.getChildren().add(serverNameLabel);

		serverNameTextField = new TextField();
		serverNameTextField.fontProperty().bind(getPresenter().fontProperty());
		serverNameTextField.promptTextProperty().bind(getPresenter().serverNamePromptProperty());
		serverNameTextField.textProperty().bindBidirectional(getPresenter().serverNameProperty());
		serverNameTextField.borderProperty().bind(getPresenter().serverNameBorderProperty());
		serverNameTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerName(newValue));
		serverNameTextField.tooltipProperty().bind(getPresenter().serverNameTooltipProperty());

		serverName.getChildren().add(serverNameTextField);
		FlowPane.setMargin(serverNameTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverName, 0, 0);
		GridPane.setMargin(serverName, new Insets(0, 0, 10, 0));

		// Server IP address
		// --------------------------------------------------------------------------------------------------

		FlowPane serverIpAddress = new FlowPane();

		serverIpAddressLabel = new Label();
		serverIpAddressLabel.fontProperty().bind(getPresenter().fontProperty());
		serverIpAddressLabel.textProperty().bind(getPresenter().serverIpAddressTextProperty());
		serverIpAddress.getChildren().add(serverIpAddressLabel);

		serverIpAddressTextField = new TextField();
		serverIpAddressTextField.fontProperty().bind(getPresenter().fontProperty());
		serverIpAddressTextField.promptTextProperty().bind(getPresenter().serverIpAddressPromptProperty());
		serverIpAddressTextField.textProperty().bindBidirectional(getPresenter().serverIpAddressProperty());
		serverIpAddressTextField.borderProperty().bind(getPresenter().serverIpAddressBorderProperty());
		serverIpAddressTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerIpAdress(newValue));
		serverIpAddressTextField.tooltipProperty().bind(getPresenter().serverIpAddressTooltipProperty());

		serverIpAddress.getChildren().add(serverIpAddressTextField);
		FlowPane.setMargin(serverIpAddressTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverIpAddress, 0, 1);
		GridPane.setMargin(serverIpAddress, new Insets(10, 0, 10, 0));

		// Server Port number
		// --------------------------------------------------------------------------------------------------

		FlowPane serverPort = new FlowPane();

		serverPortLabel = new Label();
		serverPortLabel.fontProperty().bind(getPresenter().fontProperty());
		serverPortLabel.textProperty().bind(getPresenter().serverPortTextProperty());
		serverPort.getChildren().add(serverPortLabel);

		serverPortTextField = new TextField();
		serverPortTextField.fontProperty().bind(getPresenter().fontProperty());
		serverPortTextField.promptTextProperty().bind(getPresenter().serverPortPromptProperty());
		serverPortTextField.textProperty().bindBidirectional(getPresenter().serverPortProperty());
		serverPortTextField.borderProperty().bind(getPresenter().serverPortBorderProperty());
		serverPortTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerPortNumber(newValue));
		serverPortTextField.tooltipProperty().bind(getPresenter().serverPortTooltipProperty());

		serverPort.getChildren().add(serverPortTextField);
		FlowPane.setMargin(serverPortTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverPort, 0, 2);
		GridPane.setMargin(serverPort, new Insets(10, 0, 0, 0));
	}

	private void maxWidthLabel(Label... labels) {
		double max = 0;
		for (Label label : labels)
			max = Math.max(max, label.getWidth());

		maxLabelWidth = max;
		for (Label label : labels)
			label.setPrefWidth(maxLabelWidth);
	}

	private void maxWidthTextField(TextField... textFields) {
		for (TextField textField : textFields)
			textField.setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - maxLabelWidth - marginBetweenLabelAndTextField);
	}

	@Override
	public void onPostShown() {
		maxWidthLabel(serverNameLabel, serverIpAddressLabel, serverPortLabel);
		maxWidthTextField(serverNameTextField, serverIpAddressTextField, serverPortTextField);
	}
}
