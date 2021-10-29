package fr.pederobien.mumble.client.gui.persistence.configuration;

import java.io.IOException;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.GuiConfiguration;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.mumble.client.gui.persistence.configuration.loaders.GuiConfigurationLoaderV10;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistence;

public class GuiConfigurationPersistence extends AbstractXmlPersistence<IGuiConfiguration> {
	private static final String XML_ROOT_ELEMENT = "GuiConfiguration";

	protected GuiConfigurationPersistence() {
		super(Paths.get(Variables.MUMBLE_FOLDER.getFileName()));
		register(new GuiConfigurationLoaderV10());
	}

	public static GuiConfigurationPersistence getInstance() {
		return SingletonHolder.PERSISTENCE;
	}

	private static class SingletonHolder {
		private static final GuiConfigurationPersistence PERSISTENCE = new GuiConfigurationPersistence();
	}

	@Override
	public boolean save() {
		if (get() == null)
			return true;

		Document doc = newDocument();
		doc.setXmlStandalone(true);

		Element root = createElement(doc, XML_ROOT_ELEMENT);
		doc.appendChild(root);

		Element version = createElement(doc, VERSION);
		version.appendChild(doc.createTextNode(getVersion().toString()));
		root.appendChild(version);

		Element locale = createElement(doc, GuiConfigurationXmlTag.LOCALE);
		locale.appendChild(doc.createTextNode(get().getLocale().getLanguage()));
		root.appendChild(locale);

		Element font = createElement(doc, GuiConfigurationXmlTag.FONT);
		setAttribute(font, GuiConfigurationXmlTag.FONT_FAMILY, get().getFont().getFamily());
		setAttribute(font, GuiConfigurationXmlTag.FONT_SIZE, get().getFont().getSize());
		root.appendChild(font);

		saveDocument(doc, XML_ROOT_ELEMENT);
		return true;
	}

	@Override
	protected Document createDoc(Object... args) throws IOException {
		return parseFromFileName((String) args[0]);
	}

	public void saveDefault() {
		set(new GuiConfiguration());
		save();
	}

}
