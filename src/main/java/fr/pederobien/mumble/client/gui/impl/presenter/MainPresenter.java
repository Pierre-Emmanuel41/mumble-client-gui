package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ServerManagementView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import javafx.beans.property.StringProperty;

public class MainPresenter extends PresenterBase {
	private ServerListView serverListView;
	private ServerManagementView serverManagementView;
	private StringProperty titleLanguageProperty;

	public MainPresenter() {
		ServerListPresenter serverListPresenter = new ServerListPresenter(ServerListPersistence.getInstance().get());
		serverListView = new ServerListView(serverListPresenter);

		ServerManagementPresenter serverManagementPresenter = new ServerManagementPresenter(ServerListPersistence.getInstance().get());
		serverListPresenter.addObserver(serverManagementPresenter);
		serverManagementView = new ServerManagementView(serverManagementPresenter);

		titleLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.MUMBLE_WINDOW_TITLE);
	}

	@Override
	public void onCloseRequest() {
		GuiConfigurationPersistence.getInstance().save();
		ServerListPersistence.getInstance().save();
	}

	/**
	 * @return The string property corresponding to the title of the stage.
	 */
	public StringProperty titleLanguageProperty() {
		return titleLanguageProperty;
	}

	/**
	 * @return The server list view. This view contains a view for each registered server.
	 */
	public ServerListView getServerListView() {
		return serverListView;
	}

	/**
	 * @return The server management view. this view contains buttons to join, add, edit and delete a server.
	 */
	public ServerManagementView getServerManagementView() {
		return serverManagementView;
	}
}
