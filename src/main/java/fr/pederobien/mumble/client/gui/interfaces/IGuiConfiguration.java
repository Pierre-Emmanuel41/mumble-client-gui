package fr.pederobien.mumble.client.gui.interfaces;

import java.util.Locale;

import fr.pederobien.dictionary.interfaces.IDictionary;
import fr.pederobien.dictionary.interfaces.IMessageCode;
import javafx.scene.text.Font;

public interface IGuiConfiguration {

	/**
	 * Get the font of the gui configuration. If this configuration is used by a style, then each component created by the style use
	 * the configuration font.
	 * 
	 * @return The gui Font
	 */
	Font getFont();

	/**
	 * Set the gui font. If components were already created using the previous font, then they are updated.
	 * 
	 * @param The new font.
	 */
	void setFont(Font font);

	/**
	 * @return The gui locale, this correspond to the language in which user messages are displayed.
	 */
	public Locale getLocale();

	/**
	 * Set the gui locale. This correspond to the language in which user messages are displayed. Changing this locale will
	 * automatically change all user message.
	 * 
	 * @param locale The new gui locale.
	 */
	public void setLocale(Locale locale);

	/**
	 * Register the specified dictionary for this gui configuration. All registered dictionaries are used to display message
	 * associated to code.
	 * 
	 * @param dictionary The dictionary to register.
	 * 
	 * @see IMessageCode
	 */
	public void registerDictionary(IDictionary dictionary);
}
