package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.properties.PropertyHelper;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		try {
			ServerListPersistence.getInstance().load(Variables.SERVER_LIST.getFileName());
		} catch (FileNotFoundException e) {
			ServerListPersistence.getInstance().saveDefault();
		}

		try {
			GuiConfigurationPersistence.getInstance().load(Variables.GUI_CONFIGURATION.getFileName());
		} catch (FileNotFoundException e) {
			GuiConfigurationPersistence.getInstance().saveDefault();
		}

		Environments.registerDictionaries();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new MainView(new MainPresenter(new PropertyHelper(GuiConfigurationPersistence.getInstance().get()), primaryStage), primaryStage);
	}
}
