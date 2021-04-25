package fr.pederobien.mumble.client.gui.environment;

import java.io.FileNotFoundException;
import java.io.IOException;

import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import javafx.scene.image.Image;

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

	/**
	 * Try to find the file associated to given image name. If found, then the file is parsed and an image is returned.
	 * 
	 * @param imageName The image name to load.
	 * 
	 * @return The associated image.
	 * 
	 * @throws IOException If there is no file associated to the given dictionary name.
	 */
	public static Image loadImage(String imageName) throws IOException {
		return current.loadImage(imageName);
	}
}
