package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.configuration.persistence.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.impl.Environment;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ViewBase;
import fr.pederobien.mumble.client.gui.persistence.ServerListPersistence;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		try {
			ServerListPersistence.getInstance().load(Environment.SERVER_LIST.getFileName());
		} catch (FileNotFoundException e) {
			ServerListPersistence.getInstance().saveDefault();
		}

		try {
			GuiConfigurationPersistence.getInstance().load(Environment.GUI_CONFIGURATION.getFileName());
		} catch (FileNotFoundException e) {
			GuiConfigurationPersistence.getInstance().saveDefault();
		}

		ViewBase.setGuiConfiguration(GuiConfigurationPersistence.getInstance().get());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerListView ServerListView = new ServerListView(new ServerListPresenter(primaryStage, ServerListPersistence.getInstance().get()));
		Scene scene = new Scene(ServerListView.getRoot());

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void stop() throws Exception {

	}
}
