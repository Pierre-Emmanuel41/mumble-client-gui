package fr.pederobien.mumble.client.gui.environment;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.pederobien.dictionary.impl.JarDictionaryParser;
import fr.pederobien.mumble.client.gui.interfaces.IEnvironment;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;

public class ProductionEnvironment extends AbstractEnvironment implements IEnvironment {
	private JarDictionaryParser dictionaryParser;
	private Path jarPath;

	public ProductionEnvironment(String url, IGuiConfiguration guiConfiguration) {
		super(url, guiConfiguration);
		dictionaryParser = new JarDictionaryParser(getResourcesFolder().toString());
		jarPath = Paths.get(getUrl().split("!")[0].substring(String.format("%s:%s:/", IEnvironment.FILE_PREFIX, IEnvironment.JAR_PREFIX).length()));
	}

	@Override
	public void registerDictionary(String dictionaryName) throws FileNotFoundException {
		getGuiConfiguration().registerDictionary(dictionaryParser.setName(getDictionaryPath(dictionaryName)).parse(jarPath));
	}
}
