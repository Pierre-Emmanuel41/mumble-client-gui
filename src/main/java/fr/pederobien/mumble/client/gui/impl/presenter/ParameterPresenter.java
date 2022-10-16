package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.ParameterMaxValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterMinValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter;
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
	private ObjectProperty<Border> valueBorderProperty, minValueBorderProperty, maxValueBorderProperty;
	private SimpleTooltipProperty tooltipProperty;
	private boolean valueValid, minValueValid, maxValueValid;

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

		if (rangeParameter != null) {
			minValue = rangeParameter.getMin();
			minValueProperty = new SimpleStringProperty(minValue.toString());
			minValueBorderProperty = new SimpleObjectProperty<Border>(null);

			maxValue = rangeParameter.getMax();
			maxValueProperty = new SimpleStringProperty(maxValue.toString());
			maxValueBorderProperty = new SimpleObjectProperty<Border>(null);

			tooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.RANGE_PARAMETER_TOOLTIP, rangeParameter.getType(), rangeParameter.getMin(),
					rangeParameter.getMax());
		} else
			tooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.PARAMETER_TOOLTIP, parameter.getType());

		valueValid = minValueValid = maxValueValid = true;

		EventManager.registerListener(this);
	}

	public boolean onOkButtonClicked() {
		if (rangeParameter != null) {
			try {
				rangeParameter.setMin(minValue, response -> handleResponse(response));
				rangeParameter.setMax(maxValue, response -> handleResponse(response));
			} catch (IllegalArgumentException e) {
				// When the new minimum is greater than the old maximum
				try {
					rangeParameter.setMax(maxValue, response -> handleResponse(response));
					rangeParameter.setMin(minValue, response -> handleResponse(response));
				} catch (IllegalStateException e1) {
					// When the new maximum is less than the old minimum
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
		return valueValid && minValueValid && maxValueValid;
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
	public ObjectProperty<Tooltip> tooltipProperty() {
		return tooltipProperty;
	}

	/**
	 * Validate or invalidate the new value of the parameter.
	 * 
	 * @param value The new value of the parameter.
	 */
	public void validateParameterValue(Object value) {
		if (value == null) {
			valueValid = false;
			valueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			return;
		}

		try {
			this.value = parameter.getType().getValue(value.toString());
		} catch (Exception e) {
			// When the given value cannot be parsed in parameter's type
			valueValid = false;
			valueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
		}

		valueValid = true;
		EventManager.callEvent(new ParameterValueChangeRequestEvent(parameter, this.value));
	}

	/**
	 * Validate or invalidate the new minimum value of the parameter.
	 * 
	 * @param value The new minimum value of the parameter.
	 */
	public void validateParameterMinValue(Object minValue) {
		if (minValue == null) {
			minValueValid = false;
			minValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			return;
		}

		try {
			this.value = parameter.getType().getValue(value.toString());
		} catch (Exception e) {
			// When the given value cannot be parsed in parameter's type
			minValueValid = false;
			minValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
		}

		minValueValid = true;
		EventManager.callEvent(new ParameterMinValueChangeRequestEvent(rangeParameter, this.value));
	}

	/**
	 * Validate or invalidate the new maximum value of the parameter.
	 * 
	 * @param value The new maximum value of the parameter.
	 */
	public void validateParameterMaxValue(Object maxValue) {
		if (maxValue == null) {
			maxValueValid = false;
			maxValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			return;
		}

		try {
			this.value = parameter.getType().getValue(value.toString());
		} catch (Exception e) {
			// When the given value cannot be parsed in parameter's type
			maxValueValid = false;
			maxValueBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
		}

		maxValueValid = true;
		EventManager.callEvent(new ParameterMaxValueChangeRequestEvent(rangeParameter, this.value));
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
		ErrorPresenter.showAndWait(AlertType.ERROR, EMessageCode.ADD_CHANNEL_TITLE, EMessageCode.ADD_CHANNEL_NAME_RESPONSE, response);
	}
}
