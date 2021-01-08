package fr.pederobien.mumble.client.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	private MainPresenter mainPresenter;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		mainPresenter = new MainPresenter();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainPresenter.setPrimaryStage(primaryStage);

		primaryStage.titleProperty().bind(mainPresenter.titleLanguageProperty());

		BorderPane root = new BorderPane();
		root.setCenter(mainPresenter.getServerListView().getRoot());
		root.setBottom(mainPresenter.getServerManagementView().getRoot());

		BorderPane.setAlignment(mainPresenter.getServerListView().getRoot(), Pos.CENTER);
		BorderPane.setAlignment(mainPresenter.getServerManagementView().getRoot(), Pos.CENTER);

		primaryStage.setScene(new Scene(root));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}
