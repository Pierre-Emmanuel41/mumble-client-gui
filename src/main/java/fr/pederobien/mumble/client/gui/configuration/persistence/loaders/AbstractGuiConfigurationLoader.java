package fr.pederobien.mumble.client.gui.configuration.persistence.loaders;

import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.mumble.client.gui.configuration.persistence.GuiConfigurationXmlTag;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistenceLoader;

public abstract class AbstractGuiConfigurationLoader extends AbstractXmlPersistenceLoader<GuiConfiguration> {

	protected AbstractGuiConfigurationLoader(Double version) {
		super(version);
	}

	@Override
	protected GuiConfiguration create() {
		return new GuiConfiguration();
	}

	/**
	 * Set the gui configuration locale.
	 * 
	 * @param root The xml root that contains all configuration parameters.
	 */
	protected void setLocale(Element root) {
		Node locale = getElementsByTagName(root, GuiConfigurationXmlTag.LOCALE).item(0);
		get().setLocale(Locale.forLanguageTag(locale.getChildNodes().item(0).getNodeValue()));
	}
}
