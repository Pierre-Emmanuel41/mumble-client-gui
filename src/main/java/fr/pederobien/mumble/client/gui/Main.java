package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.pederobien.dictionary.impl.DefaultDictionaryParser;
import fr.pederobien.dictionary.impl.JarDictionaryParser;
import fr.pederobien.dictionary.impl.NotificationCenter;
import fr.pederobien.mumble.client.gui.configuration.persistence.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.impl.Environment;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ServerManagementView;
import fr.pederobien.mumble.client.gui.persistence.ServerListPersistence;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
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

		PresenterBase.setGuiConfiguration(GuiConfigurationPersistence.getInstance().get());
		registerDictionaries("French.xml", "English.xml");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		PresenterBase.setPrimaryStage(primaryStage);
		ScrollPane root = new ScrollPane();
		root.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		root.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		root.setContent(root);
		root.setFitToHeight(true);
		root.setFitToWidth(true);

		BorderPane secondaryRoot = new BorderPane();
		root.setContent(secondaryRoot);

		ServerListPresenter serverListPresenter = new ServerListPresenter(ServerListPersistence.getInstance().get());
		ServerListView serverListView = new ServerListView(serverListPresenter);

		ServerManagementPresenter serverManagementPresenter = new ServerManagementPresenter(ServerListPersistence.getInstance().get());
		serverListPresenter.addObserver(serverManagementPresenter);
		ServerManagementView serverManagementView = new ServerManagementView(serverManagementPresenter);

		secondaryRoot.setCenter(serverListView.getRoot());
		secondaryRoot.setBottom(serverManagementView.getRoot());

		BorderPane.setAlignment(serverListView.getRoot(), Pos.CENTER);
		BorderPane.setAlignment(serverManagementView.getRoot(), Pos.CENTER);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		GuiConfigurationPersistence.getInstance().save();
		ServerListPersistence.getInstance().get().getServers().forEach(server -> server.dispose());
		ServerListPersistence.getInstance().save();
	}

	private void registerDictionaries(String... dictionaryNames) {
		String url = getClass().getResource(getClass().getSimpleName() + ".class").toExternalForm();

		try {
			if (url.startsWith("file")) {
				DefaultDictionaryParser parser = new DefaultDictionaryParser();
				for (String name : dictionaryNames)
					NotificationCenter.getInstance().getDictionaryContext().register(parser, Paths.get(Environment.RESOURCES_FOLDER.getFileName(), name));

			} else if (url.startsWith("jar")) {
				Path jarPath = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1));
				String internalPath = Environment.RESOURCES_FOLDER.getFileName();
				JarDictionaryParser parser = new JarDictionaryParser(internalPath);
				for (String name : dictionaryNames)
					NotificationCenter.getInstance().getDictionaryContext().register(parser.setName(internalPath.concat(name)), jarPath);

			} else
				throw new UnsupportedOperationException("Technical error");
		} catch (FileNotFoundException e) {

		}
	}
}
