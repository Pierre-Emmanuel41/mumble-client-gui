package fr.pederobien.mumble.client.gui.persistence.model.loaders;

import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.persistence.model.ServersXmlTag;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;

public class ServerListSerializerV10 extends AbstractServerListSerializer {

	public ServerListSerializerV10() {
		super(1.0);
	}

	@Override
	public boolean serialize(ServerList element, Element root) {
		Element servers = createElement(ServersXmlTag.SERVERS);
		for (IPlayerMumbleServer server : element.getServers()) {
			Element serverElement = createElement(ServersXmlTag.SERVER);
			setAttribute(serverElement, ServersXmlTag.NAME, server.getName());
			setAttribute(serverElement, ServersXmlTag.SERVER_ADDRESS, server.getAddress().getAddress().getHostAddress());
			setAttribute(serverElement, ServersXmlTag.SERVER_PORT, server.getAddress().getPort());
			servers.appendChild(serverElement);
		}
		root.appendChild(servers);
		return true;
	}

	@Override
	public boolean deserialize(ServerList element, Element root) {
		setServers(element, root);
		return true;
	}
}
