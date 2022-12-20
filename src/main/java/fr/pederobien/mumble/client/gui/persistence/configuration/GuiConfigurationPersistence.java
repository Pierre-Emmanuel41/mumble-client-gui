package fr.pederobien.mumble.client.gui.persistence.configuration;

import java.io.FileNotFoundException;

import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.GuiConfiguration;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.mumble.client.gui.persistence.configuration.loaders.GuiConfigurationSerializerV10;
import fr.pederobien.persistence.impl.Persistences;
import fr.pederobien.persistence.impl.xml.XmlPersistence;
import fr.pederobien.persistence.interfaces.IPersistence;

public class GuiConfigurationPersistence {
	private XmlPersistence<IGuiConfiguration> persistence;
	private IGuiConfiguration guiConfiguration;

	protected GuiConfigurationPersistence() {
		persistence = Persistences.xmlPersistence();
		persistence.register(persistence.adapt(new GuiConfigurationSerializerV10()));

		guiConfiguration = new GuiConfiguration();
	}

	/**
	 * @return The single instance of the persistence that serialize or deserialize a gui configuration.
	 */
	public static GuiConfigurationPersistence getInstance() {
		return SingletonHolder.PERSISTENCE;
	}

	private static class SingletonHolder {
		private static final GuiConfigurationPersistence PERSISTENCE = new GuiConfigurationPersistence();
	}

	/**
	 * Save the gui configuration at the following path: <code>%appdata%/.mumble/GuiConfiguration.xml</code>.
	 */
	public void serialize() {
		try {
			persistence.serialize(guiConfiguration, IPersistence.LATEST, Variables.MUMBLE_FOLDER.getPath().resolve(Variables.GUI_CONFIGURATION.getFileName()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the gui configuration at the following path: <code>%appdata%/.mumble/serverList.xml</code>.
	 */
	public void deserialize() {
		try {
			persistence.deserialize(guiConfiguration, Variables.MUMBLE_FOLDER.getPath().resolve(Variables.GUI_CONFIGURATION.getFileName()).toString());
		} catch (FileNotFoundException e) {
			// Do nothing, use the default GUI configuration instead.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The gui configuration associated to this persistence.
	 */
	public IGuiConfiguration getGuiConfiguration() {
		return guiConfiguration;
	}
}
