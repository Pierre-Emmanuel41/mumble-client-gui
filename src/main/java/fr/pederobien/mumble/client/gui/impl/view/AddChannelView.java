package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.AddChannelPresenter;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AddChannelView extends ViewBase<AddChannelPresenter, FlowPane> implements IOkCancelView {
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;

	private Label channelNameLabel;
	private TextField channelNameTextField;

	public AddChannelView(Stage initOwner, AddChannelPresenter presenter) {
		super(presenter, new FlowPane());
		getRoot().setPrefWidth(490);

		channelNameLabel = new Label();
		channelNameLabel.fontProperty().bind(getPresenter().fontProperty());
		channelNameLabel.textProperty().bind(getPresenter().channelNameTextProperty());
		getRoot().getChildren().add(channelNameLabel);

		channelNameTextField = new TextField();
		channelNameTextField.fontProperty().bind(getPresenter().fontProperty());
		channelNameTextField.promptTextProperty().bind(getPresenter().channelNamePromptProperty());
		channelNameTextField.textProperty().bindBidirectional(getPresenter().channelNameProperty());
		channelNameTextField.borderProperty().bind(getPresenter().channelNameBorderProperty());
		channelNameTextField.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateChannelName());
		channelNameTextField.tooltipProperty().bind(getPresenter().channelNameTooltipProperty());

		getRoot().getChildren().add(channelNameTextField);
		FlowPane.setMargin(channelNameTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		new OkCancelStage(initOwner, this);
	}

	@Override
	public void onPostShown() {
		channelNameTextField.setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - channelNameLabel.getWidth() - marginBetweenLabelAndTextField);
	}
}
