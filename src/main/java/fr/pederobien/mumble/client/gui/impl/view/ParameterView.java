package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.MumbleClientApplication;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.presenter.ParameterPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ParameterView extends ViewBase<ParameterPresenter, HBox> {
	private Label parameterName;
	private Region valueView;
	private SimpleLanguageProperty booleanTextProperty;
	private BooleanProperty booleanParameterValue;
	private double marginBetweenLabelAndTextField = 20.0;

	public ParameterView(ParameterPresenter presenter) {
		super(presenter, new HBox());

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);

		parameterName = new Label();
		parameterName.fontProperty().bind(getPresenter().fontProperty());
		parameterName.textProperty().bind(getPresenter().parameterNameProperty());
		getRoot().getChildren().add(parameterName);

		valueView = getPresenter().isBooleanParameter() ? createBooleanView() : getPresenter().isRangeParameter() ? createRangeView() : createDefaultView();
		getRoot().getChildren().add(valueView);
		HBox.setMargin(valueView, new Insets(0, 0, 0, marginBetweenLabelAndTextField));
	}

	/**
	 * @return The label that contains the parameter name.
	 */
	public Label getParameterName() {
		return parameterName;
	}

	/**
	 * @return The graphic control that contains the parameter value input.
	 */
	public Region getValueView() {
		return valueView;
	}

	private TextField createDefaultView() {
		TextField textfield = new TextField();
		textfield.fontProperty().bind(getPresenter().fontProperty());
		textfield.textProperty().bindBidirectional(getPresenter().valueProperty());
		textfield.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateParameterValue(newValue));
		textfield.borderProperty().bind(getPresenter().valueBorderProperty());
		textfield.tooltipProperty().bind(getPresenter().valueTooltipProperty());
		return textfield;
	}

	private ToggleButton createBooleanView() {
		boolean value = Boolean.parseBoolean(getPresenter().valueProperty().getValue());
		booleanTextProperty = MumbleClientApplication.getPropertyHelper().languageProperty(value ? EMessageCode.ENABLE : EMessageCode.DISABLE);
		booleanParameterValue = new SimpleBooleanProperty(value);
		booleanParameterValue.addListener((obs, oldValue, newValue) -> onToggleButtonClicked(newValue));

		ToggleButton button = new ToggleButton();
		button.fontProperty().bind(getPresenter().fontProperty());
		button.textProperty().bind(booleanTextProperty);
		button.selectedProperty().bindBidirectional(booleanParameterValue);
		return button;
	}

	private HBox createRangeView() {
		HBox horizontalBox = new HBox(5);

		horizontalBox.getChildren().add(createDefaultView());

		TextField minTextField = new TextField();
		minTextField.fontProperty().bind(getPresenter().fontProperty());
		minTextField.textProperty().bindBidirectional(getPresenter().minValueProperty());
		minTextField.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateParameterMinValue(newValue));
		minTextField.borderProperty().bind(getPresenter().minValueBorderProperty());
		minTextField.tooltipProperty().bind(getPresenter().minValueTooltipProperty());
		horizontalBox.getChildren().add(minTextField);

		TextField maxTextField = new TextField();
		maxTextField.fontProperty().bind(getPresenter().fontProperty());
		maxTextField.textProperty().bindBidirectional(getPresenter().maxValueProperty());
		maxTextField.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateParameterMaxValue(newValue));
		maxTextField.borderProperty().bind(getPresenter().maxValueBorderProperty());
		maxTextField.tooltipProperty().bind(getPresenter().maxValueTooltipProperty());
		horizontalBox.getChildren().add(maxTextField);
		return horizontalBox;
	}

	private void onToggleButtonClicked(Boolean newValue) {
		booleanTextProperty.setCode(newValue ? EMessageCode.ENABLE : EMessageCode.DISABLE);
		getPresenter().valueProperty().set(newValue.toString());
	}
}
