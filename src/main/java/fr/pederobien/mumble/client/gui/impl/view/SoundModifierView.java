package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.SoundModifierPresenter;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;

public class SoundModifierView extends ViewBase<SoundModifierPresenter, BorderPane> {
	private OkCancelStage okCancelStage;

	/**
	 * Creates a view in order to modify the sound modifier of a channel.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public SoundModifierView(SoundModifierPresenter presenter) {
		super(presenter, new BorderPane());

		// Sound modifier name
		SelectableSoundModifierView soundModifierView = getPresenter().selectableSoundModifierView();
		getPresenter().getFormView().addRow(soundModifierView.getSoundModifierNameLabel(), soundModifierView.getSoundModifierNames());

		// Sound modifier parameters
		ObservableList<ParameterView> parameterViews = soundModifierView.getPresenter().selectedParameterListViewProperty().get().getPresenter().getParameterViews();
		for (ParameterView parameterView : parameterViews)
			getPresenter().getFormView().addRow(parameterView.getParameterName(), parameterView.getValueView());

		soundModifierView.getPresenter().selectedParameterListViewProperty().addListener((obs, oldValue, newValue) -> onSoundModifierChange(oldValue, newValue));

		okCancelStage = new OkCancelStage(getPrimaryStage(), getPresenter());
		okCancelStage.show();
	}

	/**
	 * Method to call when the selected sound modifier has changed, in order to update the displayed parameters.
	 * 
	 * @param oldValue The parameters list associated to the previous selected sound modifier.
	 * @param newValue The parameters list associated to the new selected sound modifier.
	 */
	private void onSoundModifierChange(ParameterListView oldValue, ParameterListView newValue) {
		for (int i = 0; i < oldValue.getPresenter().getParameterViews().size(); i++)
			getPresenter().getFormView().removeRow(i + 1);

		for (ParameterView parameterView : newValue.getPresenter().getParameterViews())
			getPresenter().getFormView().addRow(parameterView.getParameterName(), parameterView.getValueView());

		okCancelStage.sizeToScene();
	}
}
