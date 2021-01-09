package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.fxstyle.interfaces.IStyle;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import javafx.scene.Parent;

public abstract class ViewBase<T extends PresenterBase, U extends Parent> {
	private static IStyle style;
	private T presenter;
	private U root;

	protected ViewBase(T presenter, U root) {
		this.presenter = presenter;
		this.root = root;
	}

	/**
	 * Set the style used by each view to create graphical components.
	 * 
	 * @param style The view style.
	 */
	public static void setStyle(IStyle style) {
		ViewBase.style = style;
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
	 * @return The style that create graphical components.
	 */
	protected IStyle getStyle() {
		return style;
	}
}
