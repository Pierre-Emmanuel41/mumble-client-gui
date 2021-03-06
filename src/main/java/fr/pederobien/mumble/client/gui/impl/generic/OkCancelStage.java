package fr.pederobien.mumble.client.gui.impl.generic;

import fr.pederobien.mumble.client.gui.impl.view.ViewBase;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OkCancelStage extends ViewBase<OkCancelPresenter, GridPane> {
	private double marginBetweenRootAndChildren = 10.0;

	public OkCancelStage(Stage initOwner, IOkCancelView view) {
		super(view.getPresenter(), new GridPane());

		Stage stage = new Stage();
		stage.titleProperty().bind(getPresenter().titleTextProperty());

		getRoot().setPadding(new Insets(marginBetweenRootAndChildren));
		getRoot().setAlignment(Pos.CENTER);
		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ENTER && getPresenter().onOkButtonClicked())
				stage.close();
		});

		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE && getPresenter().onCancelButtonClicked())
				stage.close();
		});

		getRoot().add(view.getRoot(), 0, 0);

		// OK CANCEL BUTTONS
		// --------------------------------------------------------------------------------------------------

		FlowPane buttons = new FlowPane();
		buttons.setAlignment(Pos.CENTER_RIGHT);

		Button ok = new Button();
		ok.fontProperty().bind(getPresenter().fontProperty());
		ok.textProperty().bind(getPresenter().okTextProperty());
		ok.setOnAction(e -> {
			if (getPresenter().onOkButtonClicked())
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

		getRoot().add(buttons, 0, 1);
		GridPane.setMargin(buttons, new Insets(50, 0, 0, 0));

		stage.setScene(new Scene(getRoot()));
		stage.sizeToScene();
		stage.setResizable(false);
		stage.initOwner(initOwner);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();

		view.onPostShown();
	}
}
