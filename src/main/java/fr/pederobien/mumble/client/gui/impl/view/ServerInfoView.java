package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.fxstyle.impl.wrapper.TextFieldWrapper;
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

		getRoot().setPadding(new Insets(marginBetweenRootAndChildren));
		getRoot().setAlignment(Pos.CENTER);
		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ENTER)
				getPresenter().ok(null);
		});

		// Server Name
		// -------------------------------------------------------------------------------------------------

		FlowPane serverName = new FlowPane();
		serverName.setPrefWidth(490);

		Label serverNameLabel = getStyle().createLabel(getPresenter().serverNameCode());
		serverName.getChildren().add(serverNameLabel);

		TextFieldWrapper nameWrapper = getStyle().createTextfield(getPresenter().serverNamePromptCode()).text(getPresenter().serverNameProperty());
		nameWrapper.border(getPresenter().serverNameBorderProperty()).onFocusChanged((obs, oldValue, newValue) -> getPresenter().validateServerName(newValue));
		nameWrapper.tooltip(getPresenter().serverNameTooltipCode()).get();

		serverName.getChildren().add(nameWrapper.get());
		FlowPane.setMargin(nameWrapper.get(), new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverName, 0, 0);
		GridPane.setMargin(serverName, new Insets(0, 0, 10, 0));

		// Server IP address
		// --------------------------------------------------------------------------------------------------

		FlowPane serverIpAddress = new FlowPane();

		Label serverIpAddressLabel = getStyle().createLabel(getPresenter().serverIpAddressCode());
		serverIpAddress.getChildren().add(serverIpAddressLabel);

		TextFieldWrapper ipWrapper = getStyle().createTextfield(getPresenter().serverIpAddressPromptCode()).text(getPresenter().serverIpAddressProperty());
		ipWrapper.border(getPresenter().serverIpAddressBorderProperty()).onFocusChanged((obs, oldValue, newValue) -> getPresenter().validateServerIpAdress(newValue));
		ipWrapper.tooltip(getPresenter().serverIpAddressTooltipCode()).get();

		serverIpAddress.getChildren().add(ipWrapper.get());
		FlowPane.setMargin(ipWrapper.get(), new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverIpAddress, 0, 1);
		GridPane.setMargin(serverIpAddress, new Insets(10, 0, 10, 0));

		// Server Port number
		// --------------------------------------------------------------------------------------------------

		FlowPane serverPort = new FlowPane();

		Label serverPortLabel = getStyle().createLabel(getPresenter().serverPortCode());
		serverPort.getChildren().add(serverPortLabel);

		TextFieldWrapper portWrapper = getStyle().createTextfield(getPresenter().serverPortPromptCode()).text(getPresenter().serverPortProperty());
		portWrapper.border(getPresenter().serverPortBorderProperty()).onFocusChanged((obs, oldValue, newValue) -> getPresenter().validateServerPortNumber(newValue));
		portWrapper.tooltip(getPresenter().serverPortTooltipCode()).get();

		serverPort.getChildren().add(portWrapper.get());
		FlowPane.setMargin(portWrapper.get(), new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(serverPort, 0, 2);
		GridPane.setMargin(serverPort, new Insets(10, 0, 0, 0));

		// OK CANCEL BUTTONS
		// --------------------------------------------------------------------------------------------------

		FlowPane buttons = new FlowPane();
		buttons.setAlignment(Pos.CENTER_RIGHT);

		Button ok = getStyle().createButton(getPresenter().okCode()).onAction(e -> getPresenter().ok(e)).disable(getPresenter().okDisableProperty()).get();
		buttons.getChildren().add(ok);
		FlowPane.setMargin(ok, new Insets(0, 10, 0, 0));

		Button cancel = getStyle().createButton(getPresenter().cancelCode()).onAction(e -> getPresenter().cancel(e)).get();
		buttons.getChildren().add(cancel);
		FlowPane.setMargin(cancel, new Insets(0, 0, 0, 10));

		getRoot().add(buttons, 0, 3);
		GridPane.setMargin(buttons, new Insets(50, 0, 0, 0));

		// --------------------------------------------------------------------------------------------------

		getPresenter().getStage().titleProperty().bind(getStyle().getPropertyHelper().languageProperty(getPresenter().titleCode()));
		getPresenter().getStage().setScene(new Scene(getRoot()));
		getPresenter().getStage().sizeToScene();
		getPresenter().getStage().setResizable(false);
		getPresenter().getStage().initOwner(initOwner);
		getPresenter().getStage().initModality(Modality.APPLICATION_MODAL);
		getPresenter().getStage().show();

		maxWidthLabel(serverNameLabel, serverIpAddressLabel, serverPortLabel);
		maxWidthTextField(nameWrapper.get(), ipWrapper.get(), portWrapper.get());
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
