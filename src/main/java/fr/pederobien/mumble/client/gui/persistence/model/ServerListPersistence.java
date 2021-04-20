package fr.pederobien.mumble.client.gui.persistence.model;

import java.io.IOException;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.persistence.model.loaders.ServerListLoaderV10;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistence;

public class ServerListPersistence extends AbstractXmlPersistence<ServerList> {
	private static final String ROOT_XML_DOCUMENT = "ServerList";

	private ServerListPersistence() {
		super(Paths.get(System.getenv("APPDATA"), ".mumble"));
		register(new ServerListLoaderV10());
	}

	public static ServerListPersistence getInstance() {
		return SingletonHolder.PERSISTENCE;
	}

	private static class SingletonHolder {
		private static final ServerListPersistence PERSISTENCE = new ServerListPersistence();
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

		Element servers = createElement(doc, ServersXmlTag.SERVERS);
		for (Server s : get().getServers()) {
			Element server = createElement(doc, ServersXmlTag.SERVER);
			setAttribute(server, ServersXmlTag.NAME, s.getName());
			setAttribute(server, ServersXmlTag.SERVER_ADDRESS, s.getAddress());
			setAttribute(server, ServersXmlTag.SERVER_PORT, s.getPort());
			servers.appendChild(server);
		}
		root.appendChild(servers);

		saveDocument(doc, ROOT_XML_DOCUMENT);
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
