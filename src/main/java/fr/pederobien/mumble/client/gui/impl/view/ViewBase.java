package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.dictionary.impl.NotificationCenter;
import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.configuration.GuiConfiguration;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import fr.pederobien.mumble.client.gui.interfaces.IObsGuiConfiguration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
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

	/**
	 * Adds a {@link ChangeListener} which will be notified whenever the value of the {@code ObservableValue} changes. If the same
	 * listener is added more than once, then it will be notified more than once. That is, no check is made to ensure uniqueness.
	 * <p>
	 * Note that the same actual {@code ChangeListener} instance may be safely registered for different {@code ObservableValues}.
	 * <p>
	 * The {@code ObservableValue} stores a strong reference to the listener which will prevent the listener from being garbage
	 * collected and may result in a memory leak. It is recommended to either unregister a listener by calling
	 * {@link #removeListener(ChangeListener) removeListener} after use or to use an instance of {@link WeakChangeListener} avoid this
	 * situation.
	 *
	 * @see #unregister(ObservableValue, ChangeListener)
	 *
	 * @param property the property to listen.
	 * @param listener The listener to register
	 * @throws NullPointerException if the listener is null
	 */
	protected <V> void register(ObservableValue<V> property, ChangeListener<? super V> listener) {
		property.addListener(listener);
	}

	/**
	 * Removes the given listener from the list of listeners, that are notified whenever the value of the {@code ObservableValue}
	 * changes.
	 * <p>
	 * If the given listener has not been previously registered (i.e. it was never added) then this method call is a no-op. If it had
	 * been previously added then it will be removed. If it had been added more than once, then only the first occurrence will be
	 * removed.
	 *
	 * @see #register(ObservableValue, ChangeListener)
	 * 
	 * @param property The property to unregister.
	 * @param listener The listener to remove
	 * @throws NullPointerException if the listener is null
	 */
	protected <V> void unregister(ObservableValue<V> property, ChangeListener<? super V> listener) {
		property.removeListener(listener);
	}
}
