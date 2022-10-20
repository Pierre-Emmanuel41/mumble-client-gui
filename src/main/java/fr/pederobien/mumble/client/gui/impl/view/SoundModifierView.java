package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.SoundModifierPresenter;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.scene.layout.BorderPane;

public class SoundModifierView extends ViewBase<SoundModifierPresenter, BorderPane> implements IOkCancelView {

	public SoundModifierView(SoundModifierPresenter presenter) {
		super(presenter, new BorderPane());

		getRoot().setTop(getPresenter().getSelectableSoundModifierView().getRoot());
		getPresenter().getSelectableSoundModifierView().setStage(new OkCancelStage(getPrimaryStage(), this).getStage());
	}

	@Override
	public void onPostShown() {
		getPresenter().getSelectableSoundModifierView().computeWidth();
	}
}
