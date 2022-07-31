package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter.ErrorPresenterBuilder;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.view.SelectableSoundModifierView;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;

public class SoundModifierPresenter extends OkCancelPresenter {
	private IChannel channel;

	private SimpleLanguageProperty titleTextProperty;

	private SelectableSoundModifierPresenter selectableSoundModifierPresenter;
	private SelectableSoundModifierView selectableSoundModifierView;

	public SoundModifierPresenter(IChannel channel) {
		this.channel = channel;

		selectableSoundModifierPresenter = new SelectableSoundModifierPresenter(channel.getServer().getSoundModifiers(), channel.getSoundModifier());
		selectableSoundModifierView = new SelectableSoundModifierView(selectableSoundModifierPresenter);
		titleTextProperty = getPropertyHelper().languageProperty(EMessageCode.SOUND_MODIFIER_TITLE, channel.getName());
	}

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty().get())
			return false;

		if (!selectableSoundModifierPresenter.onOkButtonClicked())
			return false;

		channel.setSoundModifier(selectableSoundModifierPresenter.getSelectedSoundModifier(), response -> handleSetChannelSoundModifierResponse(response));
		return true;
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return selectableSoundModifierPresenter.okDisableProperty();
	}

	@Override
	public void onClosing() {
		selectableSoundModifierPresenter.onClosing();
	}

	/**
	 * @return The view that allows the user to select a sound modifier.
	 */
	public SelectableSoundModifierView getSelectableSoundModifierView() {
		return selectableSoundModifierView;
	}

	private void handleSetChannelSoundModifierResponse(IResponse response) {
		ErrorPresenterBuilder builder = ErrorPresenterBuilder.of(AlertType.ERROR);
		builder.title(EMessageCode.SOUND_MODIFIER_TITLE, channel.getName());
		builder.header(EMessageCode.SOUND_MODIFIER_NAME_RESPONSE, selectableSoundModifierPresenter.getSelectedSoundModifier().getName(), channel.getName());
		builder.error(response);
		builder.showAndWait();
	}
}
