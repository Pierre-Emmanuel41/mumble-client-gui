package fr.pederobien.mumble.client.gui.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.image.Image;

public interface IEnvironment {
	public static final String FILE_PREFIX = "file";
	public static final String JAR_PREFIX = "jar";

	/**
	 * Try to find the dictionary file associated to the given dictionary name. If found, then the file is parsed and the dictionary
	 * is registered.
	 * 
	 * @param dictionaryName The dictionary name used to find its associated file.
	 * 
	 * @throws FileNotFoundException if there is no file associated to the given dictionary name.
	 */
	void registerDictionary(String dictionaryName) throws Exception;

	/**
	 * Try to find the file associated to given image name. If found, then the file is parsed and an image is returned.
	 * 
	 * @param imageName The image name to load.
	 * 
	 * @return The associated image.
	 * 
	 * @throws IOException If there is no file associated to the given dictionary name.
	 */
	Image loadImage(String imageName) throws IOException;
}
