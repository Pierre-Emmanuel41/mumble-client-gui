package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IResponse;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SoundModifierPresenter extends OkCancelPresenter {
	private IChannel channel;
	private String currentSoundModifierName, newSoundModifierName;

	private SimpleLanguageProperty titleTextProperty;
	private SimpleLanguageProperty modifierNameTextProperty;
	private BooleanProperty okDisableProperty;

	private ObservableList<String> modifierNames;

	public SoundModifierPresenter(IChannel channel) {
		this.channel = channel;
		currentSoundModifierName = channel.getSoundModifier().getName();
		modifierNames = FXCollections.observableArrayList(channel.getSupportedSoundModifiers());

		titleTextProperty = getPropertyHelper().languageProperty(EMessageCode.SOUND_MODIFIER_TITLE, channel.getName());
		modifierNameTextProperty = getPropertyHelper().languageProperty(EMessageCode.SOUND_MODIFIER_NAME);
		okDisableProperty = new SimpleBooleanProperty(false);
	}

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		channel.getSoundModifier().setName(newSoundModifierName, response -> soundModifierResponse(response));
		return true;
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return The property that contains the text "New sound modifier :"
	 */
	public StringProperty modifierNameTextProperty() {
		return modifierNameTextProperty;
	}

	/**
	 * @return An observable list that contains the registered sound modifiers on the server.
	 */
	public ObservableList<String> modifierNames() {
		return modifierNames;
	}

	/**
	 * @return The name of the current sound modifier of the channel.
	 */
	public String getCurrentSoundModifierName() {
		return currentSoundModifierName;
	}

	/**
	 * Verify if the new sound modifier name equals the current sound modifier name. If that is the case, then the user cannot click
	 * on the ok button.
	 * 
	 * @param newValue The new value of the sound modifier name.
	 */
	public void onSoundModifierChanged(String newValue) {
		newSoundModifierName = newValue;
		okDisableProperty.set(currentSoundModifierName.equals(newValue));
	}

	private void soundModifierResponse(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, p -> p.title(EMessageCode.SOUND_MODIFIER_TITLE, channel.getName())
				.header(EMessageCode.SOUND_MODIFIER_NAME_RESPONSE, newSoundModifierName, channel.getName()));
	}
}
