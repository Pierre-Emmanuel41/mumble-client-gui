package fr.pederobien.mumble.client.gui.persistence.configuration;

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
	 * 
	 * @return True if the save went well.
	 */
	public boolean serialize() {
		return persistence.serialize(guiConfiguration, IPersistence.LATEST,
				Variables.MUMBLE_FOLDER.getPath().resolve(Variables.GUI_CONFIGURATION.getFileName()).toString());
	}

	/**
	 * Load the gui configuration at the following path: <code>%appdata%/.mumble/serverList.xml</code>.
	 * 
	 * @return True if the load went well.
	 */
	public boolean deserialize() {
		return persistence.deserialize(guiConfiguration, Variables.MUMBLE_FOLDER.getPath().resolve(Variables.GUI_CONFIGURATION.getFileName()).toString());
	}

	/**
	 * @return The gui configuration associated to this persistence.
	 */
	public IGuiConfiguration getGuiConfiguration() {
		return guiConfiguration;
	}
}
