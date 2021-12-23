package fr.pederobien.mumble.client.gui.environment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

import fr.pederobien.dictionary.impl.JarXmlDictionaryParser;
import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.scene.image.Image;

public class ProductionEnvironment extends AbstractEnvironment implements IEnvironment {
	private JarXmlDictionaryParser dictionaryParser;
	private Path jarPath;

	public ProductionEnvironment(String url, IGuiConfiguration guiConfiguration) {
		super(url, guiConfiguration);
		jarPath = Paths.get(getUrl().split("!")[0].substring(String.format("%s:%s:/", IEnvironment.FILE_PREFIX, IEnvironment.JAR_PREFIX).length()).replace("%20", " "));
		dictionaryParser = new JarXmlDictionaryParser(jarPath);
	}

	@Override
	public void registerDictionary(String dictionaryName) throws FileNotFoundException {
		getGuiConfiguration().getDictionaryContext().register(dictionaryParser.parse(Paths.get(getDictionaryPath(dictionaryName))));
	}

	@Override
	public Image loadImage(String imageName) throws IOException {
		JarFile jarFile = new JarFile(jarPath.toFile());
		Image image = new Image(jarFile.getInputStream(jarFile.getEntry(getImagePath(imageName))));
		jarFile.close();
		return image;
	}

}
