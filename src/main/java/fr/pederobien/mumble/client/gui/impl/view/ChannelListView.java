package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ChannelListPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public class ChannelListView extends ViewBase<ChannelListPresenter, StackPane> {

	public ChannelListView(ChannelListPresenter presenter) {
		super(presenter, new StackPane());

		ListView<Object> channelListView = new ListView<Object>();
		channelListView.setItems(getPresenter().getChannels());
		channelListView.setBackground(Background.EMPTY);

		// Visible if the list view is not empty.
		channelListView.visibleProperty().bind(Bindings.greaterThan(Bindings.size(channelListView.getItems()), new SimpleIntegerProperty(0)));
		channelListView.managedProperty().bind(channelListView.visibleProperty());

		channelListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			dispatch(() -> channelListView.getSelectionModel().clearSelection());
		});

		channelListView.setCellFactory(getPresenter().channelViewFactory(null));

		getRoot().getChildren().add(channelListView);
	}
}
