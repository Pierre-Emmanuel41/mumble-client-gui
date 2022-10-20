package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.AddChannelPresenter;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AddChannelView extends ViewBase<AddChannelPresenter, BorderPane> implements IOkCancelView {
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;

	private Label channelNameLabel;
	private TextField channelNameTextField;

	public AddChannelView(AddChannelPresenter presenter) {
		super(presenter, new BorderPane());

		HBox top = new HBox();
		top.setAlignment(Pos.CENTER_LEFT);
		channelNameLabel = new Label();
		channelNameLabel.fontProperty().bind(getPresenter().fontProperty());
		channelNameLabel.textProperty().bind(getPresenter().channelNameTextProperty());
		top.getChildren().add(channelNameLabel);

		channelNameTextField = new TextField();
		channelNameTextField.fontProperty().bind(getPresenter().fontProperty());
		channelNameTextField.promptTextProperty().bind(getPresenter().channelNamePromptProperty());
		channelNameTextField.textProperty().bindBidirectional(getPresenter().channelNameProperty());
		channelNameTextField.borderProperty().bind(getPresenter().channelNameBorderProperty());
		channelNameTextField.textProperty().addListener((obs, oldValue, newValue) -> getPresenter().validateChannelName());
		channelNameTextField.tooltipProperty().bind(getPresenter().channelNameTooltipProperty());

		top.getChildren().add(channelNameTextField);
		HBox.setMargin(channelNameTextField, new Insets(0, 0, 0, marginBetweenLabelAndTextField));
		getRoot().setTop(top);
		BorderPane.setMargin(top, new Insets(0, 0, 5, 0));

		getRoot().setCenter(getPresenter().selectableSoundModifierView().getRoot());
		getPresenter().selectableSoundModifierView().setStage(new OkCancelStage(getPrimaryStage(), this).getStage());
	}

	@Override
	public void onPostShown() {
		channelNameTextField.setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - channelNameLabel.getWidth() - marginBetweenLabelAndTextField);
		getPresenter().selectableSoundModifierView().computeWidth();
	}
}
