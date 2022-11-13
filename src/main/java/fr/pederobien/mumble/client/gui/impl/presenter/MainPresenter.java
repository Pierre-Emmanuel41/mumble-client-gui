package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ServerManagementView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import fr.pederobien.mumble.client.player.interfaces.IPlayerMumbleServer;
import fr.pederobien.utils.event.EventLogger;

public class MainPresenter extends PresenterBase {
	private ServerListView serverListView;
	private ServerManagementView serverManagementView;

	public MainPresenter() {
		setPrimaryStageTitle(EGuiCode.MUMBLE_WINDOW_TITLE);

		for (IPlayerMumbleServer server : ServerListPersistence.getInstance().getServerList().getServers())
			server.open();

		serverListView = new ServerListView(new ServerListPresenter(ServerListPersistence.getInstance().getServerList()));
		serverManagementView = new ServerManagementView(new ServerManagementPresenter(ServerListPersistence.getInstance().getServerList()));
	}

	@Override
	public void onCloseRequest() {
		GuiConfigurationPersistence.getInstance().serialize();
		ServerListPersistence.getInstance().serialize();
		EventLogger.instance().unregister();
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
