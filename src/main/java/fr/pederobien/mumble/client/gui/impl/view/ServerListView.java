package fr.pederobien.mumble.client.gui.impl.view;

import java.util.Locale;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {
	private Label emptyServerListLabel;

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());

		getRoot().setPrefHeight(500);
		getRoot().setPrefWidth(1500);

		emptyServerListLabel = new Label(getMessage(getPresenter().getEmptyServerListCode()));

		if (getPresenter().getServers().isEmpty())
			getRoot().getChildren().add(emptyServerListLabel);
	}

	@Override
	public void onLanguageChanged(Locale oldLocale, Locale newLocale) {
		emptyServerListLabel.setText(getMessage(getPresenter().getEmptyServerListCode()));
	}
}
