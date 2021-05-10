package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.event.ChannelAddedEvent;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.ErrorCodeWrapper;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
import fr.pederobien.mumble.client.interfaces.IChannel;
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

public class AddChannelPresenter extends PresenterBase {
	private IChannelList channelList;

	private SimpleLanguageProperty titleTextProperty;

	private StringProperty channelNameProperty;
	private SimpleLanguageProperty channelNameTextProperty;
	private ObjectProperty<Border> channelNameBorderProperty;
	private SimpleLanguageProperty channelNamePromptProperty;
	private SimpleTooltipProperty channelNameTooltipProperty;

	// Buttons ---------------------------------------------------
	private SimpleLanguageProperty okTextProperty;
	private BooleanProperty okDisableProperty;
	private SimpleLanguageProperty cancelTextProperty;

	public AddChannelPresenter(IChannelList channelList) {
		this.channelList = channelList;

		titleTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_TITLE);

		channelNameProperty = new SimpleStringProperty();
		channelNameTextProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_NAME);
		channelNameBorderProperty = new SimpleObjectProperty<Border>(null);
		channelNamePromptProperty = getPropertyHelper().languageProperty(EMessageCode.ADD_CHANNEL_NAME_PROMPT);
		channelNameTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.ADD_CHANNEL_NAME_TOOLTIP);

		okTextProperty = getPropertyHelper().languageProperty(EMessageCode.OK);
		okDisableProperty = new SimpleBooleanProperty(true);
		cancelTextProperty = getPropertyHelper().languageProperty(EMessageCode.CANCEL);
	}

	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	public StringProperty channelNameProperty() {
		return channelNameProperty;
	}

	public StringProperty channelNameTextProperty() {
		return channelNameTextProperty;
	}

	public ObjectProperty<Border> channelNameBorderProperty() {
		return channelNameBorderProperty;
	}

	public StringProperty channelNamePromptProperty() {
		return channelNamePromptProperty;
	}

	public ObjectProperty<Tooltip> channelNameTooltipProperty() {
		return channelNameTooltipProperty;
	}

	public StringProperty okTextProperty() {
		return okTextProperty;
	}

	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	public StringProperty cancelTextProperty() {
		return cancelTextProperty;
	}

	public void validateChannelName() {
		String channelName = channelNameProperty.get();
		boolean isChannelNameLengthOk = channelName.length() > 5;
		boolean isChannelNameWithoutSpaces = !channelName.contains(" ");
		boolean isChannelNameUnique = true;

		for (IChannel channel : channelList.getChannels())
			if (channel.getName().equals(channelName)) {
				isChannelNameUnique = false;
				break;
			}

		if (isChannelNameLengthOk && isChannelNameUnique && isChannelNameWithoutSpaces) {
			channelNameBorderProperty.set(Border.EMPTY);
			okDisableProperty.set(false);
		} else {
			channelNameBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			okDisableProperty.set(true);
		}
	}

	public boolean ok() {
		if (okDisableProperty.get())
			return false;
		channelList.addChannel(channelNameProperty.get(), response -> channelNameResponse(response));
		return true;
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
