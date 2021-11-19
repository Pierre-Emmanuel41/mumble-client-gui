package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.stream.Collectors;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IResponse;
import fr.pederobien.mumble.client.interfaces.ISoundModifier;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SoundModifierPresenter extends OkCancelPresenter {
	private IChannel channel;
	private ISoundModifier currentSoundModifier, newSoundModifier;

	private SimpleLanguageProperty titleTextProperty;
	private SimpleLanguageProperty modifierNameTextProperty;
	private BooleanProperty okDisableProperty;

	private ObservableList<ISoundModifier> soundModifiers;

	public SoundModifierPresenter(IChannel channel) {
		this.channel = channel;
		currentSoundModifier = newSoundModifier = channel.getSoundModifier();
		soundModifiers = FXCollections.observableArrayList(channel.getMumbleServer().getSoundModifierList().getSoundModifiers().values());

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

		channel.setSoundModifier(newSoundModifier.getName(), response -> soundModifierResponse(response));
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
		return soundModifiers.stream().map(modifier -> modifier.getName()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
	}

	/**
	 * @return The name of the current sound modifier of the channel.
	 */
	public String getCurrentSoundModifierName() {
		return currentSoundModifier.getName();
	}

	/**
	 * Verify if the new sound modifier name equals the current sound modifier name. If that is the case, then the user cannot click
	 * on the ok button.
	 * 
	 * @param newValue The new value of the sound modifier name.
	 */
	public void onSoundModifierChanged(String newValue) {
		newSoundModifier = soundModifiers.stream().filter(modifier -> modifier.getName().equals(newValue)).findFirst().get();
		okDisableProperty.set(newSoundModifier.equals(currentSoundModifier));
	}

	private void soundModifierResponse(IResponse response) {
		handleRequestFailed(response, AlertType.ERROR, p -> p.title(EMessageCode.SOUND_MODIFIER_TITLE, channel.getName())
				.header(EMessageCode.SOUND_MODIFIER_NAME_RESPONSE, newSoundModifier.getName(), channel.getName()));
	}
}
