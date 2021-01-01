package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import fr.pederobien.mumble.client.gui.interfaces.IObsGuiConfiguration;
import javafx.scene.Parent;

public abstract class ViewBase<T extends PresenterBase, U extends Parent> implements IObsGuiConfiguration {
	private T presenter;
	private U root;

	protected ViewBase(T presenter, U root) {
		this.presenter = presenter;
		this.root = root;
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
}
