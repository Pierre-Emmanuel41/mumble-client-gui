package fr.pederobien.mumble.client.gui.environment;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;

public class AbstractEnvironment implements IEnvironment {
	private String url;
	private IGuiConfiguration guiconfiguration;

	protected AbstractEnvironment(String url, IGuiConfiguration guiConfiguration) {
		this.url = url;
		this.guiconfiguration = guiConfiguration;
	}

	@Override
	public void registerDictionary(String dictionaryName) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return The url used to know if the environment is a development environment or a production environment.
	 */
	protected String getUrl() {
		return url;
	}

	/**
	 * @return The gui configuration associated to this environment.
	 */
	protected IGuiConfiguration getGuiConfiguration() {
		return guiconfiguration;
	}

	/**
	 * Get the path associated to the given dictionary name.
	 * 
	 * @param dictionaryName The dictionary name used to get the path associated to the its file.
	 * @return The concatenated path.
	 */
	protected String getDictionaryPath(String dictionaryName) {
		return Variables.DICTIONARIES_FOLDER.getFileName().concat(dictionaryName);
	}
}
