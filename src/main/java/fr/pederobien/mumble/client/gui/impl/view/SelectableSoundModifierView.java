package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.javafx.configuration.impl.components.SimpleCombobox;
import fr.pederobien.javafx.configuration.impl.components.SimpleLabel;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.presenter.SelectableSoundModifierPresenter;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SelectableSoundModifierView extends ViewBase<SelectableSoundModifierPresenter, BorderPane> {
	private SimpleLabel soundModifierNameLabel;
	private SimpleCombobox<String> soundModifierNames;

	/**
	 * Creates a view in order to let the user to chose a sound modifier for a channel.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public SelectableSoundModifierView(SelectableSoundModifierPresenter presenter) {
		super(presenter, new BorderPane());

		soundModifierNameLabel = new SimpleLabel(EGuiCode.SOUND_MODIFIER_NAME);

		soundModifierNames = new SimpleCombobox<String>(getPresenter().modifierNames());
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
