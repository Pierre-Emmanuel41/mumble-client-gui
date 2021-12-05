package fr.pederobien.mumble.client.gui.persistence.configuration.loaders;

import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationXmlTag;
import fr.pederobien.persistence.impl.xml.AbstractXmlSerializer;
import javafx.scene.text.Font;

public abstract class AbstractGuiConfigurationSerializer extends AbstractXmlSerializer<IGuiConfiguration> {

	protected AbstractGuiConfigurationSerializer(Double version) {
		super(version);
	}

	/**
	 * Set the gui configuration locale.
	 * 
	 * @param element The gui configuration to update.
	 * @param root    The xml root that contains all configuration parameters.
	 */
	protected void setLocale(IGuiConfiguration element, Element root) {
		Node locale = getElementsByTagName(root, GuiConfigurationXmlTag.LOCALE).item(0);
		element.setLocale(Locale.forLanguageTag(locale.getChildNodes().item(0).getNodeValue()));
	}

	/**
	 * Set the gui configuration font.
	 * 
	 * @param element The gui configuration to update.
	 * @param root    The xml root that contains all configuration parameters.
	 */
	protected void setFont(IGuiConfiguration element, Element root) {
		Element font = (Element) getElementsByTagName(root, GuiConfigurationXmlTag.FONT).item(0);
		element.setFont(Font.font(getStringAttribute(font, GuiConfigurationXmlTag.FONT_FAMILY), getDoubleAttribute(font, GuiConfigurationXmlTag.FONT_SIZE)));
	}
}
