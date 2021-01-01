package fr.pederobien.mumble.client.gui.persistence;

import java.io.IOException;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.persistence.loaders.ServerListLoaderV10;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistence;
import fr.pederobien.persistence.interfaces.IPersistence;

public class ServerListPersistence extends AbstractXmlPersistence<ServerList> {
	private static final String ROOT_XML_DOCUMENT = "ServerList";

	private ServerListPersistence() {
		super(Paths.get(System.getenv("APPDATA"), ".mumble"));
		register(new ServerListLoaderV10());
	}

	public static IPersistence<ServerList> getInstance() {
		return SingletonHolder.PERSISTENCE;
	}

	private static class SingletonHolder {
		private static final IPersistence<ServerList> PERSISTENCE = new ServerListPersistence();
	}

	@Override
	public boolean save() {
		if (get() == null)
			return true;

		Document doc = newDocument();
		doc.setXmlStandalone(true);

		Element root = createElement(doc, ROOT_XML_DOCUMENT);
		doc.appendChild(root);

		Element version = createElement(doc, VERSION);
		version.appendChild(doc.createTextNode(getVersion().toString()));
		root.appendChild(version);

		Element name = createElement(doc, ServersXmlTag.NAME);
		name.appendChild(doc.createTextNode(get().getName()));
		root.appendChild(name);

		Element servers = createElement(doc, ServersXmlTag.SERVERS);
		for (Server s : get().getServers()) {
			Element server = createElement(doc, ServersXmlTag.SERVER);
			setAttribute(server, ServersXmlTag.NAME, s.getName());
			setAttribute(server, ServersXmlTag.SERVER_ADDRESS, s.getAddress());
			setAttribute(server, ServersXmlTag.SERVER_PORT, s.getPort());
			servers.appendChild(server);
		}
		root.appendChild(servers);

		saveDocument(doc, get().getName());
		return true;
	}

	@Override
	protected Document createDoc(Object... args) throws IOException {
		return parseFromFileName((String) args[0]);
	}

	public void saveDefault() {
		set(new ServerList());
		save();
	}
}
