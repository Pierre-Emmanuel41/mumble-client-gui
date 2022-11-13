package fr.pederobien.mumble.client.gui.impl.generic;

import fr.pederobien.dictionary.interfaces.ICode;
import fr.pederobien.mumble.client.gui.impl.EMessageCode;

public enum ButtonType {
	/**
	 * Creates a button with the text "ok" (language sensitive)
	 */
	OK(EMessageCode.OK),

	/**
	 * Creates a button with the text "cancel" (language sensitive)
	 */
	CANCEL(EMessageCode.CANCEL);

	private ICode code;

	private ButtonType(ICode code) {
		this.code = code;
	}

	/**
	 * @return The code associated to the text of the button
	 */
	public ICode getCode() {
		return code;
	}
}
