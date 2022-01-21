package fr.pederobien.mumble.client.gui.environment;

import java.io.FileInputStream;
import java.io.IOException;

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
	public void registerDictionary(String dictionaryName) throws Exception {
		getGuiConfiguration().getDictionaryContext().register(dictionaryParser.parse(getDictionaryPath(dictionaryName)));
	}

	@Override
	public Image loadImage(String imageName) throws IOException {
		return new Image(new FileInputStream(getImagePath(imageName)));
	}
}
