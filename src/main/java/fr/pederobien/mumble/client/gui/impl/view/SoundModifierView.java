package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.SoundModifierPresenter;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class SoundModifierView extends ViewBase<SoundModifierPresenter, FlowPane> implements IOkCancelView {
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;

	private Label modifierNameLabel;
	private ComboBox<String> modifierNames;

	public SoundModifierView(Stage initOwner, SoundModifierPresenter presenter) {
		super(presenter, new FlowPane());

		modifierNameLabel = new Label();
		modifierNameLabel.fontProperty().bind(getPresenter().fontProperty());
		modifierNameLabel.textProperty().bind(getPresenter().modifierNameTextProperty());
		getRoot().getChildren().add(modifierNameLabel);

		modifierNames = new ComboBox<String>();
		// modifierNames.fontProperty().bind(getPresenter().fontProperty());
		modifierNames.itemsProperty().set(getPresenter().modifierNames());
		modifierNames.valueProperty().addListener((obs, oldValue, newValue) -> getPresenter().onSoundModifierChanged(newValue));
		modifierNames.getSelectionModel().select(getPresenter().getCurrentSoundModifierName());

		getRoot().getChildren().add(modifierNames);
		FlowPane.setMargin(modifierNames, new Insets(0, 0, 0, marginBetweenLabelAndTextField));

		new OkCancelStage(initOwner, this);
	}

	@Override
	public void onPostShown() {
		modifierNames.setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - modifierNameLabel.getWidth() - marginBetweenLabelAndTextField);
	}
}
