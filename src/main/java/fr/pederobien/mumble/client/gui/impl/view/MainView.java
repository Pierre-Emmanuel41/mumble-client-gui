package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.MainPresenter;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class MainView extends ViewBase<MainPresenter, BorderPane> {

	public MainView(MainPresenter presenter) {
		super(presenter, new BorderPane());

		getRoot().setCenter(getPresenter().getServerListView().getRoot());
		getRoot().setBottom(getPresenter().getServerManagementView().getRoot());

		BorderPane.setAlignment(getPresenter().getServerListView().getRoot(), Pos.CENTER);
		BorderPane.setAlignment(getPresenter().getServerManagementView().getRoot(), Pos.CENTER);
	}
}
