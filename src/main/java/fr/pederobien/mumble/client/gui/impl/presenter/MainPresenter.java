package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.communication.impl.LogEventListener;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ServerManagementView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import javafx.beans.property.StringProperty;

public class MainPresenter extends PresenterBase {
	private ServerListView serverListView;
	private ServerManagementView serverManagementView;
	private StringProperty titleLanguageProperty;
	private LogEventListener logListener;

	public MainPresenter() {
		for (IMumbleServer server : ServerListPersistence.getInstance().get().getServers())
			server.open();

		serverListView = new ServerListView(new ServerListPresenter(ServerListPersistence.getInstance().get()));
		serverManagementView = new ServerManagementView(new ServerManagementPresenter(ServerListPersistence.getInstance().get()));
		titleLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.MUMBLE_WINDOW_TITLE);
		logListener = new LogEventListener();
		logListener.register();
	}

	@Override
	public void onCloseRequest() {
		GuiConfigurationPersistence.getInstance().save();
		ServerListPersistence.getInstance().save();
		logListener.unregister();
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
	 * @return The server management view. This view contains buttons to join, add, edit and delete a server.
	 */
	public ServerManagementView getServerManagementView() {
		return serverManagementView;
	}
}
