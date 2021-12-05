package fr.pederobien.mumble.client.gui.environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import fr.pederobien.dictionary.impl.XmlDictionaryParser;
import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import javafx.scene.image.Image;

public class DevelopmentEnvironment extends AbstractEnvironment implements IEnvironment {
	private XmlDictionaryParser dictionaryParser;

	public DevelopmentEnvironment(String url, IGuiConfiguration guiConfiguration) {
		super(url, guiConfiguration);
		dictionaryParser = new XmlDictionaryParser();
	}

	@Override
	public void registerDictionary(String dictionaryName) throws FileNotFoundException {
		getGuiConfiguration().registerDictionary(dictionaryParser.parse(Paths.get(getDictionaryPath(dictionaryName))));
	}

	@Override
	public Image loadImage(String imageName) throws FileNotFoundException {
		return new Image(new FileInputStream(getImagePath(imageName)));
	}
}
