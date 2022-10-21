package fr.pederobien.mumble.client.gui.impl.generic;

import java.util.function.Function;

import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public abstract class OkCancelPresenter extends PresenterBase {
	private FormView formView;

	/**
	 * Creates a presenter associated to a form view with ok and cancel button.
	 */
	public OkCancelPresenter() {
		Function<ButtonType, Boolean> onButtonClicked = button -> {
			if (button == ButtonType.OK)
				return onOkButtonClicked();
			else if (button == ButtonType.CANCEL)
				return onCancelButtonClicked();
			return true;
		};
		formView = new FormView(new FormPresenter(onButtonClicked, ButtonType.CANCEL, ButtonType.OK));
	}

	/**
	 * @return The title of the stage.
	 */
	public abstract StringProperty titleTextProperty();

	/**
	 * Method called when the ok button has been clicked.
	 *
	 * @return True if the stage can be closed, false otherwise.
	 */
	public abstract boolean onOkButtonClicked();

	/**
	 * @return The content of the stage.
	 */
	public FormView getFormView() {
		return formView;
	}

	/**
	 * @return The boolean property used to enable/disable the ok button.
	 */
	public abstract BooleanProperty okDisableProperty();

	/**
	 * Method called when the cancel button has been clicked.
	 *
	 * @return True if the stage can be closed, false otherwise.
	 */
	public boolean onCancelButtonClicked() {
		return true;
	}

	/**
	 * Action to execute when the stage is about to be closed.
	 */
	public void onClosing() {

	}
}
