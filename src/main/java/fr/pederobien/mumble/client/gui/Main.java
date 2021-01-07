package fr.pederobien.mumble.client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private MainPresenter mainPresenter;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		mainPresenter = new MainPresenter();
		mainPresenter.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(mainPresenter.start(primaryStage));

		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		mainPresenter.stop();
	}
}
