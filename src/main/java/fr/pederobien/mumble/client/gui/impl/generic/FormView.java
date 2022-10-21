package fr.pederobien.mumble.client.gui.impl.generic;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.mumble.client.gui.impl.view.ViewBase;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class FormView extends ViewBase<FormPresenter, BorderPane> {
	private int row;
	private GridPane rows;
	private FlowPane buttons;
	private Map<ButtonType, Button> buttonFromType;
	private Map<Button, ButtonType> typeFromButton;
	private Insets columnMargin, rowMargin;

	/**
	 * Layout that is represented by a BorderPane as root, a GridPane that represents all the rows and a horizontal box to display
	 * buttons. The goal of this layout is to display text on the left side (first column) and a view associated to this text in the
	 * second column.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public FormView(FormPresenter presenter) {
		super(presenter, new BorderPane());

		buttonFromType = new HashMap<ButtonType, Button>();
		typeFromButton = new HashMap<Button, ButtonType>();
		columnMargin = Insets.EMPTY;
		rowMargin = Insets.EMPTY;

		row = 0;

		rows = new GridPane();
		getRoot().setCenter(rows);

		buttons = new FlowPane();
		for (Map.Entry<ButtonType, StringProperty> entry : getPresenter().getButtonTexts().entrySet()) {
			Button button = new Button();
			button.fontProperty().bind(getPresenter().fontProperty());
			button.textProperty().bind(entry.getValue());
			button.setOnAction(e -> getPresenter().onButtonClicked(typeFromButton.get(e.getSource())));

			buttons.getChildren().add(button);

			buttonFromType.put(entry.getKey(), button);
			typeFromButton.put(button, entry.getKey());
		}

		buttons.setAlignment(Pos.CENTER_RIGHT);
		getRoot().setBottom(buttons);
	}

	/**
	 * Creates a new row that contains the given label in the first column and the given field in the second one.
	 * 
	 * @param label The label of the row.
	 * @param field The field of the row.
	 */
	public void addRow(Label label, Region field) {
		rows.add(label, 0, row);
		rows.add(field, 1, row);
		// field.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(field, Priority.ALWAYS);
		GridPane.setMargin(label, getMargins());
		GridPane.setMargin(field, rowMargin);
		row++;
	}

	/**
	 * Creates a new row that contains the given label in the first column and the given field in the second one.
	 * 
	 * @param label The label of the row.
	 * @param field The field of the row.
	 */
	public void addRow(String label, Region field) {
		Label labelView = new Label(label);
		labelView.fontProperty().bind(getPresenter().fontProperty());
		addRow(labelView, field);
	}

	/**
	 * Removes all the component in at the specific row index.
	 * 
	 * @param index The row index.
	 */
	public void removeRow(int index) {
		int before = rows.getChildren().size();
		rows.getChildren().removeIf(node -> GridPane.getRowIndex(node) == index);
		int after = rows.getChildren().size();

		if (after < before)
			row--;
	}

	/**
	 * @return The button associated to the given type if registered, null otherwise.
	 */
	public Button getButton(ButtonType type) {
		return buttonFromType.get(type);
	}

	/**
	 * Set the margin between the first and the second column.
	 * 
	 * @param top    The top offset.
	 * @param right  The right offset.
	 * @param bottom The bottom offset.
	 * @param left   The left offset.
	 */
	public void setColumnMargin(double top, double right, double bottom, double left) {
		columnMargin = new Insets(top, right, bottom, left);
		for (Node node : rows.getChildren())
			if (GridPane.getColumnIndex(node) == 0)
				GridPane.setMargin(node, getMargins());
	}

	/**
	 * Set the margin between the first and the second column.
	 * 
	 * @param top    The top offset.
	 * @param right  The right offset.
	 * @param bottom The bottom offset.
	 * @param left   The left offset.
	 */
	public void setRowMargin(double top, double right, double bottom, double left) {
		rowMargin = new Insets(top, right, bottom, left);
		for (Node node : rows.getChildren())
			GridPane.setMargin(node, getMargins());
	}

	/**
	 * Set the margin between the buttons and the rows.
	 * 
	 * @param top    The top offset.
	 * @param right  The right offset.
	 * @param bottom The bottom offset.
	 * @param left   The left offset.
	 */
	public void setButtonsMargin(double top, double right, double bottom, double left) {
		BorderPane.setMargin(buttons, new Insets(top, right, bottom, left));
	}

	/**
	 * Set the margin of the button associated to the given type.
	 * 
	 * @param type   The type of the button whose the margin must be updated.
	 * @param top    The top offset.
	 * @param right  The right offset.
	 * @param bottom The bottom offset.
	 * @param left   The left offset.
	 */
	public void setButtonMargin(ButtonType type, double top, double right, double bottom, double left) {
		Button button = getButton(type);
		if (button == null)
			return;

		FlowPane.setMargin(button, new Insets(top, right, bottom, left));
	}

	/**
	 * @return The global margin which correspond to the sum of the column margin and the row margin.
	 */
	private Insets getMargins() {
		double top = columnMargin.getTop() + rowMargin.getTop();
		double right = columnMargin.getRight() + rowMargin.getRight();
		double bottom = columnMargin.getBottom() + rowMargin.getBottom();
		double left = columnMargin.getLeft() + rowMargin.getLeft();
		return new Insets(top, right, bottom, left);
	}
}
