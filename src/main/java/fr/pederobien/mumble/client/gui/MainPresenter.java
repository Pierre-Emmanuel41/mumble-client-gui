package fr.pederobien.mumble.client.gui;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.pederobien.dictionary.impl.DefaultDictionaryParser;
import fr.pederobien.dictionary.impl.JarDictionaryParser;
import fr.pederobien.mumble.client.gui.configuration.persistence.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.Environment;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.PropertyHelper;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ServerManagementView;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import javafx.beans.property.StringProperty;

public class MainPresenter extends PresenterBase {
	private ServerListView serverListView;
	private ServerManagementView serverManagementView;
	private StringProperty titleLanguageProperty;

	public MainPresenter() {
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

		setPropertyHelper(new PropertyHelper(GuiConfigurationPersistence.getInstance().get()));
		registerDictionaries("French.xml", "English.xml");

		ServerListPresenter serverListPresenter = new ServerListPresenter(ServerListPersistence.getInstance().get());
		serverListView = new ServerListView(serverListPresenter);

		ServerManagementPresenter serverManagementPresenter = new ServerManagementPresenter(ServerListPersistence.getInstance().get());
		serverListPresenter.addObserver(serverManagementPresenter);
		serverManagementView = new ServerManagementView(serverManagementPresenter);

		titleLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.MUMBLE_WINDOW_TITLE);
	}

	@Override
	public void onCloseRequest() {
		GuiConfigurationPersistence.getInstance().save();
		ServerListPersistence.getInstance().save();
	}

	/**
	 * @return The string property corresponding to the title of the stage.
	 */
	public StringProperty titleLanguageProperty() {
		return titleLanguageProperty;
	}

	/**
	 * @return The server list view. This view contains a view for each registered server.
	 */
	public ServerListView getServerListView() {
		return serverListView;
	}

	/**
	 * @return The server management view. this view contains buttons to join, add, edit and delete a server.
	 */
	public ServerManagementView getServerManagementView() {
		return serverManagementView;
	}

	private void registerDictionaries(String... dictionaryNames) {
		String url = getClass().getResource(getClass().getSimpleName() + ".class").toExternalForm();

		try {
			if (url.startsWith("file")) {
				DefaultDictionaryParser parser = new DefaultDictionaryParser();
				for (String name : dictionaryNames)
					GuiConfigurationPersistence.getInstance().get().registerDictionary(parser.parse(Paths.get(Environment.RESOURCES_FOLDER.getFileName(), name)));

			} else if (url.startsWith("jar")) {
				Path jarPath = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("%20", " "));
				String internalPath = Environment.RESOURCES_FOLDER.getFileName();
				JarDictionaryParser parser = new JarDictionaryParser(internalPath);
				for (String name : dictionaryNames)
					GuiConfigurationPersistence.getInstance().get().registerDictionary(parser.setName(internalPath.concat(name)).parse(jarPath));

			} else
				throw new UnsupportedOperationException("Technical error");
		} catch (FileNotFoundException e) {

		}
	}
}
