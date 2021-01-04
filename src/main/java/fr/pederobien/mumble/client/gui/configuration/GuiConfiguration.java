package fr.pederobien.mumble.client.gui.configuration;

import java.util.Locale;

import fr.pederobien.mumble.client.gui.interfaces.observers.model.IObsGuiConfiguration;
import fr.pederobien.persistence.interfaces.IUnmodifiableNominable;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;
import javafx.scene.text.Font;

public class GuiConfiguration implements IUnmodifiableNominable, IObservable<IObsGuiConfiguration> {
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final Font DEFAULT_FONT = Font.getDefault();

	private Observable<IObsGuiConfiguration> observers;
	private Locale locale;
	private Font font;

	public GuiConfiguration() {
		observers = new Observable<IObsGuiConfiguration>();
	}

	@Override
	public String getName() {
		return "GuiConfiguration";
	}

	@Override
	public void addObserver(IObsGuiConfiguration obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsGuiConfiguration obs) {
		observers.removeObserver(obs);
	}

	/**
	 * @return The gui locale, this correspond to the language in which user messages are displayed.
	 */
	public Locale getLocale() {
		return locale == null ? DEFAULT_LOCALE : locale;
	}

	/**
	 * Set the gui locale. This correspond to the language in which user messages are displayed. Changing this locale will
	 * automatically change all user message.
	 * 
	 * @param locale The new gui locale.
	 */
	public void setLocale(Locale locale) {
		if (this.locale != null && this.locale.equals(locale))
			return;

		Locale oldLocale = getLocale();
		this.locale = locale;
		observers.notifyObservers(obs -> obs.onLanguageChanged(oldLocale, locale));
	}

	/**
	 * @return The gui font, this correspond to the font used by each graphical component to display messages.
	 */
	public Font getFont() {
		return font == null ? DEFAULT_FONT : font;
	}

	public void setFont(Font font) {
		if (this.font != null && this.font.equals(font))
			return;
		Font oldFont = getFont();
		this.font = font;
		observers.notifyObservers(obs -> obs.onFontChanged(oldFont, font));
	}
}
