package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.pederobien.dictionary.impl.DefaultDictionaryParser;
import fr.pederobien.dictionary.impl.JarDictionaryParser;
import fr.pederobien.dictionary.impl.NotificationCenter;
import fr.pederobien.dictionary.interfaces.IDictionaryParser;
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
		registerDictionaries("French.xml");
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

	private void registerDictionaries(String... dictionaryNames) {
		String url = getClass().getResource(getClass().getSimpleName() + ".class").toExternalForm();

		if (url.startsWith("file")) {
			DefaultDictionaryParser parser = new DefaultDictionaryParser();
			for (String name : dictionaryNames)
				registerDictionary(parser, Paths.get(Environment.RESOURCES_FOLDER.getFileName(), name));

		} else if (url.startsWith("jar")) {
			Path jarPath = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1));
			String internalPath = Environment.RESOURCES_FOLDER.getFileName();
			JarDictionaryParser parser = new JarDictionaryParser(internalPath);
			for (String name : dictionaryNames)
				registerDictionary(parser.setName(internalPath.concat(name)), jarPath);

		} else
			throw new UnsupportedOperationException("Technical error");
	}

	private void registerDictionary(IDictionaryParser parser, Path path) {
		try {
			NotificationCenter.getInstance().getDictionaryContext().register(parser, path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
