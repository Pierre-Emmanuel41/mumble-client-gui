package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.dictionary.impl.NotificationCenter;
import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import fr.pederobien.mumble.client.gui.interfaces.IObsGuiConfiguration;
import javafx.scene.Parent;

public abstract class ViewBase<T extends PresenterBase, U extends Parent> implements IObsGuiConfiguration {
	private static GuiConfiguration guiConfiguration;
	private T presenter;
	private U root;

	protected ViewBase(T presenter, U root) {
		this.presenter = presenter;
		this.root = root;
		guiConfiguration.addObserver(this);
	}

	/**
	 * Set the guiConfiguration. When changes are made in the specified configuration, then each view are updated.
	 * 
	 * @param guiConfiguration The gui configuration.
	 */
	public static void setGuiConfiguration(GuiConfiguration guiConfiguration) {
		ViewBase.guiConfiguration = guiConfiguration;
	}

	/**
	 * @return The root that contains all graphical components.
	 */
	public U getRoot() {
		return root;
	}

	/**
	 * @return The presenter associated to this view.
	 */
	protected T getPresenter() {
		return presenter;
	}

	/**
	 * Get the message associated to the given message code.
	 * 
	 * @param code The code associated to the message to display.
	 * @param args Additional parameters for the message to retrieve.
	 * 
	 * @return The string associated to the given code.
	 */
	protected String getMessage(IMessageCode code, Object... args) {
		return NotificationCenter.getInstance().getDictionaryContext().getMessage(new MessageEvent(guiConfiguration.getLocale(), code, args));
	}
}
