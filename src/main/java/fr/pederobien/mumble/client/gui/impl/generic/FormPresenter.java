package fr.pederobien.mumble.client.gui.impl.generic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import javafx.beans.property.StringProperty;

public class FormPresenter extends PresenterBase {
	private Function<ButtonType, Boolean> onButtonClicked;
	private Map<ButtonType, StringProperty> buttonTexts;

	/**
	 * Creates a presenter for a form view.
	 * 
	 * @param onButtonClicked The action to perform when the user clicked on a button.
	 * @param buttons         The type of button to display.
	 */
	public FormPresenter(Function<ButtonType, Boolean> onButtonClicked, ButtonType... buttons) {
		this.onButtonClicked = onButtonClicked;
		buttonTexts = new LinkedHashMap<ButtonType, StringProperty>();

		for (ButtonType button : buttons) {
			if (buttonTexts.get(button) != null)
				continue;

			buttonTexts.put(button, getPropertyHelper().newLanguageProperty(button.getCode()));
		}
	}

	/**
	 * Creates a presenter for a form view.
	 * 
	 * @param buttons The type of button to display.
	 */
	public FormPresenter(ButtonType... buttons) {
		this(null, buttons);
	}

	/**
	 * Method to run when the user clicked on a button.
	 * 
	 * @param type The type of the clicked button.
	 */
	public boolean onButtonClicked(ButtonType type) {
		return onButtonClicked != null ? onButtonClicked.apply(type) : true;
	}

	/**
	 * @return A list that contains the text of buttons to create.
	 */
	public Map<ButtonType, StringProperty> getButtonTexts() {
		return buttonTexts;
	}

	/**
	 * Set the code to run when a button has been clicked.
	 * 
	 * @param onButtonClicked The new code to run.
	 */
	public void setOnButtonClicked(Function<ButtonType, Boolean> onButtonClicked) {
		this.onButtonClicked = onButtonClicked;
	}
}
