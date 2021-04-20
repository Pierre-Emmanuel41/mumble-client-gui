package fr.pederobien.mumble.client.gui.persistence.configuration.loaders;

import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.pederobien.mumble.client.gui.impl.GuiConfiguration;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationXmlTag;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistenceLoader;
import javafx.scene.text.Font;

public abstract class AbstractGuiConfigurationLoader extends AbstractXmlPersistenceLoader<IGuiConfiguration> {

	protected AbstractGuiConfigurationLoader(Double version) {
		super(version);
	}

	@Override
	protected IGuiConfiguration create() {
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

	/**
	 * Set the gui configuration font.
	 * 
	 * @param root The xml root that contains all configuration parameters.
	 */
	protected void setFont(Element root) {
		Element font = (Element) getElementsByTagName(root, GuiConfigurationXmlTag.FONT).item(0);
		get().setFont(Font.font(getStringAttribute(font, GuiConfigurationXmlTag.FONT_FAMILY), getDoubleAttribute(font, GuiConfigurationXmlTag.FONT_SIZE)));
	}
}
