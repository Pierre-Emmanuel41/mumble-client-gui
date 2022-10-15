package fr.pederobien.mumble.client.gui;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Environments;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.presenter.AlertPresenter;
import fr.pederobien.mumble.client.gui.impl.presenter.MainPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.PropertyHelper;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import fr.pederobien.mumble.client.player.event.MumbleGamePortCheckPostEvent;
import fr.pederobien.utils.ApplicationLock;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MumbleClientApplication extends Application implements IEventListener {
	private static PropertyHelper propertyHelper;
	private static Stage primaryStage;
	private static ApplicationLock lock;

	/**
	 * Run the main application.
	 * 
	 * @param args The command line arguments passed to the application.
	 */
	public void run(String[] args) {
		lock = new ApplicationLock(Variables.LOCK_FILE.getFileName(), Variables.MUMBLE_FOLDER.getPath());

		if (args.length > 0 && args[0].equalsIgnoreCase("true"))
			EventManager.registerListener(this);

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MumbleClientApplication.primaryStage = primaryStage;

		GuiConfigurationPersistence.getInstance().deserialize();

		propertyHelper = new PropertyHelper(GuiConfigurationPersistence.getInstance().getGuiConfiguration());
		Environments.registerDictionaries();

		if (!lock.lock()) {
			AlertPresenter presenter = new AlertPresenter(AlertType.ERROR);
			presenter.title(EMessageCode.APPLICATION_ALREADY_RUNNING_TITLE);
			presenter.header(EMessageCode.APPLICATION_ALREADY_RUNNING_HEADER);
			presenter.getAlert().showAndWait();
			Platform.exit();
			return;
		}

		ServerListPersistence.getInstance().deserialize();

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

	@EventHandler
	private void onGamePortCheck(MumbleGamePortCheckPostEvent event) {
		event.setUsed(true);
	}
}
