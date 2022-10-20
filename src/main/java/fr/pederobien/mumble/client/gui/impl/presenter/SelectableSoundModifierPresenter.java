package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.ParameterListView;
import fr.pederobien.mumble.client.player.interfaces.ISoundModifier;
import fr.pederobien.mumble.client.player.interfaces.ISoundModifierList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectableSoundModifierPresenter extends PresenterBase {
	private ISoundModifierList soundModifierList;
	private ISoundModifier initialSoundModifier, selectedSoundModifier;

	private SimpleLanguageProperty modifierNameTextProperty;
	private StringProperty selectedSoundModifierNameProperty;
	private ObjectProperty<ParameterListView> selectedParameterListViewProperty;
	private Map<ISoundModifier, ParameterListView> parameterListViews;

	/**
	 * Creates a presenter in order to let the user selecting a sound modifier from the given list.
	 * 
	 * @param soundModifierList    The list that contains all the sound modifier available for selection.
	 * @param initialSoundModifier The sound modifier initially selected.
	 */
	public SelectableSoundModifierPresenter(ISoundModifierList soundModifierList, ISoundModifier initialSoundModifier) {
		this.soundModifierList = soundModifierList;
		this.selectedSoundModifier = this.initialSoundModifier = initialSoundModifier;

		// Initializing map with the specified selected sound modifier.
		parameterListViews = new HashMap<ISoundModifier, ParameterListView>();
		parameterListViews.put(selectedSoundModifier, new ParameterListView(new ParameterListPresenter(selectedSoundModifier.getParameters())));
		selectedParameterListViewProperty = new SimpleObjectProperty<ParameterListView>(parameterListViews.get(selectedSoundModifier));

		modifierNameTextProperty = getPropertyHelper().languageProperty(EMessageCode.SOUND_MODIFIER_NAME);
		selectedSoundModifierNameProperty = new SimpleStringProperty(selectedSoundModifier.getName());
		selectedSoundModifierNameProperty.addListener((obs, oldValue, newValue) -> onSelectedSoundModifierChange(oldValue, newValue));
	}

	/**
	 * Update the current value of each parameter of the selected sound modifier. If a parameter is associated to a range, then the
	 * minimum/maximum value of the range is also updated.
	 * 
	 * @return True if each parameter has been successfully updated, false otherwise.
	 */
	public boolean apply() {
		return selectedParameterListViewProperty.get().getPresenter().apply();
	}

	/**
	 * Unregister the presenter of each parameter for event handling.
	 */
	public void onClosing() {
		parameterListViews.values().forEach(view -> view.getPresenter().onClosing());
	}

	/**
	 * @return True if the new parameter's value (resp. minimum/maximum value) is valid.
	 */
	public boolean isValid() {
		return selectedParameterListViewProperty.get().getPresenter().isValid();
	}

	/**
	 * @return True if the new parameter's value (resp. minimum/maximum value) equals the current parameter's value (resp.
	 *         minimum/maximum value).
	 */
	public boolean isIdentical() {
		return initialSoundModifier.equals(selectedSoundModifier) && selectedParameterListViewProperty.get().getPresenter().isIdentical();
	}

	/**
	 * @return An observable list that contains the name of the sound modifiers registered on the server.
	 */
	public ObservableList<String> modifierNames() {
		return soundModifierList.stream().map(modifier -> modifier.getName()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
	}

	/**
	 * @return The sound modifier initially selected.
	 */
	public ISoundModifier getOldSoundModifier() {
		return initialSoundModifier;
	}

	/**
	 * @return The sound modifier currently selected in the user interface.
	 */
	public ISoundModifier getSelectedSoundModifier() {
		return selectedSoundModifier;
	}

	/**
	 * @return The property that contains the text "New sound modifier :"
	 */
	public StringProperty modifierNameTextProperty() {
		return modifierNameTextProperty;
	}

	/**
	 * @return The name of the current sound modifier of the channel.
	 */
	public StringProperty selectedSoundModifierNameProperty() {
		return selectedSoundModifierNameProperty;
	}

	/**
	 * @return The parameter list view associated to the
	 */
	public ObjectProperty<ParameterListView> selectedParameterListViewProperty() {
		return selectedParameterListViewProperty;
	}

	private void onSelectedSoundModifierChange(String oldValue, String newValue) {
		Optional<ISoundModifier> optModifier = soundModifierList.get(newValue);
		if (!optModifier.isPresent()) {
			dispatch(() -> selectedSoundModifierNameProperty.set(oldValue));
			return;
		}

		selectedSoundModifier = optModifier.get();
		selectedSoundModifier = selectedSoundModifier.getName().equals(initialSoundModifier.getName()) ? initialSoundModifier : selectedSoundModifier;
		ParameterListView selectedParameterListView = parameterListViews.get(selectedSoundModifier);
		if (selectedParameterListView == null) {
			selectedParameterListView = new ParameterListView(new ParameterListPresenter(selectedSoundModifier.getParameters()));
			parameterListViews.put(selectedSoundModifier, selectedParameterListView);
		}
		selectedParameterListViewProperty.set(selectedParameterListView);
	}
}
