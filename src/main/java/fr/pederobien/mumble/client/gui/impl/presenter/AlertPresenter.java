package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.Optional;

import fr.pederobien.dictionary.interfaces.ICode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class AlertPresenter extends PresenterBase {
	private Alert alert;

	/**
	 * Creates a presenter that manage a pop-up to display.
	 * 
	 * @param alertType The type of the pop-up.
	 */
	public AlertPresenter(AlertType alertType) {
		alert = new Alert(alertType);
	}

	/**
	 * Creates a String property based on the given message and bind the title property of the under ground alert with it.
	 * 
	 * @param title The message code associated to the title.
	 * @param args  The argument used to format the title.
	 * 
	 * @return This presenter.
	 */
	public AlertPresenter title(ICode title, Object... args) {
		alert.titleProperty().bind(getPropertyHelper().languageProperty(title, args));
		return this;
	}

	/**
	 * Creates a String property based on the given message and bind the header property of the under ground alert with it.
	 * 
	 * @param header The message code associated to the header.
	 * @param args   The argument used to format the header.
	 * 
	 * @return This presenter.
	 */
	public AlertPresenter header(ICode header, Object... args) {
		alert.headerTextProperty().bind(getPropertyHelper().languageProperty(header, args));
		return this;
	}

	/**
	 * Creates a String property based on the given message and bind the content property of the under ground alert with it.
	 * 
	 * @param content The message code associated to the content.
	 * @param args    The argument used to format the content.
	 * 
	 * @return This presenter.
	 */
	public AlertPresenter content(ICode content, Object... args) {
		alert.contentTextProperty().bind(getPropertyHelper().languageProperty(content, args));
		return this;
	}

	/**
	 * Shows the dialog and waits for the user response (in other words, brings up a blocking dialog, with the returned value the
	 * users input).
	 * <p>
	 * This method must be called on the JavaFX Application thread. Additionally, it must either be called from an input event handler
	 * or from the run method of a Runnable passed to {@link javafx.application.Platform#runLater Platform.runLater}. It must not be
	 * called during animation or layout processing.
	 * </p>
	 *
	 * @return An {@link Optional} that contains the {@link #resultProperty() result}. Refer to the {@link Dialog} class documentation
	 *         for more detail.
	 * 
	 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
	 * @throws IllegalStateException if this method is called during animation or layout processing.
	 */
	public Optional<ButtonType> showAndWait() {
		return alert.showAndWait();
	}

	/**
	 * Shows the dialog but does not wait for a user response (in other words, this brings up a non-blocking dialog). Users of this
	 * API must either poll the {@link #resultProperty() result property}, or else add a listener to the result property to be
	 * informed of when it is set.
	 * 
	 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
	 */
	public void show() {
		alert.show();
	}

	/**
	 * @return The alert popup defined by this presenter.
	 */
	public Alert getAlert() {
		return alert;
	}
}
