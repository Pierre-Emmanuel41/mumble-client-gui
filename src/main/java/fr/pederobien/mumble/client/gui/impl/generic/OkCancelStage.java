package fr.pederobien.mumble.client.gui.impl.generic;

import fr.pederobien.mumble.client.gui.impl.view.ViewBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class OkCancelStage extends ViewBase<OkCancelPresenter, GridPane> {
	private double marginBetweenRootAndChildren = 10.0;
	private Stage stage;

	public OkCancelStage(Stage initOwner, OkCancelPresenter presenter) {
		super(presenter, new GridPane());

		stage = new Stage();
		stage.titleProperty().bind(getPresenter().titleTextProperty());
		stage.setOnCloseRequest(e -> getPresenter().onClosing());

		getRoot().setAlignment(Pos.CENTER);
		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ENTER && getPresenter().getFormView().getPresenter().onButtonClicked(ButtonType.OK))
				stage.fireEvent(new WindowEvent(initOwner, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		getRoot().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE && getPresenter().getFormView().getPresenter().onButtonClicked(ButtonType.CANCEL))
				stage.fireEvent(new WindowEvent(initOwner, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		getRoot().setPadding(new Insets(marginBetweenRootAndChildren));
		getRoot().add(getPresenter().getFormView().getRoot(), 0, 0);

		// OK CANCEL BUTTONS
		// --------------------------------------------------------------------------------------------------

		Button ok = getPresenter().getFormView().getButton(ButtonType.OK);
		ok.setOnAction(e -> {
			if (getPresenter().getFormView().getPresenter().onButtonClicked(ButtonType.OK))
				stage.fireEvent(new WindowEvent(initOwner, WindowEvent.WINDOW_CLOSE_REQUEST));
		});
		ok.disableProperty().bind(getPresenter().okDisableProperty());

		Button cancel = getPresenter().getFormView().getButton(ButtonType.CANCEL);
		cancel.setOnAction(e -> {
			if (getPresenter().getFormView().getPresenter().onButtonClicked(ButtonType.CANCEL))
				stage.fireEvent(new WindowEvent(initOwner, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		getPresenter().getFormView().setRowMargin(5, 0, 5, 0);
		getPresenter().getFormView().setColumnMargin(0, 10, 0, 0);
		getPresenter().getFormView().setButtonsMargin(50, 0, 0, 0);
		getPresenter().getFormView().setButtonMargin(ButtonType.OK, 0, 0, 0, 10);
		getPresenter().getFormView().setButtonMargin(ButtonType.CANCEL, 0, 10, 0, 0);

		stage.setScene(new Scene(getRoot()));
		stage.sizeToScene();
		stage.setResizable(false);
		stage.initOwner(initOwner);
		stage.initModality(Modality.APPLICATION_MODAL);
	}

	/**
	 * Attempts to show this Window by setting visibility to true
	 *
	 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
	 */
	public void show() {
		stage.show();
	}

	/**
	 * Set the width and height of this Window to match the size of the content of this Window's Scene.
	 */
	public void sizeToScene() {
		stage.setResizable(true);
		stage.sizeToScene();
		stage.setResizable(false);
	}

	/**
	 * @return The stage associated to this generic view.
	 */
	public Stage getStage() {
		return stage;
	}
}
