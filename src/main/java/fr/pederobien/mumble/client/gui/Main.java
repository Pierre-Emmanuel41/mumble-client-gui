package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.impl.Environment;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.persistence.ServerListPersistence;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerListPersistence persistence = (ServerListPersistence) ServerListPersistence.getInstance();
		try {
			persistence.load(Environment.SERVER_LIST.getFileName());
		} catch (FileNotFoundException e) {
			persistence.saveDefault();
		}

		ServerListView ServerListView = new ServerListView(new ServerListPresenter(primaryStage, persistence.get()));
		Scene scene = new Scene(ServerListView.getRoot());

		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
