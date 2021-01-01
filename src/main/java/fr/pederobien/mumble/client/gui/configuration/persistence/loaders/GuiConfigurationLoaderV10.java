package fr.pederobien.mumble.client.gui.configuration.persistence.loaders;

import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.persistence.interfaces.xml.IXmlPersistenceLoader;

public class GuiConfigurationLoaderV10 extends AbstractGuiConfigurationLoader {

	public GuiConfigurationLoaderV10() {
		super(1.0);
	}

	@Override
	public IXmlPersistenceLoader<GuiConfiguration> load(Element root) {
		createNewElement();

		setLocale(root);
		return this;
	}
}
