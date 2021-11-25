package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.presenter.AlertPresenter;
import fr.pederobien.mumble.client.gui.impl.presenter.MainPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.PropertyHelper;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import fr.pederobien.utils.ApplicationLock;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MumbleClientApplication extends Application {
	private static PropertyHelper propertyHelper;
	private static Stage primaryStage;
	private static ApplicationLock lock;

	public static void main(String[] args) {
		lock = new ApplicationLock(Variables.LOCK_FILE.getFileName(), Variables.MUMBLE_FOLDER.getPath());
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MumbleClientApplication.primaryStage = primaryStage;

		try {
			GuiConfigurationPersistence.getInstance().load(Variables.GUI_CONFIGURATION.getFileName());
		} catch (FileNotFoundException e) {
			GuiConfigurationPersistence.getInstance().saveDefault();
		}

		propertyHelper = new PropertyHelper(GuiConfigurationPersistence.getInstance().get());
		Environments.registerDictionaries();

		if (!lock.lock()) {
			AlertPresenter presenter = new AlertPresenter(AlertType.ERROR);
			presenter.title(EMessageCode.APPLICATION_ALREADY_RUNNING_TITLE);
			presenter.header(EMessageCode.APPLICATION_ALREADY_RUNNING_HEADER);
			presenter.getAlert().showAndWait();
			Platform.exit();
			return;
		}

		try {
			ServerListPersistence.getInstance().load(Variables.SERVER_LIST.getFileName());
		} catch (FileNotFoundException e) {
			ServerListPersistence.getInstance().saveDefault();
		}

		MainView mainView = new MainView(new MainPresenter());
		primaryStage.setScene(new Scene(mainView.getRoot()));
		primaryStage.setMaximized(true);
		primaryStage.show();
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
