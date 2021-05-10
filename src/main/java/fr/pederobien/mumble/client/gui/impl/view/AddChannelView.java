package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.AddChannelPresenter;
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

public class AddChannelView extends ViewBase<AddChannelPresenter, GridPane> {
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;

	public AddChannelView(Stage initOwner, AddChannelPresenter presenter) {
		super(presenter, new GridPane());

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

		FlowPane channelName = new FlowPane();
		channelName.setPrefWidth(490);

		Label channelNameLabel = new Label();
		channelNameLabel.fontProperty().bind(getPresenter().fontProperty());
		channelNameLabel.textProperty().bind(getPresenter().channelNameTextProperty());
		channelName.getChildren().add(channelNameLabel);

		TextField channelNameTextField = new TextField();
		channelNameTextField.fontProperty().bind(getPresenter().fontProperty());
		channelNameTextField.promptTextProperty().bind(getPresenter().channelNamePromptProperty());
		channelNameTextField.textProperty().bindBidirectional(getPresenter().channelNameProperty());
		channelNameTextField.borderProperty().bind(getPresenter().channelNameBorderProperty());
		channelNameTextField.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateChannelName());
		channelNameTextField.tooltipProperty().bind(getPresenter().channelNameTooltipProperty());

		channelName.getChildren().add(channelNameTextField);
		FlowPane.setMargin(channelNameTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		getRoot().add(channelName, 0, 2);
		GridPane.setMargin(channelName, new Insets(10, 0, 0, 0));

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

		stage.setScene(new Scene(getRoot()));
		stage.sizeToScene();
		stage.setResizable(false);
		stage.initOwner(initOwner);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();

		channelNameTextField.setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - channelNameLabel.getWidth() - marginBetweenLabelAndTextField);
	}
}
