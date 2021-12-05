package fr.pederobien.mumble.client.gui.persistence.model.loaders;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.persistence.model.ServersXmlTag;
import fr.pederobien.persistence.impl.xml.AbstractXmlSerializer;

public abstract class AbstractServerListSerializer extends AbstractXmlSerializer<ServerList> {

	protected AbstractServerListSerializer(Double version) {
		super(version);
	}

	/**
	 * Retrieve registered servers in the given xml root and add them to the server list.
	 * 
	 * @param element The server list to update.
	 * @param root    The xml root that contains all servers.
	 */
	protected void setServers(ServerList element, Element root) {
		element.clear();
		NodeList servers = getElementsByTagName(root, ServersXmlTag.SERVER);
		for (int i = 0; i < servers.getLength(); i++) {
			Element s = (Element) servers.item(i);
			String name = getStringAttribute(s, ServersXmlTag.NAME);
			String address = getStringAttribute(s, ServersXmlTag.SERVER_ADDRESS);
			int port = getIntAttribute(s, ServersXmlTag.SERVER_PORT);
			element.add(ServerList.createNewServer(name, address, port));
		}
	}
}
