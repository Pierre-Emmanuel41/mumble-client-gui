package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.javafx.configuration.impl.components.SimpleLabel;
import fr.pederobien.javafx.configuration.impl.components.SimpleTextField;
import fr.pederobien.javafx.configuration.impl.components.SimpleToggleButton;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.presenter.ParameterPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ParameterView extends ViewBase<ParameterPresenter, HBox> {
	private SimpleLabel parameterName;
	private Region valueView;
	private double marginBetweenLabelAndTextField = 20.0;

	/**
	 * Creates a view in order to display the name of a parameter, its value and its minimum/maximum value if the parameter is
	 * associated to a range.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public ParameterView(ParameterPresenter presenter) {
		super(presenter, new HBox());

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);

		parameterName = new SimpleLabel(getPresenter().parameterNameProperty());
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
		SimpleTextField textfield = new SimpleTextField(getPresenter().valueProperty());
		textfield.borderProperty().bind(getPresenter().valueBorderProperty());
		textfield.tooltipProperty().bind(getPresenter().valueTooltipProperty());
		return textfield;
	}

	private ToggleButton createBooleanView() {
		SimpleToggleButton button = new SimpleToggleButton(getPresenter().booleanTextProperty());
		button.selectedProperty().bindBidirectional(getPresenter().booleanValueProperty());
		return button;
	}

	private HBox createRangeView() {
		HBox horizontalBox = new HBox(5);
		horizontalBox.setAlignment(Pos.CENTER);

		TextField valueTextField = createDefaultView();
		horizontalBox.getChildren().add(valueTextField);

		SimpleLabel minLabel = new SimpleLabel(EGuiCode.MUMBLE_CLIENT_GUI__MIN_VALUE);
		horizontalBox.getChildren().add(minLabel);

		SimpleTextField minTextField = new SimpleTextField(getPresenter().minValueProperty());
		minTextField.borderProperty().bind(getPresenter().minValueBorderProperty());
		minTextField.tooltipProperty().bind(getPresenter().minValueTooltipProperty());
		minTextField.setPrefWidth(60);
		horizontalBox.getChildren().add(minTextField);

		SimpleLabel maxLabel = new SimpleLabel(EGuiCode.MUMBLE_CLIENT_GUI__MAX_VALUE);
		horizontalBox.getChildren().add(maxLabel);

		SimpleTextField maxTextField = new SimpleTextField(getPresenter().maxValueProperty());
		maxTextField.borderProperty().bind(getPresenter().maxValueBorderProperty());
		maxTextField.tooltipProperty().bind(getPresenter().maxValueTooltipProperty());
		maxTextField.setPrefWidth(60);
		horizontalBox.getChildren().add(maxTextField);
		return horizontalBox;
	}
}
