package fr.pederobien.mumble.client.gui.impl.view;

import java.util.function.Function;

import javafx.scene.Parent;
import javafx.scene.control.ListCell;

public class ListCellView<T> extends ListCell<T> {
	private Function<T, Parent> constructor;

	public ListCellView(Function<T, Parent> constructor) {
		this.constructor = constructor;
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setGraphic(constructor.apply(item));
		}
	}

}
