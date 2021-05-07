package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ChannelPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ChannelView extends ViewBase<ChannelPresenter, GridPane> {

	public ChannelView(ChannelPresenter presenter) {
		super(presenter, new GridPane());

		Label channelName = new Label();
		channelName.fontProperty().bind(getPresenter().fontProperty());
		channelName.textProperty().bind(getPresenter().channelNameProperty());
		channelName.setTextFill(Color.BLACK);

		channelName.setOnMouseEntered(e -> {
			channelName.setBackground(new Background(new BackgroundFill(Color.web("0x0096c9ff"), new CornerRadii(4), null)));
		});

		channelName.setOnMouseExited(e -> {
			channelName.setBackground(Background.EMPTY);
		});

		channelName.setOnMouseClicked(e -> getPresenter().onChannelClicked());

		getRoot().add(channelName, 0, 0);

		ListView<Object> playerListView = new ListView<Object>();
		playerListView.setItems(getPresenter().getPlayers());

		// Visible if the list view is not empty.
		playerListView.visibleProperty().bind(Bindings.greaterThan(Bindings.size(playerListView.getItems()), new SimpleIntegerProperty(0)));
		playerListView.managedProperty().bind(playerListView.visibleProperty());

		playerListView.setBackground(Background.EMPTY);
		playerListView.setCellFactory(getPresenter().playerViewFactory(Color.web("0x0096c9ff")));

		getRoot().add(playerListView, 0, 1);
		GridPane.setMargin(playerListView, new Insets(0, 0, 0, 10));
	}
}
