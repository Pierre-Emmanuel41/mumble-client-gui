package fr.pederobien.mumble.client.gui.impl.presenter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.gui.impl.view.ServerListView;
import fr.pederobien.mumble.client.gui.impl.view.ServerManagementView;
import fr.pederobien.mumble.client.gui.persistence.configuration.GuiConfigurationPersistence;
import fr.pederobien.mumble.client.gui.persistence.model.ServerListPersistence;
import fr.pederobien.mumble.client.interfaces.IMumbleServer;
import fr.pederobien.sound.event.SoundEvent;
import fr.pederobien.utils.event.EventCalledEvent;
import fr.pederobien.utils.event.EventLogger;
import javafx.beans.property.StringProperty;

public class MainPresenter extends PresenterBase {
	private ServerListView serverListView;
	private ServerManagementView serverManagementView;
	private StringProperty titleLanguageProperty;

	static {
		EventLogger.instance().register();
		EventLogger.instance().ignore(SoundEvent.class);
	}

	public MainPresenter() {
		for (IMumbleServer server : ServerListPersistence.getInstance().get().getServers())
			server.open();

		serverListView = new ServerListView(new ServerListPresenter(ServerListPersistence.getInstance().get()));
		serverManagementView = new ServerManagementView(new ServerManagementPresenter(ServerListPersistence.getInstance().get()));
		titleLanguageProperty = getPropertyHelper().languageProperty(EMessageCode.MUMBLE_WINDOW_TITLE);
	}

	@Override
	public void onCloseRequest() {
		GuiConfigurationPersistence.getInstance().save();
		ServerListPersistence.getInstance().save();
		EventLogger.instance().unregister();
		saveLog();
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

	private void saveLog() {
		// Creates intermediate folders if they don't exist.
		if (!Files.exists(Variables.LOG_FOLDER.getPath()))
			Variables.LOG_FOLDER.getPath().toFile().mkdirs();

		String name = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
		Path logPath = Variables.LOG_FOLDER.getPath().resolve(String.format("log_%s.zip", name));

		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(logPath.toFile()));
			ZipEntry zipEntry = new ZipEntry(String.format("log_%s.txt", name));
			zipOutputStream.putNextEntry(zipEntry);

			for (EventCalledEvent event : EventLogger.instance().getEvents()) {
				String entry = String.format("[%s %s] %s\r\n", event.getTime().toLocalDate(), event.getTime().toLocalTime(), event.getEvent().toString());
				zipOutputStream.write(entry.getBytes());
			}

			zipOutputStream.closeEntry();
			zipOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
