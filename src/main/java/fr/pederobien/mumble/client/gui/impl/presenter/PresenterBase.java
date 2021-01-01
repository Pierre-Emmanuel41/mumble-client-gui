package fr.pederobien.mumble.client.gui.impl.presenter;

import javafx.stage.Stage;

public abstract class PresenterBase {
	private Stage primaryStage;

	protected PresenterBase(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * @return The primary stage of the application.
	 */
	protected Stage getPrimaryStage() {
		return primaryStage;
	}
}
