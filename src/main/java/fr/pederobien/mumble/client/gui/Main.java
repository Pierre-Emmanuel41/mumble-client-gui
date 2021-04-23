package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.presenter.MainPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.PropertyHelper;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private static PropertyHelper propertyHelper;
	private static Stage primaryStage;

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

		propertyHelper = new PropertyHelper(GuiConfigurationPersistence.getInstance().get());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		new MainView(new MainPresenter(), primaryStage);
	}

	/**
	 * @return The helper that creates properties.
	 */
	public static PropertyHelper getPropertyHelper() {
		return propertyHelper;
	}

	/**
	 * @return The primary stage of the application.
	 */
	public static Stage getStage() {
		return primaryStage;
	}
}
