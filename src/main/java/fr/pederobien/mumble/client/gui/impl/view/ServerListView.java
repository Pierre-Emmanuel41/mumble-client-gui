package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.fxstyle.impl.wrapper.ListViewWrapper;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());

		Label emptyServerListLabel = getStyle().createLabel(getPresenter().emptyServerCode());
		emptyServerListLabel.visibleProperty().bind(getPresenter().emptyServersListVisibilityProperty());
		getRoot().getChildren().add(emptyServerListLabel);

		ListViewWrapper<Object> listWrapper = getStyle().createListView(getPresenter().getServers()).background(Background.EMPTY);
		listWrapper.visibleIfNotEmpty().onClick(e -> getPresenter().onDoubleClickOnSelectedServer(e));
		listWrapper.onSelectedItemChanged((obs, oldValue, newValue) -> getPresenter().onServerSelectedChanged(oldValue, newValue));
		listWrapper.cellView(getPresenter().serverViewConstructor(), Color.web("0x0096c9ff"));
		listWrapper.get().setMaxWidth(600);
		getRoot().getChildren().add(listWrapper.get());
	}
}
