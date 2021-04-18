package fr.pederobien.mumble.client.gui.configuration.persistence.loaders;

import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.interfaces.observers.IGuiConfiguration;
import fr.pederobien.persistence.interfaces.xml.IXmlPersistenceLoader;

public class GuiConfigurationLoaderV10 extends AbstractGuiConfigurationLoader {

	public GuiConfigurationLoaderV10() {
		super(1.0);
	}

	@Override
	public IXmlPersistenceLoader<IGuiConfiguration> load(Element root) {
		createNewElement();
		setLocale(root);
		setFont(root);
		return this;
	}
}
