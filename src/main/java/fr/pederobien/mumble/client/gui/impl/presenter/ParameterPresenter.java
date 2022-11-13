package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.MumbleClientApplication;
import fr.pederobien.mumble.client.gui.event.ParameterMaxValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterMinValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
import fr.pederobien.mumble.client.player.event.MumbleParameterMaxValueChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleParameterMinValueChangePostEvent;
import fr.pederobien.mumble.client.player.event.MumbleParameterValueChangePostEvent;
import fr.pederobien.mumble.client.player.interfaces.IParameter;
import fr.pederobien.mumble.client.player.interfaces.IRangeParameter;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ParameterPresenter extends PresenterBase implements IEventListener {
	private IParameter<?> parameter;
	private IRangeParameter<?> rangeParameter;
	private Object value, minValue, maxValue;
	private StringProperty parameterNameProperty, valueProperty, minValueProperty, maxValueProperty;
	private SimpleLanguageProperty booleanTextProperty, minTextProperty, maxTextProperty;
	private ObjectProperty<Border> valueBorderProperty, minValueBorderProperty, maxValueBorderProperty;
	private SimpleBooleanProperty booleanValueProperty;
	private SimpleTooltipProperty valueTooltipProperty, minValueTooltipProperty, maxValueTooltipProperty;
	private boolean isValueValid, isMinValueValid, isMaxValueValid;

	/**
	 * Creates a presenter in order to update the value of a parameter. If the parameter is a associated to a range, then it is
	 * possible to update the minimum value and the maximum value.
	 * 
	 * @param parameter The parameter associated to this presenter.
	 */
	public ParameterPresenter(IParameter<?> parameter) {
		this.parameter = parameter;
		rangeParameter = parameter instanceof IRangeParameter<?> ? (IRangeParameter<?>) parameter : null;

		parameterNameProperty = new SimpleStringProperty(String.format("%s :", parameter.getName()));
		valueBorderProperty = new SimpleObjectProperty<Border>(null);

		value = parameter.getValue();
		valueProperty = new SimpleStringProperty(value.toString());
		valueProperty.addListener((obs, oldValue, newValue) -> validateParameterValue(newValue));

		if (parameter.getType() == ParameterType.BOOLEAN) {
			booleanTextProperty = MumbleClientApplication.getPropertyHelper().languageProperty((Boolean) value ? EGuiCode.ENABLE : EGuiCode.DISABLE);
			booleanValueProperty = new SimpleBooleanProperty((Boolean) value);
			booleanValueProperty.addListener((obs, oldValue, newValue) -> validateParameterValue(newValue));
		}

		if (rangeParameter != null) {
			minValue = rangeParameter.getMin();
			maxValue = rangeParameter.getMax();

			minValueProperty = new SimpleStringProperty(minValue.toString());
			minValueProperty.addListener((obs, oldValue, newValue) -> validateParameterMinValue(newValue));
			minValueBorderProperty = new SimpleObjectProperty<Border>(null);
			minTextProperty = getPropertyHelper().languageProperty(EGuiCode.MUMBLE_CLIENT_GUI__MIN_VALUE);
			minValueTooltipProperty = getPropertyHelper().tooltipProperty(EGuiCode.MUMBLE_CLIENT_GUI__MIN_VALUE_TOOLTIP, rangeParameter.getType(), maxValue);

			maxValueProperty = new SimpleStringProperty(maxValue.toString());
			maxValueProperty.addListener((obs, oldValue, newValue) -> validateParameterMaxValue(newValue));
			maxValueBorderProperty = new SimpleObjectProperty<Border>(null);
			maxTextProperty = getPropertyHelper().languageProperty(EGuiCode.MUMBLE_CLIENT_GUI__MAX_VALUE);
			maxValueTooltipProperty = getPropertyHelper().tooltipProperty(EGuiCode.MUMBLE_CLIENT_GUI__MAX_VALUE_TOOLTIP, rangeParameter.getType(), minValue);

			valueTooltipProperty = getPropertyHelper().tooltipProperty(EGuiCode.RANGE_PARAMETER_TOOLTIP, rangeParameter.getType(), minValue, maxValue);
		} else
			valueTooltipProperty = getPropertyHelper().tooltipProperty(EGuiCode.PARAMETER_TOOLTIP, parameter.getType());

		isValueValid = isMinValueValid = isMaxValueValid = true;

		EventManager.registerListener(this);
	}

	/**
	 * Update the current value of the underlying parameter. If it is associated to a range, then update also the minimum/maximum
	 * value of the parameter.
	 * 
	 * @return True if values have been applied successfully, false otherwise.
	 */
	public boolean apply() {
		if (rangeParameter != null) {
			try {
				rangeParameter.setMin(minValue, response -> handleResponse(response));
				rangeParameter.setMax(maxValue, response -> handleResponse(response));
			} catch (IllegalArgumentException e) {
				try {
					rangeParameter.setMax(maxValue, response -> handleResponse(response));
					rangeParameter.setMin(minValue, response -> handleResponse(response));
				} catch (IllegalArgumentException e1) {
					return false;
				}
			}
		}

		parameter.setValue(value, response -> handleResponse(response));
		return true;
	}

	/**
	 * Unregister this presenter as event listener for the event {@link MumbleParameterValueChangePostEvent}.
	 */
	public void onClosing() {
		EventManager.unregisterListener(this);
	}

	/**
	 * @return The property that display "&lt;ParameterName&gt; :"
	 */
	public StringProperty parameterNameProperty() {
		return parameterNameProperty;
	}

	/**
	 * @return True if the type of the underlying parameter is boolean.
	 */
	public boolean isBooleanParameter() {
		return parameter.getType() == ParameterType.BOOLEAN;
	}

	/**
	 * @return True if the underlying parameter is associated to a range.
	 */
	public boolean isRangeParameter() {
		return rangeParameter != null;
	}

	/**
	 * @return The property that contains the new parameter value.
	 */
	public StringProperty valueProperty() {
		return valueProperty;
	}

	/**
	 * @return The property that display enable/disable
	 */
	public StringProperty booleanTextProperty() {
		return booleanTextProperty;
	}

	/**
	 * @return The property that display "Min:"
	 */
	public StringProperty minTextProperty() {
		return minTextProperty;
	}

	/**
	 * @return The property that display "Max:"
	 */
	public StringProperty maxTextProperty() {
		return maxTextProperty;
	}

	/**
	 * @return The property that contains the new parameter value with boolean data type.
	 */
	public SimpleBooleanProperty booleanValueProperty() {
		return booleanValueProperty;
	}

	/**
	 * @return The property that contains the new parameter minimum value.
	 */
	public StringProperty minValueProperty() {
		return minValueProperty;
	}

	/**
	 * @return The property that contains the new parameter maximum value.
	 */
	public StringProperty maxValueProperty() {
		return maxValueProperty;
	}

	/**
	 * @return True if the new parameter value is valid. If the underlying parameter is associated to a range, then the minimum value
	 *         and the maximum value are checked.
	 */
	public boolean isValid() {
		return isValueValid && isMinValueValid && isMaxValueValid;
	}

	/**
	 * @return True if the new parameter value (resp. the new minimum parameter value or the new maximum parameter value) is different
	 *         from the current parameter value (resp. parameter minimum value and parameter maximum value).
	 */
	public boolean isIdentical() {
		boolean isIdentical = value != null && value.equals(parameter.getValue());
		if (rangeParameter != null) {
			isIdentical &= minValue != null && minValue.equals(rangeParameter.getMin());
			isIdentical &= maxValue != null && maxValue.equals(rangeParameter.getMax());
		}
		return isIdentical;
	}

	/**
	 * @return The property to change the border for the parameter value input.
	 */
	public ObjectProperty<Border> valueBorderProperty() {
		return valueBorderProperty;
	}

	/**
	 * @return The property to change the border for the parameter minimum value input.
	 */
	public ObjectProperty<Border> minValueBorderProperty() {
		return minValueBorderProperty;
	}

	/**
	 * @return The property to change the border for the parameter maximum value input.
	 */
	public ObjectProperty<Border> maxValueBorderProperty() {
		return maxValueBorderProperty;
	}

	/**
	 * @return The property that contains the tooltip of the component in which the parameter value is written. This tooltip contains
	 *         a description of constraints for the parameter value.
	 */
	public ObjectProperty<Tooltip> valueTooltipProperty() {
		return valueTooltipProperty;
	}

	/**
	 * @return The property that contains the tooltip of the component in which the minimum value of the parameter is written. This
	 *         tooltip contains a description of constraints for the minimum value of the parameter.
	 */
	public ObjectProperty<Tooltip> minValueTooltipProperty() {
		return minValueTooltipProperty;
	}

	/**
	 * @return The property that contains the tooltip of the component in which the maximum value of the parameter is written. This
	 *         tooltip contains a description of constraints for the maximum value of the parameter.
	 */
	public ObjectProperty<Tooltip> maxValueTooltipProperty() {
		return maxValueTooltipProperty;
	}

	@EventHandler
	private void onParameterValueChange(MumbleParameterValueChangePostEvent event) {
		if (!event.getParameter().equals(parameter))
			return;

		valueProperty.setValue(event.getParameter().getValue().toString());
	}

	@EventHandler
	private void onParameterMinValueChange(MumbleParameterMinValueChangePostEvent event) {
		if (!event.getParameter().equals(parameter))
			return;

		minValueProperty.setValue(event.getParameter().getMin().toString());
	}

	@EventHandler
	private void onParameterMaxValueChange(MumbleParameterMaxValueChangePostEvent event) {
		if (!event.getParameter().equals(parameter))
			return;

		maxValueProperty.setValue(event.getParameter().getMax().toString());
	}

	private void handleResponse(IResponse response) {
		ErrorPresenter.showAndWait(AlertType.ERROR, EGuiCode.ADD_CHANNEL_TITLE, EGuiCode.ADD_CHANNEL_NAME_RESPONSE, response);
	}

	/**
	 * Validate or invalidate the new value of the parameter.
	 * 
	 * @param value The new value of the parameter.
	 */
	private void validateParameterValue(Object value) {
		this.value = value;
		validateParameterValues();
		EventManager.callEvent(new ParameterValueChangeRequestEvent(parameter, this.value));
	}

	/**
	 * Validate or invalidate the new minimum value of the parameter.
	 * 
	 * @param value The new minimum value of the parameter.
	 */
	private void validateParameterMinValue(Object minValue) {
		this.minValue = minValue;
		validateParameterValues();
		EventManager.callEvent(new ParameterMinValueChangeRequestEvent(rangeParameter, this.minValue));
	}

	/**
	 * Validate or invalidate the new maximum value of the parameter.
	 * 
	 * @param value The new maximum value of the parameter.
	 */
	private void validateParameterMaxValue(Object maxValue) {
		this.maxValue = maxValue;
		validateParameterValues();
		EventManager.callEvent(new ParameterMaxValueChangeRequestEvent(rangeParameter, this.maxValue));
	}

	private void validateParameterValues() {
		isValueValid = true;
		if (value == null || value.equals("")) {
			isValueValid = false;
			valueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
		}

		if (isValueValid)
			try {
				value = parameter.getType().getValue(value.toString());

				if (parameter.getType() == ParameterType.BOOLEAN)
					booleanTextProperty.setCode((Boolean) value ? EGuiCode.ENABLE : EGuiCode.DISABLE);

				if (rangeParameter == null)
					valueBorderProperty.set(null);
			} catch (Exception e) {
				// Parameter value cannot be parsed
				isValueValid = false;
				valueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			}

		if (rangeParameter != null) {
			isMinValueValid = true;

			if (minValue == null || minValue.equals("")) {
				isMinValueValid = false;
				minValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			}

			if (isMinValueValid)
				try {
					minValue = parameter.getType().getValue(minValue.toString());
				} catch (Exception e) {
					// When parameter minimum value cannot be parsed
					isMinValueValid = false;
					minValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
				}

			isMaxValueValid = true;
			if (maxValue == null || maxValue.equals("")) {
				isMaxValueValid = false;
				maxValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			}

			if (isMaxValueValid)
				try {
					maxValue = parameter.getType().getValue(maxValue.toString());
				} catch (Exception e) {
					// When parameter minimum value cannot be parsed
					isMaxValueValid = false;
					maxValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
				}

			if (isMinValueValid && isMaxValueValid) {
				try {
					rangeParameter.check(minValue, maxValue, "The minimum value must be less than the maximimum value");
					minValueBorderProperty.set(null);
					minValueTooltipProperty.setMessageCode(EGuiCode.MUMBLE_CLIENT_GUI__MIN_VALUE_TOOLTIP, rangeParameter.getType(), minValue);
				} catch (Exception e) {
					// When the minimum value is strictly greater than the maximum value
					isMinValueValid = false;
					minValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
				}

				try {
					rangeParameter.check(minValue, maxValue, "The maximum value must be greater than the minimum value");
					maxValueBorderProperty.set(null);
					maxValueTooltipProperty.setMessageCode(EGuiCode.MUMBLE_CLIENT_GUI__MAX_VALUE_TOOLTIP, rangeParameter.getType(), minValue);
				} catch (Exception e) {
					// When the maximum value is strictly less than the minimum value
					isMaxValueValid = false;
					maxValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
				}
			}

			if (isValid())
				try {
					rangeParameter.checkRange(minValue, value, maxValue);
					isValueValid = true;
					valueBorderProperty.set(null);
					valueTooltipProperty.setMessageCode(EGuiCode.RANGE_PARAMETER_TOOLTIP, rangeParameter.getType(), minValue, maxValue);
				} catch (IllegalArgumentException e) {
					isValueValid = false;
					valueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
				}
		}
	}
}
