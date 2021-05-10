package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerInfoPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServerInfoView extends ViewBase<ServerInfoPresenter, GridPane> {
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;
	private double maxLabelWidth;

	public ServerInfoView(Stage initOwner, ServerInfoPresenter presenter) {
		super(presenter, new GridPane());

		// Secondary stage in order to display this view
		Stage stage = new Stage();
		stage.titleProperty().bind(getPresenter().titleTextProperty());

		getRoot().setPadding(new Insets(marginBetweenRootAndChildren));
		getRoot().setAlignment(Pos.CENTER);
		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ENTER)
				getPresenter().ok();
		});

		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE)
				stage.close();
		});

		// Server Name
		// -------------------------------------------------------------------------------------------------

		FlowPane serverName = new FlowPane();
		serverName.setPrefWidth(490);

		Label serverNameLabel = new Label();
		serverNameLabel.fontProperty().bind(getPresenter().fontProperty());
		serverNameLabel.textProperty().bind(getPresenter().serverNameTextProperty());
		serverName.getChildren().add(serverNameLabel);

		TextField serverNameTextField = new TextField();
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

		Label serverIpAddressLabel = new Label();
		serverIpAddressLabel.fontProperty().bind(getPresenter().fontProperty());
		serverIpAddressLabel.textProperty().bind(getPresenter().serverIpAddressTextProperty());
		serverIpAddress.getChildren().add(serverIpAddressLabel);

		TextField ipTextField = new TextField();
		ipTextField.fontProperty().bind(getPresenter().fontProperty());
		ipTextField.promptTextProperty().bind(getPresenter().serverIpAddressPromptProperty());
		ipTextField.textProperty().bindBidirectional(getPresenter().serverIpAddressProperty());
		ipTextField.borderProperty().bind(getPresenter().serverIpAddressBorderProperty());
		ipTextField.focusedProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateServerIpAdress(newValue));
		ipTextField.tooltipProperty().bind(getPresenter().serverIpAddressTooltipProperty());

		serverIpAddress.getChildren().add(ipTextField);
		FlowPane.setMargin(ipTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverIpAddress, 0, 1);
		GridPane.setMargin(serverIpAddress, new Insets(10, 0, 10, 0));

		// Server Port number
		// --------------------------------------------------------------------------------------------------

		FlowPane serverPort = new FlowPane();

		Label serverPortLabel = new Label();
		serverPortLabel.fontProperty().bind(getPresenter().fontProperty());
		serverPortLabel.textProperty().bind(getPresenter().serverPortTextProperty());
		serverPort.getChildren().add(serverPortLabel);

		TextField serverPortTextField = new TextField();
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

		// OK CANCEL BUTTONS
		// --------------------------------------------------------------------------------------------------

		FlowPane buttons = new FlowPane();
		buttons.setAlignment(Pos.CENTER_RIGHT);

		Button ok = new Button();
		ok.fontProperty().bind(getPresenter().fontProperty());
		ok.textProperty().bind(getPresenter().okTextProperty());
		ok.setOnAction(e -> {
			if (getPresenter().ok())
				stage.close();
		});
		ok.disableProperty().bind(getPresenter().okDisableProperty());
		buttons.getChildren().add(ok);
		FlowPane.setMargin(ok, new Insets(0, 10, 0, 0));

		Button cancel = new Button();
		cancel.fontProperty().bind(getPresenter().fontProperty());
		cancel.textProperty().bind(getPresenter().cancelTextProperty());
		cancel.setOnAction(e -> stage.close());
		buttons.getChildren().add(cancel);
		FlowPane.setMargin(cancel, new Insets(0, 0, 0, 10));

		getRoot().add(buttons, 0, 3);
		GridPane.setMargin(buttons, new Insets(50, 0, 0, 0));

		// --------------------------------------------------------------------------------------------------

		stage.setScene(new Scene(getRoot()));
		stage.sizeToScene();
		stage.setResizable(false);
		stage.initOwner(initOwner);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();

		maxWidthLabel(serverNameLabel, serverIpAddressLabel, serverPortLabel);
		maxWidthTextField(serverNameTextField, ipTextField, serverPortTextField);
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
}
