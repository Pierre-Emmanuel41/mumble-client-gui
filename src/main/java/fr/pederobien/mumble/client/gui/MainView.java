package fr.pederobien.mumble.client.gui;

import fr.pederobien.mumble.client.gui.impl.view.ViewBase;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainView extends ViewBase<MainPresenter, BorderPane> {

	public MainView(MainPresenter presenter, Stage primaryStage) {
		super(presenter, new BorderPane());

		primaryStage.titleProperty().bind(getPresenter().titleLanguageProperty());

		BorderPane root = new BorderPane();
		root.setCenter(getPresenter().getServerListView().getRoot());
		root.setBottom(getPresenter().getServerManagementView().getRoot());

		BorderPane.setAlignment(getPresenter().getServerListView().getRoot(), Pos.CENTER);
		BorderPane.setAlignment(getPresenter().getServerManagementView().getRoot(), Pos.CENTER);

		primaryStage.setScene(new Scene(root));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}
