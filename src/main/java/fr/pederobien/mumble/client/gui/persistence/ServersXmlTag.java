package fr.pederobien.mumble.client.gui.persistence;

public enum ServersXmlTag {
	NAME("name"), SERVERS("servers"), SERVER("server"), SERVER_ADDRESS("address"), SERVER_PORT("port");

	private String name;

	private ServersXmlTag(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
