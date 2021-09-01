package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.event.ChannelAddedEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
import fr.pederobien.mumble.client.interfaces.IChannelList;
import fr.pederobien.mumble.client.interfaces.IResponse;
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

public class AddChannelPresenter extends OkCancelPresenter {
	private IChannelList channelList;

	private SimpleLanguageProperty titleTextProperty;

	private StringProperty channelNameProperty;
	private SimpleLanguageProperty channelNameTextProperty;
	private ObjectProperty<Border> channelNameBorderProperty;
	private SimpleLanguageProperty channelNamePromptProperty;
	private SimpleTooltipProperty channelNameTooltipProperty;

	// Buttons ---------------------------------------------------
	private BooleanProperty okDisableProperty;

	public AddChannelPresenter(IChannelList channelList) {
		this.channelList = channelList;

		titleTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_TITLE);

		channelNameProperty = new SimpleStringProperty();
		channelNameTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_NAME);
		channelNameBorderProperty = new SimpleObjectProperty<Border>(null);
		channelNamePromptProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_NAME_PROMPT);
		channelNameTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.CHANNEL_NAME_TOOLTIP);

		okDisableProperty = new SimpleBooleanProperty(true);
	}

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;
		channelList.addChannel(channelNameProperty.get(), null, response -> channelNameResponse(response));
		return true;
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

	public void validateChannelName() {
		String channelName = channelNameProperty.get();
		boolean isChannelNameLengthOk = channelName.length() > 5;
		boolean isChannelNameWithoutSpaces = !channelName.contains(" ");
		boolean isChannelNameUnique = true;

		if (channelList.getChannels().get(channelName) != null)
			isChannelNameUnique = false;

		if (isChannelNameLengthOk && isChannelNameUnique && isChannelNameWithoutSpaces) {
			channelNameBorderProperty.set(Border.EMPTY);
			okDisableProperty.set(false);
		} else {
			channelNameBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			okDisableProperty.set(true);
		}
	}

	private void channelNameResponse(IResponse<ChannelAddedEvent> response) {
		if (!response.hasFailed())
			return;

		dispatch(() -> {
			AlertPresenter alertPresenter = new AlertPresenter(AlertType.ERROR);
			alertPresenter.setTitle(EMessageCode.ADD_CHANNEL_TITLE);
			alertPresenter.setHeader(EMessageCode.ADD_CHANNEL_NAME_RESPONSE);
			alertPresenter.setContent(ErrorCodeWrapper.getByErrorCode(response.getErrorCode()).getMessageCode());
			alertPresenter.getAlert().show();
		});
	}
}
