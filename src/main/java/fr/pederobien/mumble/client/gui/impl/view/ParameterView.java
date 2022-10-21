package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ParameterPresenter;
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
		textfield.borderProperty().bind(getPresenter().valueBorderProperty());
		textfield.tooltipProperty().bind(getPresenter().valueTooltipProperty());
		return textfield;
	}

	private ToggleButton createBooleanView() {
		ToggleButton button = new ToggleButton();
		button.fontProperty().bind(getPresenter().fontProperty());
		button.textProperty().bind(getPresenter().booleanTextProperty());
		button.selectedProperty().bindBidirectional(getPresenter().booleanValueProperty());
		return button;
	}

	private HBox createRangeView() {
		HBox horizontalBox = new HBox(5);
		horizontalBox.setAlignment(Pos.CENTER);

		TextField valueTextField = createDefaultView();
		horizontalBox.getChildren().add(valueTextField);

		Label minLabel = new Label();
		minLabel.fontProperty().bind(getPresenter().fontProperty());
		minLabel.textProperty().bind(getPresenter().minTextProperty());
		horizontalBox.getChildren().add(minLabel);

		TextField minTextField = new TextField();
		minTextField.fontProperty().bind(getPresenter().fontProperty());
		minTextField.textProperty().bindBidirectional(getPresenter().minValueProperty());
		minTextField.borderProperty().bind(getPresenter().minValueBorderProperty());
		minTextField.tooltipProperty().bind(getPresenter().minValueTooltipProperty());
		minTextField.setPrefWidth(60);
		horizontalBox.getChildren().add(minTextField);

		Label maxLabel = new Label();
		maxLabel.fontProperty().bind(getPresenter().fontProperty());
		maxLabel.textProperty().bind(getPresenter().maxTextProperty());
		horizontalBox.getChildren().add(maxLabel);

		TextField maxTextField = new TextField();
		maxTextField.fontProperty().bind(getPresenter().fontProperty());
		maxTextField.textProperty().bindBidirectional(getPresenter().maxValueProperty());
		maxTextField.borderProperty().bind(getPresenter().maxValueBorderProperty());
		maxTextField.tooltipProperty().bind(getPresenter().maxValueTooltipProperty());
		maxTextField.setPrefWidth(60);
		horizontalBox.getChildren().add(maxTextField);
		return horizontalBox;
	}
}
