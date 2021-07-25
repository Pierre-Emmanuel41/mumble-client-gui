package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ChannelPresenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ChannelView extends ViewBase<ChannelPresenter, GridPane> {

	public ChannelView(ChannelPresenter presenter) {
		super(presenter, new GridPane());

		Label channelName = new Label();
		channelName.fontProperty().bind(getPresenter().fontProperty());
		channelName.textProperty().bind(getPresenter().channelNameProperty());
		channelName.setTextFill(Color.BLACK);

		channelName.setOnMouseEntered(e -> {
			channelName.setBackground(new Background(new BackgroundFill(Color.web("0x0096c9ff"), new CornerRadii(4), null)));
		});

		channelName.setOnMouseExited(e -> {
			channelName.setBackground(Background.EMPTY);
		});

		channelName.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> getPresenter().onChannelClicked(e));

		// Context menu associated to the channel name in order to add remove or rename a channel.
		ContextMenu contextMenu = new ContextMenu();

		// Add channel ------------------------------------------------------------------------------------
		Label addChannelLabel = new Label();
		addChannelLabel.fontProperty().bind(getPresenter().fontProperty());
		addChannelLabel.textProperty().bind(getPresenter().addChannelTextProperty());
		addChannelLabel.setTextFill(Color.BLACK);

		MenuItem addChannel = new MenuItem();
		addChannel.setGraphic(addChannelLabel);
		addChannel.visibleProperty().bind(getPresenter().addChannelVisibility());
		addChannel.setOnAction(e -> getPresenter().onAddChannel());
		contextMenu.getItems().add(addChannel);

		// Remove channel ---------------------------------------------------------------------------------
		Label removeChannelLabel = new Label();
		removeChannelLabel.fontProperty().bind(getPresenter().fontProperty());
		removeChannelLabel.textProperty().bind(getPresenter().removeChannelTextProperty());
		removeChannelLabel.setTextFill(Color.BLACK);

		MenuItem removeChannel = new MenuItem();
		removeChannel.setGraphic(removeChannelLabel);
		removeChannel.visibleProperty().bind(getPresenter().removeChannelVisibility());
		removeChannel.setOnAction(e -> getPresenter().onRemoveChannel());
		contextMenu.getItems().add(removeChannel);

		// Rename channel ---------------------------------------------------------------------------------
		Label renameChannelLabel = new Label();
		renameChannelLabel.fontProperty().bind(getPresenter().fontProperty());
		renameChannelLabel.textProperty().bind(getPresenter().renameChannelTextProperty());
		renameChannelLabel.setTextFill(Color.BLACK);

		MenuItem renameChannel = new MenuItem();
		renameChannel.setGraphic(renameChannelLabel);
		renameChannel.visibleProperty().bind(getPresenter().renameChannelVisibility());
		renameChannel.setOnAction(e -> getPresenter().onRenameChannel());
		contextMenu.getItems().add(renameChannel);

		// Sound modifier ---------------------------------------------------------------------------------
		Label soundModifierLabel = new Label();
		soundModifierLabel.fontProperty().bind(getPresenter().fontProperty());
		soundModifierLabel.textProperty().bind(getPresenter().soundModifierTextProperty());
		soundModifierLabel.setTextFill(Color.BLACK);

		MenuItem soundModifier = new MenuItem();
		soundModifier.setGraphic(soundModifierLabel);
		soundModifier.visibleProperty().bind(getPresenter().soundModifierVisibility());
		soundModifier.setOnAction(e -> getPresenter().onSetSoundModifier());
		contextMenu.getItems().add(soundModifier);

		channelName.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> displayContextMenu(contextMenu, e, channelName));

		getRoot().add(channelName, 0, 0);

		ListView<Object> playerListView = new ListView<Object>();
		playerListView.setItems(getPresenter().getPlayers());

		// Visible if the list view is not empty.
		playerListView.visibleProperty().bind(Bindings.greaterThan(Bindings.size(playerListView.getItems()), new SimpleIntegerProperty(0)));
		playerListView.managedProperty().bind(playerListView.visibleProperty());

		playerListView.setBackground(Background.EMPTY);
		playerListView.setCellFactory(getPresenter().playerViewFactory(Color.web("0x0096c9ff")));

		getRoot().add(playerListView, 0, 1);
		GridPane.setMargin(playerListView, new Insets(0, 0, 0, 10));
	}

	private void displayContextMenu(ContextMenu contextMenu, MouseEvent event, Label channelName) {
		if (event.getButton() != MouseButton.SECONDARY)
			return;

		boolean canDisplay = false;
		for (MenuItem menuItem : contextMenu.getItems())
			canDisplay |= menuItem.isVisible();

		if (!canDisplay)
			return;

		contextMenu.show(channelName, Side.RIGHT, 0, 0);
	}
}
