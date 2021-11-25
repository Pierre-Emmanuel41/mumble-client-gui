package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.ParameterListView;
import fr.pederobien.mumble.client.interfaces.ISoundModifier;
import fr.pederobien.mumble.client.interfaces.ISoundModifierList;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectableSoundModifierPresenter extends OkCancelPresenter implements IEventListener {
	private ISoundModifierList soundModifierList;
	private ISoundModifier initialSoundModifier, selectedSoundModifier;

	private SimpleLanguageProperty modifierNameTextProperty;
	private StringProperty selectedSoundModifierNameProperty;
	private ObjectProperty<ParameterListView> selectedParameterListViewProperty;
	private BooleanProperty okDisableProperty;
	private Map<ISoundModifier, ParameterListView> parameterListViews;

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

		okDisableProperty = new SimpleBooleanProperty(true);
		EventManager.registerListener(this);
	}

	@Override
	public StringProperty titleTextProperty() {
		return null;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		return selectedParameterListViewProperty.get().getPresenter().onOkButtonClicked();
	}

	@Override
	public void onClosing() {
		parameterListViews.values().forEach(view -> view.getPresenter().onClosing());
		EventManager.unregisterListener(this);
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return An observable list that contains the name of the sound modifiers registered on the server.
	 */
	public ObservableList<String> modifierNames() {
		return soundModifierList.getSoundModifiers().values().stream().map(modifier -> modifier.getName())
				.collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
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

	@EventHandler(priority = EventPriority.HIGH)
	private void onParameterValueChange(ParameterValueChangeRequestEvent event) {
		boolean identicalSoundModifier = selectedSoundModifier.equals(initialSoundModifier);
		boolean isNotValid = selectedParameterListViewProperty.get().getPresenter().isNotValid(false);
		boolean isIdentical = selectedParameterListViewProperty.get().getPresenter().isIdentical(false);
		okDisableProperty.set(identicalSoundModifier ? isNotValid || isIdentical : isNotValid);
	}

	private void onSelectedSoundModifierChange(String oldValue, String newValue) {
		Optional<ISoundModifier> optModifier = soundModifierList.getByName(newValue);
		if (!optModifier.isPresent()) {
			dispatch(() -> selectedSoundModifierNameProperty.set(oldValue));
			return;
		}

		selectedSoundModifier = optModifier.get();
		ParameterListView selectedParameterListView = parameterListViews.get(selectedSoundModifier);
		if (selectedParameterListView == null) {
			selectedParameterListView = new ParameterListView(new ParameterListPresenter(selectedSoundModifier.getParameters()));
			parameterListViews.put(selectedSoundModifier, selectedParameterListView);
		}
		selectedParameterListViewProperty.set(selectedParameterListView);
		onParameterValueChange(null);
	}
}
