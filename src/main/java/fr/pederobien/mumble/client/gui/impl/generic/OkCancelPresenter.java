package fr.pederobien.mumble.client.gui.impl.generic;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public abstract class OkCancelPresenter extends PresenterBase {
	private SimpleLanguageProperty okTextProperty;
	private SimpleLanguageProperty cancelTextProperty;

	public OkCancelPresenter() {
		okTextProperty = getPropertyHelper().languageProperty(EMessageCode.OK);
		cancelTextProperty = getPropertyHelper().languageProperty(EMessageCode.CANCEL);
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
	 * @return The text displayed on the ok button.
	 */
	public StringProperty okTextProperty() {
		return okTextProperty;
	}

	/**
	 * @return The text displayed on the cancel button.
	 */
	public StringProperty cancelTextProperty() {
		return cancelTextProperty;
	}

}
