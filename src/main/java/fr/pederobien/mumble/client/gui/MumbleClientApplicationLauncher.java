package fr.pederobien.mumble.client.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.pederobien.communication.event.ConnectionEvent;
import fr.pederobien.dictionary.event.DictionaryEvent;
import fr.pederobien.mumble.client.external.event.PlayerPositionChangePostEvent;
import fr.pederobien.mumble.client.external.event.PlayerPositionChangePreEvent;
import fr.pederobien.mumble.client.gui.environment.Variables;
import fr.pederobien.mumble.client.player.event.MumblePlayerPositionChangePostEvent;
import fr.pederobien.sound.event.ProjectSoundEvent;
import fr.pederobien.utils.event.EventCalledEvent;
import fr.pederobien.utils.event.EventLogger;
import fr.pederobien.vocal.client.event.VocalPlayerSpeakPostEvent;
import fr.pederobien.vocal.client.event.VocalPlayerSpeakPreEvent;

public class MumbleClientApplicationLauncher {
	private static MumbleClientApplication application;

	/**
	 * Fake main in order to run the application from a jar file.
	 * 
	 * @param args the command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		EventLogger.instance().newLine(true).timeStamp(true).register();

		EventLogger.instance().ignore(DictionaryEvent.class);
		EventLogger.instance().ignore(ConnectionEvent.class);
		EventLogger.instance().ignore(ProjectSoundEvent.class);
		EventLogger.instance().ignore(VocalPlayerSpeakPreEvent.class);
		EventLogger.instance().ignore(VocalPlayerSpeakPostEvent.class);
		EventLogger.instance().ignore(PlayerPositionChangePreEvent.class);
		EventLogger.instance().ignore(PlayerPositionChangePostEvent.class);
		EventLogger.instance().ignore(MumblePlayerPositionChangePostEvent.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> saveLog()));
		application = new MumbleClientApplication();
		application.run(args);
	}

	private static void saveLog() {
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
