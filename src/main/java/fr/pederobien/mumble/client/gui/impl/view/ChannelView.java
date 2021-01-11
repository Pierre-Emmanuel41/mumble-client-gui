package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.fxstyle.impl.wrapper.ListViewWrapper;
import fr.pederobien.mumble.client.gui.impl.presenter.ChannelPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ChannelView extends ViewBase<ChannelPresenter, GridPane> {

	public ChannelView(ChannelPresenter presenter) {
		super(presenter, new GridPane());

		getRoot().setPadding(new Insets(10));

		Label channelName = getStyle().createLabel(getPresenter().channelNameProperty());
		channelName.setTextFill(Color.BLACK);
		channelName.setOnMouseEntered(e -> {
			channelName.setBackground(new Background(new BackgroundFill(Color.web("0x0096c9ff"), new CornerRadii(4), null)));
		});
		channelName.setOnMouseExited(e -> {
			channelName.setBackground(Background.EMPTY);
		});
		channelName.setOnMouseClicked(e -> getPresenter().onChannelClicked());

		getRoot().add(channelName, 0, 0);

		ListViewWrapper<Object> listWrapper = getStyle().createListView(getPresenter().getPlayers()).visibleIfNotEmpty().background(Background.EMPTY);
		listWrapper.cellView(getPresenter().playerViewConstructor(), Color.web("0x0096c9ff"));

		getRoot().add(listWrapper.get(), 0, 1);
		GridPane.setMargin(listWrapper.get(), new Insets(0, 0, 0, 10));
	}
}
