package fr.pederobien.mumble.client.gui.impl.view;

import java.util.function.Function;

import javafx.scene.Parent;
import javafx.scene.control.ListCell;

public class ListCellView<T> extends ListCell<T> {
	private Function<T, Parent> constructor;

	public ListCellView(Function<T, Parent> constructor) {
		this.constructor = constructor;
		/*
		 * setOnMouseEntered(e -> updateBackground(new Background(new BackgroundFill(Color.web("0x002a91"), null, null))));
		 * setOnMouseExited(e -> updateBackground(Background.EMPTY));
		 */
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			// setBackground(Background.EMPTY);
		} else {
			setGraphic(constructor.apply(item));
		}
	}

	/*
	 * private void updateBackground(Background background) { if (isEmpty()) return; setBackground(background); }
	 */
}
