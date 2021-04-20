package fr.pederobien.mumble.client.gui.impl;

import java.util.Locale;

import fr.pederobien.dictionary.impl.NotificationCenter;
import fr.pederobien.dictionary.interfaces.IDictionary;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsGuiConfiguration;
import fr.pederobien.utils.Observable;
import javafx.scene.text.Font;

public class GuiConfiguration implements IGuiConfiguration {
	private static final Font DEFAULT_FONT = Font.getDefault();
	private static final Locale DEFAULT_LOCALE = Locale.getDefault();

	private Observable<IObsGuiConfiguration> observers;
	private Font font;
	private Locale locale;

	public GuiConfiguration() {
		observers = new Observable<IObsGuiConfiguration>();
	}

	@Override
	public void addObserver(IObsGuiConfiguration obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsGuiConfiguration obs) {
		observers.removeObserver(obs);
	}

	@Override
	public Font getFont() {
		return font == null ? DEFAULT_FONT : font;
	}

	@Override
	public void setFont(Font font) {
		Font oldFont = getFont();
		this.font = font;
		observers.notifyObservers(obs -> obs.onFontChanged(oldFont, this.font));
	}

	@Override
	public Locale getLocale() {
		return locale == null ? DEFAULT_LOCALE : locale;
	}

	@Override
	public void setLocale(Locale locale) {
		Locale oldLocale = getLocale();
		this.locale = locale;
		observers.notifyObservers(obs -> obs.onLanguageChanged(oldLocale, this.locale));
	}

	@Override
	public void registerDictionary(IDictionary dictionary) {
		NotificationCenter.getInstance().getDictionaryContext().register(dictionary);
	}
}
