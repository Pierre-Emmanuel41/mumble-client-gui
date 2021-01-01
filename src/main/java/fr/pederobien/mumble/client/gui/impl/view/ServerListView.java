package fr.pederobien.mumble.client.gui.impl.view;

import java.util.Locale;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerListPresenter;
import javafx.scene.layout.StackPane;

public class ServerListView extends ViewBase<ServerListPresenter, StackPane> {

	public ServerListView(ServerListPresenter presenter) {
		super(presenter, new StackPane());
	}

	@Override
	public void onLanguageChanged(Locale oldLocale, Locale newLocale) {

	}
}
