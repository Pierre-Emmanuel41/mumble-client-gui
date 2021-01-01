package fr.pederobien.mumble.client.gui.configuration.persistence;

public enum GuiConfigurationXmlTag {
	NAME("name"), LOCALE("locale");

	private String name;

	private GuiConfigurationXmlTag(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
