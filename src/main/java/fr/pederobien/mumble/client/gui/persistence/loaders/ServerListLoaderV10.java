package fr.pederobien.mumble.client.gui.persistence.loaders;

import org.w3c.dom.Element;

import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.persistence.interfaces.xml.IXmlPersistenceLoader;

public class ServerListLoaderV10 extends AbstractServerListLoader {

	public ServerListLoaderV10() {
		super(1.0);
	}

	@Override
	public IXmlPersistenceLoader<ServerList> load(Element root) {
		createNewElement();
		setServers(root);
		return this;
	}
}
