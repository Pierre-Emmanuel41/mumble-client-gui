package fr.pederobien.mumble.client.gui.persistence.loaders;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.pederobien.mumble.client.gui.model.Server;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.persistence.ServersXmlTag;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistenceLoader;

public abstract class AbstractServerListLoader extends AbstractXmlPersistenceLoader<ServerList> {

	protected AbstractServerListLoader(Double version) {
		super(version);
	}

	@Override
	protected ServerList create() {
		return new ServerList();
	}

	/**
	 * Retrieve registered servers in the given xml root and add them to the server list.
	 * 
	 * @param root The xml root that contains all servers.
	 */
	protected void setServers(Element root) {
		get().clear();
		NodeList servers = getElementsByTagName(root, ServersXmlTag.SERVER);
		for (int i = 0; i < servers.getLength(); i++) {
			Element s = (Element) servers.item(i);
			Server server = new Server();
			server.setName(getStringAttribute(s, ServersXmlTag.NAME));
			server.setAddress(getStringAttribute(s, ServersXmlTag.SERVER_ADDRESS));
			server.setPort(getIntAttribute(s, ServersXmlTag.SERVER_PORT));
			get().add(server);
		}
	}
}
