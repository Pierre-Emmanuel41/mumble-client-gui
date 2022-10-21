package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.RenameChannelPresenter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class RenameChannelView extends ViewBase<RenameChannelPresenter, FlowPane> {

	public RenameChannelView(RenameChannelPresenter presenter) {
		super(presenter, new FlowPane());

		Label channelNameLabel = new Label();
		channelNameLabel.fontProperty().bind(getPresenter().fontProperty());
		channelNameLabel.textProperty().bind(getPresenter().channelNameTextProperty());
		getRoot().getChildren().add(channelNameLabel);

		TextField channelNameTextField = new TextField();
		channelNameTextField.fontProperty().bind(getPresenter().fontProperty());
		channelNameTextField.textProperty().bindBidirectional(getPresenter().channelNameProperty());
		channelNameTextField.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateChannelName());
		channelNameTextField.borderProperty().bind(getPresenter().channelNameBorderProperty());
		channelNameTextField.tooltipProperty().bind(getPresenter().channelNameTooltipProperty());

		getPresenter().getFormView().addRow(channelNameLabel, channelNameTextField);

		OkCancelStage okCancelStage = new OkCancelStage(getPrimaryStage(), getPresenter());
		okCancelStage.show();
	}
}
