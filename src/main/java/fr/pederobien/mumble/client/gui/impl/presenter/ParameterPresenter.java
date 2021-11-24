package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.event.ParameterValueChangePostEvent;
import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.impl.RangeParameter;
import fr.pederobien.mumble.client.interfaces.IParameter;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ParameterPresenter extends OkCancelPresenter implements IEventListener {
	private IParameter<?> parameter;
	private Object newValue;
	private StringProperty parameterNameProperty, valueProperty;
	private BooleanProperty okDisableProperty;
	private boolean isNotValid, internalUpdate;

	public ParameterPresenter(IParameter<?> parameter) {
		this.parameter = parameter;

		parameterNameProperty = new SimpleStringProperty(String.format("%s :", parameter.getName()));
		valueProperty = new SimpleStringProperty(parameter.getValue().toString());
		valueProperty.addListener((obs, oldValue, newValue) -> validate(newValue));
		okDisableProperty = new SimpleBooleanProperty(true);
		newValue = parameter.getValue();

		EventManager.registerListener(this);
	}

	@Override
	public StringProperty titleTextProperty() {
		return null;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		parameter.setValue(newValue, response -> handleResponse(response));
		return true;
	}

	@Override
	public void onClosing() {
		EventManager.unregisterListener(this);
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return The property that display "&lt;ParameterName&gt; :"
	 */
	public StringProperty parameterNameProperty() {
		return parameterNameProperty;
	}

	/**
	 * @return The code associated to the type of this parameter.
	 */
	public int getParameterTypeCode() {
		return parameter.getType().getCode();
	}

	/**
	 * @return The property that contains the new parameter value.
	 */
	public StringProperty valueProperty() {
		return valueProperty;
	}

	/**
	 * @return true if the parameter new value is not valid, false otherwise. A not valid value does not means the new value is
	 *         different from the previous parameter value. It only indicates the new value does not respect the conditions associated
	 *         to this parameter (right type, right range).
	 */
	public boolean isNotValid() {
		return isNotValid;
	}

	/**
	 * @return True if the intermediate parameter value is identical to the current parameter value, false otherwise.
	 */
	public boolean isIdentical() {
		return newValue != null && newValue.equals(parameter.getValue());
	}

	@EventHandler
	private void onParameterValueChange(ParameterValueChangePostEvent event) {
		if (!event.getParameter().equals(parameter))
			return;

		internalUpdate = true;
		valueProperty.set(event.getParameter().getValue().toString());
	}

	/**
	 * Try to validate the new parameter value. A value is validated if and only if the conversion from string to the parameter type
	 * has been successful and if the new value respects the range associated to the parameter (if a range is defined).
	 */
	private void validate(String value) {
		if (internalUpdate) {
			internalUpdate = false;
			return;
		}

		try {
			// Particular case
			if (getParameterTypeCode() == ParameterType.CHAR_CODE && value.length() != 1)
				throw new Exception();

			newValue = parameter.getType().getValue(value);
			if (parameter instanceof RangeParameter)
				((RangeParameter<?>) parameter).checkRange(parameter.getType().getValue(value));
			isNotValid = false;
		} catch (Exception e) {
			newValue = null;
			isNotValid = true;
		}

		okDisableProperty.set(isNotValid || isIdentical());
		EventManager.callEvent(new ParameterValueChangeRequestEvent(parameter, newValue));
	}

	private void handleResponse(IResponse response) {
		// Do nothing
	}
}
