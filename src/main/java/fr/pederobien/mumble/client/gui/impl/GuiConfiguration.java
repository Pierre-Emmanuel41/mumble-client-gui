package fr.pederobien.mumble.client.gui.impl;

import java.util.Locale;

import fr.pederobien.dictionary.impl.DictionaryContext;
import fr.pederobien.dictionary.interfaces.IDictionary;
import fr.pederobien.mumble.client.gui.event.FontChangePostEvent;
import fr.pederobien.mumble.client.gui.event.LocaleChangePostEvent;
import fr.pederobien.mumble.client.gui.interfaces.IGuiConfiguration;
import fr.pederobien.utils.event.EventManager;
import javafx.scene.text.Font;

public class GuiConfiguration implements IGuiConfiguration {
	private static final Font DEFAULT_FONT = Font.getDefault();
	private static final Locale DEFAULT_LOCALE = Locale.getDefault();

	private Font font;
	private Locale locale;

	@Override
	public Font getFont() {
		return font == null ? DEFAULT_FONT : font;
	}

	@Override
	public void setFont(Font font) {
		Font oldFont = getFont();
		this.font = font;
		EventManager.callEvent(new FontChangePostEvent(this, oldFont, getFont()));
	}

	@Override
	public Locale getLocale() {
		return locale == null ? DEFAULT_LOCALE : locale;
	}

	@Override
	public void setLocale(Locale locale) {
		Locale oldLocale = getLocale();
		this.locale = locale;
		EventManager.callEvent(new LocaleChangePostEvent(this, oldLocale, getLocale()));
	}

	@Override
	public void registerDictionary(IDictionary dictionary) {
		DictionaryContext.getInstance().register(dictionary);
	}
}
