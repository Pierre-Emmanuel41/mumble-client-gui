package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.javafx.configuration.impl.properties.SimpleLanguageProperty;
import fr.pederobien.messenger.interfaces.IResponse;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.ErrorPresenter.ErrorPresenterBuilder;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.player.interfaces.IChannel;
import fr.pederobien.mumble.client.player.interfaces.IChannelList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert.AlertType;
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
	private ObjectProperty<Border> channelNameBorderProperty;

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

		titleTextProperty = getPropertyHelper().newLanguageProperty(EGuiCode.RENAME_CHANNEL_TITLE, channel.getName());

		channelNameProperty = new SimpleStringProperty(channel.getName());
		channelNameProperty.addListener((obs, oldValue, newValue) -> validateChannelName());
		channelNameBorderProperty = new SimpleObjectProperty<Border>(null);

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
	 * @return The border property that is update when user write the new channel name. If the channel name does not respect the
	 *         constraints then the border becomes red. Otherwise the border is empty.
	 */
	public ObjectProperty<Border> channelNameBorderProperty() {
		return channelNameBorderProperty;
	}

	/**
	 * Apply validation rule on the new channel name. It update the enabled property of the ok button and the border property.
	 */
	private void validateChannelName() {
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
		builder.title(EGuiCode.RENAME_CHANNEL_TITLE, channel.getName());
		builder.header(EGuiCode.RENAME_CHANNEL_NAME_RESPONSE, channel.getName(), channelNameProperty.get());
		builder.error(response);
		builder.showAndWait();
	}
}
