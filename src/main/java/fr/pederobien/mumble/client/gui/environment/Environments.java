package fr.pederobien.mumble.client.gui.environment;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;

public class Environments {
	private static final String[] DICTIONARIES_LIST;
	private static IEnvironment development, production, current;

	static {
		DICTIONARIES_LIST = new String[] { "French.xml", "English.xml" };
		new Environments();
	}

	private Environments() {
		String url = getClass().getResource(getClass().getSimpleName() + ".class").toExternalForm();
		development = new DevelopmentEnvironment(url, GuiConfigurationPersistence.getInstance().get());
		production = new ProductionEnvironment(url, GuiConfigurationPersistence.getInstance().get());
		current = url.startsWith(IEnvironment.FILE_PREFIX) ? development : url.startsWith(IEnvironment.JAR_PREFIX) ? production : null;

		if (current == null)
			throw new UnsupportedOperationException("Technical error, the environment is neither a development environment nor a production environment");
	}

	/**
	 * Register all dictionaries in order to display messages in user interface.
	 */
	public static void registerDictionaries() {
		for (String dictionaryName : DICTIONARIES_LIST)
			try {
				current.registerDictionary(dictionaryName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
}
