package fr.pederobien.mumble.client.gui.configuration.persistence;

public enum GuiConfigurationXmlTag {
	NAME("name"), LOCALE("locale"), FONT("font"), FONT_FAMILY("fontFamily"), FONT_SIZE("fontSize");

	private String name;

	private GuiConfigurationXmlTag(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
