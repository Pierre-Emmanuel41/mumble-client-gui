package fr.pederobien.mumble.client.gui.impl.generic;

import fr.pederobien.dictionary.interfaces.ICode;
import fr.pederobien.messenger.interfaces.IErrorCode;
import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.presenter.AlertPresenter;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import javafx.scene.control.Alert.AlertType;

public class ErrorPresenter extends PresenterBase {
	private static AlertPresenter alertPresenter;

	/**
	 * Creates a pop-up in order to display a message to the user. Shows the dialog but does not wait for a user response (in other
	 * words, this brings up a non-blocking dialog). Users of this API must either poll the {@link #resultProperty() result property},
	 * or else add a listener to the result property to be informed of when it is set.
	 * 
	 * @param alertType The type of alert.
	 * @param title     The code associated to the title of the alert box.
	 * @param header    The code associated to the header of the alert box.
	 * @param response  The response received from the server.
	 * 
	 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
	 */
	public static void show(AlertType alertType, ICode title, ICode header, IResponse response) {
		if (!response.hasFailed())
			return;

		dispatch(() -> {
			alertPresenter = new AlertPresenter(alertType);
			alertPresenter.getAlert().initOwner(getPrimaryStage());
			alertPresenter.title(title);
			alertPresenter.header(header);
			alertPresenter.content(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
			alertPresenter.getAlert().show();
		});
	}

	/**
	 * Creates a pop-up in order to display a message to the user. Shows the dialog but does not wait for a user response (in other
	 * words, this brings up a non-blocking dialog). Users of this API must either poll the {@link #resultProperty() result property},
	 * or else add a listener to the result property to be informed of when it is set.
	 * 
	 * @param alertType The type of alert.
	 * @param title     The code associated to the title of the alert box.
	 * @param header    The code associated to the header of the alert box.
	 * @param errorCode The code of the error to display.
	 * 
	 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
	 */
	public static void show(AlertType alertType, ICode title, ICode header, IErrorCode errorCode) {
		dispatch(() -> {
			alertPresenter = new AlertPresenter(alertType);
			alertPresenter.getAlert().initOwner(getPrimaryStage());
			alertPresenter.title(title);
			alertPresenter.header(header);
			alertPresenter.content(ErrorCodeWrapper.getByErrorCode(errorCode).getMessageCode());
			alertPresenter.getAlert().show();
		});
	}

	/**
	 * Creates a pop-up in order to display a message to the user. Shows the dialog but does not wait for a user response (in other
	 * words, this brings up a non-blocking dialog). Users of this API must either poll the {@link #resultProperty() result property},
	 * or else add a listener to the result property to be informed of when it is set.
	 * 
	 * @param alertType The type of alert.
	 * @param title     The code associated to the title of the alert box.
	 * @param header    The code associated to the header of the alert box.
	 * @param content   The message of the error to display.
	 * 
	 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
	 */
	public static void show(AlertType alertType, ICode title, ICode header, ICode content) {
		dispatch(() -> {
			alertPresenter = new AlertPresenter(alertType);
			alertPresenter.getAlert().initOwner(getPrimaryStage());
			alertPresenter.title(title);
			alertPresenter.header(header);
			alertPresenter.content(content);
			alertPresenter.getAlert().show();
		});
	}

	/**
	 * Creates a pop-up in order to display a message to the user. Shows the dialog and waits for the user response (in other words,
	 * brings up a blocking dialog, with the returned value the users input).
	 * <p>
	 * This method must be called on the JavaFX Application thread. Additionally, it must either be called from an input event handler
	 * or from the run method of a Runnable passed to {@link javafx.application.Platform#runLater Platform.runLater}. It must not be
	 * called during animation or layout processing.
	 * </p>
	 * 
	 * @param alertType The type of alert.
	 * @param title     The code associated to the title of the alert box.
	 * @param header    The code associated to the header of the alert box.
	 * @param response  The response received from the server.
	 */
	public static void showAndWait(AlertType alertType, ICode title, ICode header, IResponse response) {
		if (!response.hasFailed())
			return;

		dispatch(() -> {
			alertPresenter = new AlertPresenter(alertType);
			alertPresenter.getAlert().initOwner(getPrimaryStage());
			alertPresenter.title(title);
			alertPresenter.header(header);
			alertPresenter.content(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
			alertPresenter.getAlert().showAndWait();
		});
	}

	/**
	 * Creates a pop-up in order to display a message to the user. Shows the dialog and waits for the user response (in other words,
	 * brings up a blocking dialog, with the returned value the users input).
	 * <p>
	 * This method must be called on the JavaFX Application thread. Additionally, it must either be called from an input event handler
	 * or from the run method of a Runnable passed to {@link javafx.application.Platform#runLater Platform.runLater}. It must not be
	 * called during animation or layout processing.
	 * </p>
	 * 
	 * @param alertType The type of alert.
	 * @param title     The code associated to the title of the alert box.
	 * @param header    The code associated to the header of the alert box.
	 * @param errorCode The code of the error to display.
	 */
	public static void showAndWait(AlertType alertType, ICode title, ICode header, IErrorCode errorCode) {
		dispatch(() -> {
			alertPresenter = new AlertPresenter(alertType);
			alertPresenter.getAlert().initOwner(getPrimaryStage());
			alertPresenter.title(title);
			alertPresenter.header(header);
			alertPresenter.content(ErrorCodeWrapper.getByErrorCode(errorCode).getMessageCode());
			alertPresenter.getAlert().showAndWait();
		});
	}

	/**
	 * Creates a pop-up in order to display a message to the user. Shows the dialog and waits for the user response (in other words,
	 * brings up a blocking dialog, with the returned value the users input).
	 * <p>
	 * This method must be called on the JavaFX Application thread. Additionally, it must either be called from an input event handler
	 * or from the run method of a Runnable passed to {@link javafx.application.Platform#runLater Platform.runLater}. It must not be
	 * called during animation or layout processing.
	 * </p>
	 * 
	 * @param alertType The type of alert.
	 * @param title     The code associated to the title of the alert box.
	 * @param header    The code associated to the header of the alert box.
	 * @param content   The message of the error to display.
	 */
	public static void showAndWait(AlertType alertType, ICode title, ICode header, ICode content) {
		dispatch(() -> {
			alertPresenter = new AlertPresenter(alertType);
			alertPresenter.getAlert().initOwner(getPrimaryStage());
			alertPresenter.title(title);
			alertPresenter.header(header);
			alertPresenter.content(content);
			alertPresenter.getAlert().showAndWait();
		});
	}

	public static class ErrorPresenterBuilder {
		private AlertType alertType;
		private ICode titleCode, headerCode;
		private IResponse response;
		private Object[] titleArgs, headerArgs, errorArgs;

		/**
		 * Creates an alert presenter associated to the given alert type.
		 * 
		 * @param alertType The alert type.
		 */
		private ErrorPresenterBuilder(AlertType alertType) {
			this.alertType = alertType;
		}

		/**
		 * Creates a new ErrorPresenter associated to the given alert type.
		 * 
		 * @param alertType The alert type.
		 * 
		 * @return a new error presenter.
		 */
		public static ErrorPresenterBuilder of(AlertType alertType) {
			return new ErrorPresenterBuilder(alertType);
		}

		/**
		 * Set the code associated to the title of the pop-up to display.
		 * 
		 * @param title The code associated to the title of the alert box.
		 * @param args  The arguments for the title message.
		 * 
		 * @return This builder.
		 */
		public ErrorPresenterBuilder title(ICode title, Object... args) {
			titleCode = title;
			titleArgs = args;
			return this;
		}

		/**
		 * Set the code associated to the header of the pop-up to display.
		 * 
		 * @param header The code associated to the header of the alert box.
		 * @param args   The arguments for the header message.
		 * 
		 * @return This builder.
		 */
		public ErrorPresenterBuilder header(ICode header, Object... args) {
			headerCode = header;
			headerArgs = args;
			return this;
		}

		/**
		 * Set the error code associated to the content of the pop-up to display.
		 * 
		 * @param response The response received from the server.
		 * @param args     The arguments for the error message.
		 * 
		 * @return This builder.
		 */
		public ErrorPresenterBuilder error(IResponse response, Object... args) {
			this.response = response;
			errorArgs = args;
			return this;
		}

		/**
		 * Shows the dialog but does not wait for a user response (in other words, this brings up a non-blocking dialog). Users of this
		 * API must either poll the {@link #resultProperty() result property}, or else add a listener to the result property to be
		 * informed of when it is set.
		 * 
		 * @throws IllegalStateException if this method is called on a thread other than the JavaFX Application Thread.
		 */
		public void show() {
			if (!response.hasFailed())
				return;

			dispatch(() -> {
				alertPresenter = new AlertPresenter(alertType);
				alertPresenter.getAlert().initOwner(getPrimaryStage());
				alertPresenter.title(titleCode, titleArgs);
				alertPresenter.header(headerCode, headerArgs);
				alertPresenter.content(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode(), errorArgs);
				alertPresenter.show();
			});
		}

		/**
		 * Creates a pop-up in order to display a message to the user. Shows the dialog and waits for the user response (in other words,
		 * brings up a blocking dialog, with the returned value the users input).
		 * <p>
		 * This method must be called on the JavaFX Application thread. Additionally, it must either be called from an input event handler
		 * or from the run method of a Runnable passed to {@link javafx.application.Platform#runLater Platform.runLater}. It must not be
		 * called during animation or layout processing.
		 * </p>
		 */
		public void showAndWait() {
			if (!response.hasFailed())
				return;

			dispatch(() -> {
				alertPresenter = new AlertPresenter(alertType);
				alertPresenter.getAlert().initOwner(getPrimaryStage());
				alertPresenter.title(titleCode, titleArgs);
				alertPresenter.header(headerCode, headerArgs);
				alertPresenter.content(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode(), errorArgs);
				alertPresenter.showAndWait();
			});
		}
	}
}
