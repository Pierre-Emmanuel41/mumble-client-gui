package fr.pederobien.mumble.client.gui.impl.view;

import java.util.function.Function;

import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ListCellView<T> extends ListCell<T> {
	private Function<T, Parent> constructor;

	public ListCellView(Function<T, Parent> constructor) {
		this.constructor = constructor;
		setBackground(Background.EMPTY);

		setOnMouseEntered(e -> {
			if (isEmpty())
				return;
			setBackground(new Background(new BackgroundFill(Color.web("0x0096c9ff"), new CornerRadii(2), null)));
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
