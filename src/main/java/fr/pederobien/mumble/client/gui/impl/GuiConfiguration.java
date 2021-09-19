package fr.pederobien.mumble.client.gui.impl;

import java.util.Locale;

import fr.pederobien.dictionary.impl.NotificationCenter;
import fr.pederobien.dictionary.interfaces.IDictionary;
import fr.pederobien.mumble.client.gui.event.FontChangeEvent;
import fr.pederobien.mumble.client.gui.event.LocaleChangeEvent;
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
		EventManager.callEvent(new FontChangeEvent(this, oldFont, getFont()));
	}

	@Override
	public Locale getLocale() {
		return locale == null ? DEFAULT_LOCALE : locale;
	}

	@Override
	public void setLocale(Locale locale) {
		Locale oldLocale = getLocale();
		this.locale = locale;
		EventManager.callEvent(new LocaleChangeEvent(this, oldLocale, getLocale()));
	}

	@Override
	public void registerDictionary(IDictionary dictionary) {
		NotificationCenter.getInstance().getDictionaryContext().register(dictionary);
	}
}
