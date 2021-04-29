package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertPresenter extends PresenterBase {
	private Alert alert;

	public AlertPresenter(AlertType alertType) {
		alert = new Alert(alertType);
	}

	/**
	 * Creates a String property based on the given message and bind the title property of the under ground alert with it.
	 * 
	 * @param title The message code associated to the title.
	 * @param args  The argument used to format the title.
	 */
	public void setTitle(IMessageCode title, Object... args) {
		alert.titleProperty().bind(getPropertyHelper().languageProperty(title, args));
	}

	/**
	 * Creates a String property based on the given message and bind the header property of the under ground alert with it.
	 * 
	 * @param header The message code associated to the header.
	 * @param args   The argument used to format the header.
	 */
	public void setHeader(IMessageCode header, Object... args) {
		alert.headerTextProperty().bind(getPropertyHelper().languageProperty(header, args));
	}

	/**
	 * Creates a String property based on the given message and bind the content property of the under ground alert with it.
	 * 
	 * @param content The message code associated to the content.
	 * @param args    The argument used to format the content.
	 */
	public void setContent(IMessageCode content, Object... args) {
		alert.contentTextProperty().bind(getPropertyHelper().languageProperty(content, args));
	}

	/**
	 * @return The alert popup defined by this presenter.
	 */
	public Alert getAlert() {
		return alert;
	}
}
