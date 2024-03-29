package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter.ErrorPresenterBuilder;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleLanguageProperty;
import fr.pederobien.mumble.client.gui.impl.properties.SimpleTooltipProperty;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.mumble.client.player.interfaces.IChannelList;
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

public class RenameChannelPresenter extends OkCancelPresenter {
	private IChannelList channelList;
	private IChannel channel;

	private SimpleLanguageProperty titleTextProperty;

	private StringProperty channelNameProperty;
	private SimpleLanguageProperty channelNameTextProperty;
	private ObjectProperty<Border> channelNameBorderProperty;
	private SimpleTooltipProperty channelNameTooltipProperty;

	// Buttons ---------------------------------------------------
	private BooleanProperty okDisableProperty;

	/**
	 * Creates a presenter in order to rename a channel.
	 * 
	 * @param channelList The list in which the channel to rename is registered.
	 * @param channel     The channel to rename.
	 */
	public RenameChannelPresenter(IChannelList channelList, IChannel channel) {
		this.channelList = channelList;
		this.channel = channel;

		titleTextProperty = getPropertyHelper().languageProperty(EMessageCode.RENAME_CHANNEL_TITLE, channel.getName());

		channelNameProperty = new SimpleStringProperty(channel.getName());
		channelNameTextProperty = getPropertyHelper().languageProperty(EMessageCode.RENAME_CHANNEL_NAME);
		channelNameBorderProperty = new SimpleObjectProperty<Border>(null);
		channelNameTooltipProperty = getPropertyHelper().tooltipProperty(EMessageCode.CHANNEL_NAME_TOOLTIP);

		okDisableProperty = new SimpleBooleanProperty(true);
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		channel.setName(channelNameProperty.get(), response -> handleRenameChannelResponse(response));
		return true;
	}

	@Override
	public StringProperty titleTextProperty() {
		return titleTextProperty;
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return The property that contains the new channel name.
	 */
	public StringProperty channelNameProperty() {
		return channelNameProperty;
	}

	/**
	 * @return The property that contains the text "New channel name:"
	 */
	public StringProperty channelNameTextProperty() {
		return channelNameTextProperty;
	}

	/**
	 * @return The border property that is update when user write the new channel name. If the channel name does not respect the
	 *         constraints then the border becomes red. Otherwise the border is empty.
	 */
	public ObjectProperty<Border> channelNameBorderProperty() {
		return channelNameBorderProperty;
	}

	/**
	 * @return The property that contains the tooltip of the component on which the new channel name is written. This tooltip contains
	 *         a description of the constraints associated to the channel name.>
	 */
	public ObjectProperty<Tooltip> channelNameTooltipProperty() {
		return channelNameTooltipProperty;
	}

	/**
	 * Apply validation rule on the new channel name. It update the enabled property of the ok button and the border property.
	 */
	public void validateChannelName() {
		String channelName = channelNameProperty.get();
		boolean isChannelNameLengthOk = channelName.length() > 5;
		boolean isChannelNameWithoutSpaces = !channelName.contains(" ");
		boolean isChannelNameChanged = !channel.getName().equals(channelNameProperty.get());
		boolean isChannelNameUnique = true;

		if (channelList.get(channelName).isPresent())
			isChannelNameUnique = false;

		if (!isChannelNameChanged) {
			okDisableProperty.set(true);
			return;
		}

		if (isChannelNameLengthOk && isChannelNameUnique && isChannelNameWithoutSpaces && isChannelNameChanged) {
			channelNameBorderProperty.set(Border.EMPTY);
			okDisableProperty.set(false);
		} else {
			channelNameBorderProperty.set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
			okDisableProperty.set(true);
		}
	}

	private void handleRenameChannelResponse(IResponse response) {
		ErrorPresenterBuilder builder = ErrorPresenterBuilder.of(AlertType.ERROR);
		builder.title(EMessageCode.RENAME_CHANNEL_TITLE, channel.getName());
		builder.header(EMessageCode.RENAME_CHANNEL_NAME_RESPONSE, channel.getName(), channelNameProperty.get());
		builder.error(response);
		builder.showAndWait();
	}
}
