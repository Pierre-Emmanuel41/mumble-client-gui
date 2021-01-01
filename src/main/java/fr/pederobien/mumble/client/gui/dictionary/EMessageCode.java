package fr.pederobien.mumble.client.gui.dictionary;

import fr.pederobien.dictionary.interfaces.IMessageCode;

public enum EMessageCode implements IMessageCode {
	EMPTY_SERVER_LIST;

	@Override
	public String value() {
		return toString();
	}
}
