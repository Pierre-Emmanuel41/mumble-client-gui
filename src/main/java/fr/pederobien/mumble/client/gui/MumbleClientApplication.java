package fr.pederobien.mumble.client.gui;

import fr.pederobien.javafx.configuration.impl.GuiHelper;
import fr.pederobien.javafx.configuration.interfaces.IEnvironmentVariable;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.Variables;
import fr.pederobien.mumble.client.gui.impl.presenter.AlertPresenter;
import fr.pederobien.mumble.client.gui.impl.presenter.MainPresenter;
import fr.pederobien.mumble.client.gui.impl.view.MainView;
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
	private static Stage primaryStage;
	private static ApplicationLock lock;

	/**
	 * Run the main application.
	 * 
	 * @param args The command line arguments passed to the application.
	 */
	public void run(String[] args) {
		lock = new ApplicationLock(Variables.LOCK_FILE.getPath().toFile().getName(), Variables.LOCK_FILE.getPath().getParent());

		if (args.length > 0 && args[0].equalsIgnoreCase("true"))
			EventManager.registerListener(this);

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MumbleClientApplication.primaryStage = primaryStage;

		GuiHelper.deserialize(Variables.MUMBLE_FOLDER.getPath().toString(), GuiHelper.getConfiguration());
		for (IEnvironmentVariable variable : Variables.values())
			GuiHelper.add(variable);

		String[] dictionaries = new String[] { "English.xml", "French.xml" };
		for (String dictionary : dictionaries)
			GuiHelper.registerDictionary(dictionary);

		if (!lock.lock()) {
			AlertPresenter presenter = new AlertPresenter(AlertType.ERROR);
			presenter.title(EGuiCode.APPLICATION_ALREADY_RUNNING_TITLE);
			presenter.header(EGuiCode.APPLICATION_ALREADY_RUNNING_HEADER);
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
