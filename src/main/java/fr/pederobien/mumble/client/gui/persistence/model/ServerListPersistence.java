package fr.pederobien.mumble.client.gui.persistence.model;

import fr.pederobien.mumble.client.gui.impl.Variables;
import fr.pederobien.mumble.client.gui.model.ServerList;
import fr.pederobien.mumble.client.gui.persistence.model.loaders.ServerListSerializerV10;
import fr.pederobien.persistence.impl.Persistences;
import fr.pederobien.persistence.impl.xml.XmlPersistence;
import fr.pederobien.persistence.interfaces.IPersistence;

public class ServerListPersistence {
	private XmlPersistence<ServerList> persistence;
	private ServerList serverList;

	private ServerListPersistence() {
		persistence = Persistences.xmlPersistence();
		persistence.register(persistence.adapt(new ServerListSerializerV10()));

		serverList = new ServerList();
	}

	/**
	 * @return The single instance of the persistence that serialize or deserialize a server list.
	 */
	public static ServerListPersistence getInstance() {
		return SingletonHolder.PERSISTENCE;
	}

	private static class SingletonHolder {
		private static final ServerListPersistence PERSISTENCE = new ServerListPersistence();
	}

	/**
	 * Save the server list at the following path: <code>%appdata%/.mumble/serverList.xml</code>.
	 */
	public void serialize() {
		try {
			persistence.serialize(serverList, IPersistence.LATEST, Variables.SERVER_LIST.getPath().toAbsolutePath().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the server list at the following path: <code>%appdata%/.mumble/serverList.xml</code>.
	 */
	public void deserialize() {
		try {
			persistence.deserialize(serverList, Variables.SERVER_LIST.getPath().toAbsolutePath().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The server list associated to this persistence.
	 */
	public ServerList getServerList() {
		return serverList;
	}
}
