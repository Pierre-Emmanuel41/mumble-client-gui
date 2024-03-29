package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.event.ParameterMaxValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterMinValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
import fr.pederobien.mumble.client.gui.impl.view.SelectableSoundModifierView;
import fr.pederobien.mumble.client.player.interfaces.IChannelList;
import fr.pederobien.mumble.client.player.interfaces.ISoundModifierList;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.IEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class AddChannelPresenter extends OkCancelPresenter implements IEventListener {
	private IChannelList channelList;

	private SimpleLanguageProperty titleTextProperty;

	private StringProperty channelNameProperty;
	private SimpleLanguageProperty channelNameTextProperty;
	private ObjectProperty<Border> channelNameBorderProperty;
	private SimpleLanguageProperty channelNamePromptProperty;
	private SimpleTooltipProperty channelNameTooltipProperty;

	private SelectableSoundModifierPresenter selectableSoundModifierPresenter;
	private SelectableSoundModifierView selectableSoundModifierView;

	// Buttons ---------------------------------------------------
	private BooleanProperty okDisableProperty;

	private boolean isChannelNameValid;

	/**
	 * Creates a presenter in order to add a channel to the given list.
	 * 
	 * @param channelList The list to which a channel should be added.
	 */
	public AddChannelPresenter(IChannelList channelList) {
		this.channelList = channelList;

		titleTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_TITLE);

		channelNameProperty = new SimpleStringProperty();
		channelNameProperty.addListener((obs, oldValue, newValue) -> validateChannelName(newValue));
		channelNameTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_NAME);
		channelNameBorderProperty = new SimpleObjectProperty<Border>(null);
		channelNamePromptProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_NAME_PROMPT);
		channelNameTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.CHANNEL_NAME_TOOLTIP);

		ISoundModifierList soundModifierList = channelList.getServer().getSoundModifiers();
		selectableSoundModifierPresenter = new SelectableSoundModifierPresenter(getFormView(), soundModifierList, soundModifierList.getDefaultSoundModifier());
		selectableSoundModifierView = new SelectableSoundModifierView(selectableSoundModifierPresenter);

		isChannelNameValid = false;
		okDisableProperty = new SimpleBooleanProperty(true);
		EventManager.registerListener(this);
	}

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		if (!selectableSoundModifierPresenter.apply())
			return false;

		channelList.add(channelNameProperty.get(), selectableSoundModifierPresenter.getSelectedSoundModifier(), response -> handleAddChannelResponse(response));
		return true;
	}

	@Override
	public void onClosing() {
		selectableSoundModifierPresenter.onClosing();
		EventManager.unregisterListener(this);
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return The property to get the name of the channel to add.
	 */
	public StringProperty channelNameProperty() {
		return channelNameProperty;
	}

	/**
	 * @return The property to display "Channel name : ".
	 */
	public StringProperty channelNameTextProperty() {
		return channelNameTextProperty;
	}

	/**
	 * @return The border property when the channel name is defined by the user. The border becomes red when the channel name does not
	 *         respect the constraints and disappears when the channel name respects constraints.
	 */
	public ObjectProperty<Border> channelNameBorderProperty() {
		return channelNameBorderProperty;
	}

	/**
	 * @return The property to display "Channel name" as prompt.
	 */
	public StringProperty channelNamePromptProperty() {
		return channelNamePromptProperty;
	}

	/**
	 * @return The tooltip property to display on the channel name. This tooltip explains the constraints the channel name must
	 *         respect.
	 */
	public ObjectProperty<Tooltip> channelNameTooltipProperty() {
		return channelNameTooltipProperty;
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

	private void validateChannelName(String newName) {
		boolean isChannelNameLengthOk = newName.length() > 5;
		boolean isChannelNameWithoutSpaces = !newName.contains(" ");
		boolean isChannelNameUnique = true;

		if (channelList.get(newName).isPresent())
			isChannelNameUnique = false;

		if (isChannelNameLengthOk && isChannelNameUnique && isChannelNameWithoutSpaces) {
			channelNameBorderProperty.set(Border.EMPTY);
			isChannelNameValid = true;
		} else {
			channelNameBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			isChannelNameValid = false;
		}
		updateOkDisable();
	}

	private void updateOkDisable() {
		if (selectableSoundModifierPresenter.getOldSoundModifier().equals(selectableSoundModifierPresenter.getSelectedSoundModifier()))
			okDisableProperty.set(!isChannelNameValid);
		else {
			okDisableProperty.set(!isChannelNameValid || !selectableSoundModifierPresenter.isValid());
		}
	}

	private void handleAddChannelResponse(IResponse response) {
		ErrorPresenter.showAndWait(AlertType.ERROR, EMessageCode.ADD_CHANNEL_TITLE, EMessageCode.ADD_CHANNEL_NAME_RESPONSE, response);
	}
}
