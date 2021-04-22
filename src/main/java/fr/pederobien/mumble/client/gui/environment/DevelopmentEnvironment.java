package fr.pederobien.mumble.client.gui.environment;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import fr.pederobien.dictionary.impl.DefaultDictionaryParser;
import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;

public class DevelopmentEnvironment extends AbstractEnvironment implements IEnvironment {
	private DefaultDictionaryParser dictionaryParser;

	public DevelopmentEnvironment(String url, IGuiConfiguration guiConfiguration) {
		super(url, guiConfiguration);
		dictionaryParser = new DefaultDictionaryParser();
	}

	@Override
	public void registerDictionary(String dictionaryName) throws FileNotFoundException {
		getGuiConfiguration().registerDictionary(dictionaryParser.parse(Paths.get(getDictionaryPath(dictionaryName))));
	}
}
