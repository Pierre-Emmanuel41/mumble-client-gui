package fr.pederobien.mumble.client.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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

		ScrollPane root = new ScrollPane();
		root.setFitToHeight(true);
		root.setFitToWidth(true);
		root.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		root.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		BorderPane secondaryRoot = new BorderPane();
		root.setContent(secondaryRoot);
		secondaryRoot.setCenter(mainPresenter.getServerListView().getRoot());
		secondaryRoot.setBottom(mainPresenter.getServerManagementView().getRoot());

		BorderPane.setAlignment(mainPresenter.getServerListView().getRoot(), Pos.CENTER);
		BorderPane.setAlignment(mainPresenter.getServerManagementView().getRoot(), Pos.CENTER);

		primaryStage.setScene(new Scene(root));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}
