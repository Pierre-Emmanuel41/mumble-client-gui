package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.AddChannelPresenter;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class AddChannelView extends ViewBase<AddChannelPresenter, BorderPane> {
	private OkCancelStage okCancelStage;

	public AddChannelView(AddChannelPresenter presenter) {
		super(presenter, new BorderPane());

		// Channel name
		Label channelNameLabel = new Label();
		channelNameLabel.fontProperty().bind(getPresenter().fontProperty());
		channelNameLabel.textProperty().bind(getPresenter().channelNameTextProperty());

		TextField channelNameTextField = new TextField();
		channelNameTextField.fontProperty().bind(getPresenter().fontProperty());
		channelNameTextField.promptTextProperty().bind(getPresenter().channelNamePromptProperty());
		channelNameTextField.textProperty().bindBidirectional(getPresenter().channelNameProperty());
		channelNameTextField.borderProperty().bind(getPresenter().channelNameBorderProperty());
		channelNameTextField.tooltipProperty().bind(getPresenter().channelNameTooltipProperty());

		getPresenter().getFormView().addRow(channelNameLabel, channelNameTextField);

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
			getPresenter().getFormView().removeRow(i + 2);

		for (ParameterView parameterView : newValue.getPresenter().getParameterViews())
			getPresenter().getFormView().addRow(parameterView.getParameterName(), parameterView.getValueView());

		okCancelStage.sizeToScene();
	}
}
