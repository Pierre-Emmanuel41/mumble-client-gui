package fr.pederobien.mumble.client.gui.interfaces;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import javafx.scene.Node;

public interface IOkCancelView {

	/**
	 * @return The root that contains all elements to display.
	 */
	Node getRoot();

	/**
	 * @return The presenter associated to this view.
	 */
	OkCancelPresenter getPresenter();

	/**
	 * Method called by the {@link OkCancelStage} once the stage has been shown. It could be useful when some calculation needs to be
	 * done about height, width or margin etc...
	 */
	void onPostShown();
}
