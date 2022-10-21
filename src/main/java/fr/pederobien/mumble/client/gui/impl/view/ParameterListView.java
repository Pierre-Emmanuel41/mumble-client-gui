package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ParameterListPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class ParameterListView extends ViewBase<ParameterListPresenter, VBox> {

	/**
	 * Creates a view in order to display all parameters of a sound modifier.
	 * 
	 * @param presenter the presenter associated to this view.
	 */
	public ParameterListView(ParameterListPresenter presenter) {
		super(presenter, new VBox());

		// Visible if the view list is not empty.
		getRoot().visibleProperty().bind(Bindings.greaterThan(Bindings.size(getPresenter().getParameterViews()), new SimpleIntegerProperty(0)));
		getRoot().managedProperty().bind(getRoot().visibleProperty());

		// Registering each parameter view.
		for (ParameterView view : getPresenter().getParameterViews()) {
			getRoot().getChildren().add(view.getRoot());
			VBox.setMargin(view.getRoot(), new Insets(0, 0, 5, 0));
		}
	}
}
