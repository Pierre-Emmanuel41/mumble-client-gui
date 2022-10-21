package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.SelectableSoundModifierPresenter;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SelectableSoundModifierView extends ViewBase<SelectableSoundModifierPresenter, BorderPane> {
	private Label soundModifierNameLabel;
	private ComboBox<String> soundModifierNames;

	public SelectableSoundModifierView(SelectableSoundModifierPresenter presenter) {
		super(presenter, new BorderPane());

		soundModifierNameLabel = new Label();
		soundModifierNameLabel.fontProperty().bind(getPresenter().fontProperty());
		soundModifierNameLabel.textProperty().bind(getPresenter().modifierNameTextProperty());

		soundModifierNames = new ComboBox<String>();
		soundModifierNames.getEditor().fontProperty().bind(getPresenter().fontProperty());
		soundModifierNames.itemsProperty().set(getPresenter().modifierNames());
		soundModifierNames.valueProperty().bindBidirectional(getPresenter().selectedSoundModifierNameProperty());
	}

	/**
	 * @return The label that display the name of the selected sound modifier.
	 */
	public Label getSoundModifierNameLabel() {
		return soundModifierNameLabel;
	}

	/**
	 * @return A combobox that contains all registered sound modifier names
	 */
	public ComboBox<String> getSoundModifierNames() {
		return soundModifierNames;
	}
}
