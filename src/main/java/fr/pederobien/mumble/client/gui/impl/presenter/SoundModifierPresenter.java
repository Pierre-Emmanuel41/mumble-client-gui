package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.event.ParameterMaxValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterMinValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter.ErrorPresenterBuilder;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.SelectableSoundModifierView;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;

public class SoundModifierPresenter extends OkCancelPresenter implements IEventListener {
	private IChannel channel;

	private SimpleLanguageProperty titleTextProperty;
	private SimpleBooleanProperty okDisableProperty;

	private SelectableSoundModifierPresenter selectableSoundModifierPresenter;
	private SelectableSoundModifierView selectableSoundModifierView;

	/**
	 * Creates a presenter in order to modify the sound modifier of a channel.
	 * 
	 * @param channel The channel associated to this presenter.
	 */
	public SoundModifierPresenter(IChannel channel) {
		this.channel = channel;

		selectableSoundModifierPresenter = new SelectableSoundModifierPresenter(getFormView(), channel.getServer().getSoundModifiers(), channel.getSoundModifier());
		selectableSoundModifierPresenter.selectedSoundModifierNameProperty().addListener((obs, oldValue, newValue) -> updateOkDisable());
		selectableSoundModifierView = new SelectableSoundModifierView(selectableSoundModifierPresenter);
		titleTextProperty = getPropertyHelper().languageProperty(EGuiCode.SOUND_MODIFIER_TITLE, channel.getName());

		okDisableProperty = new SimpleBooleanProperty(true);
		EventManager.registerListener(this);
	}

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty().get())
			return false;

		if (!selectableSoundModifierPresenter.apply())
			return false;

		channel.setSoundModifier(selectableSoundModifierPresenter.getSelectedSoundModifier(), response -> handleSetChannelSoundModifierResponse(response));
		return true;
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	@Override
	public void onClosing() {
		selectableSoundModifierPresenter.onClosing();
		EventManager.unregisterListener(this);
	}

	/**
	 * @return The view that allows the user to select a sound modifier.
	 */
	public SelectableSoundModifierView selectableSoundModifierView() {
		return selectableSoundModifierView;
	}

	@EventHandler
	private void onParameterValueChange(ParameterValueChangeRequestEvent event) {
		updateOkDisable();
	}

	@EventHandler
	private void onParameterMinValueChange(ParameterMinValueChangeRequestEvent event) {
		updateOkDisable();
	}

	@EventHandler
	private void onParameterMaxValueChange(ParameterMaxValueChangeRequestEvent event) {
		updateOkDisable();
	}

	private void handleSetChannelSoundModifierResponse(IResponse response) {
		ErrorPresenterBuilder builder = ErrorPresenterBuilder.of(AlertType.ERROR);
		builder.title(EGuiCode.SOUND_MODIFIER_TITLE, channel.getName());
		builder.header(EGuiCode.SOUND_MODIFIER_NAME_RESPONSE, selectableSoundModifierPresenter.getSelectedSoundModifier().getName(), channel.getName());
		builder.error(response);
		builder.showAndWait();
	}

	private void updateOkDisable() {
		okDisableProperty.set(selectableSoundModifierPresenter.isIdentical() || !selectableSoundModifierPresenter.isValid());
	}
}
