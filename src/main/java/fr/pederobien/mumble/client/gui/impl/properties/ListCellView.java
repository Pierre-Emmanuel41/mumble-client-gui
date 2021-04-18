package fr.pederobien.mumble.client.gui.impl.properties;

import java.util.function.Function;

import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ListCellView<T> extends ListCell<T> {
	private Function<T, Parent> constructor;

	/**
	 * Create a cell view. If the entered color is null then the background is not updated when the mouse enter or exit the component.
	 * 
	 * @param constructor  The constructor that create the graphic of the cell.
	 * @param enteredColor Color when mouse entered in the cell.
	 */
	public ListCellView(Function<T, Parent> constructor, Color enteredColor) {
		this.constructor = constructor;
		setBackground(Background.EMPTY);

		if (enteredColor == null)
			return;

		// No need to recreate each time the Mouse enters in the cell.
		Background background = new Background(new BackgroundFill(enteredColor, new CornerRadii(2), null));
		setOnMouseEntered(e -> {
			if (isEmpty())
				return;
			setBackground(background);
		});

		setOnMouseExited(e -> {
			if (isEmpty() || isSelected())
				return;
			setBackground(Background.EMPTY);
		});

		selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue)
				return;
			setBackground(Background.EMPTY);
		});
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else
			setGraphic(constructor.apply(item));
	}
}
