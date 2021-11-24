package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.SelectableSoundModifierPresenter;
import fr.pederobien.mumble.client.gui.interfaces.IOkCancelView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SelectableSoundModifierView extends ViewBase<SelectableSoundModifierPresenter, BorderPane> implements IOkCancelView {
	private Stage stage;
	private Label modifierNameLabel;
	private ComboBox<String> modifierNames;
	private double marginBetweenRootAndChildren = 10.0;
	private double marginBetweenLabelAndTextField = 20.0;
	private double maxLabelWidth;

	public SelectableSoundModifierView(SelectableSoundModifierPresenter presenter) {
		super(presenter, new BorderPane());

		HBox top = new HBox();
		top.setAlignment(Pos.CENTER_LEFT);

		modifierNameLabel = new Label();
		modifierNameLabel.fontProperty().bind(getPresenter().fontProperty());
		modifierNameLabel.textProperty().bind(getPresenter().modifierNameTextProperty());
		top.getChildren().add(modifierNameLabel);

		modifierNames = new ComboBox<String>();
		modifierNames.itemsProperty().set(getPresenter().modifierNames());
		modifierNames.valueProperty().bindBidirectional(getPresenter().selectedSoundModifierNameProperty());
		top.getChildren().add(modifierNames);
		HBox.setMargin(modifierNames, new Insets(0, 0, 0, marginBetweenLabelAndTextField));
		getRoot().setTop(top);
		BorderPane.setMargin(top, new Insets(0, 0, 5, 0));

		getRoot().setCenter(getPresenter().selectedParameterListViewProperty().get().getRoot());
		getPresenter().selectedParameterListViewProperty().addListener((obs, oldValue, newValue) -> onOnSoundModifierChange(newValue));
	}

	@Override
	public void onPostShown() {
		maxWidthLabel();
		maxWidthTextField();
	}

	/**
	 * Set the stage on which this view is displayed.
	 * 
	 * @param stage The stage on which this view is displayed.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void onOnSoundModifierChange(ParameterListView newValue) {
		getRoot().setCenter(newValue.getRoot());
		if (stage != null)
			stage.sizeToScene();
		onPostShown();
	}

	private void maxWidthLabel() {
		double max = modifierNameLabel.getWidth();
		for (ParameterView parameterView : getPresenter().selectedParameterListViewProperty().get().getPresenter().getParameterViews())
			max = Math.max(max, parameterView.getParameterName().getWidth());

		maxLabelWidth = max;
		modifierNameLabel.setPrefWidth(maxLabelWidth);
		for (ParameterView parameterView : getPresenter().selectedParameterListViewProperty().get().getPresenter().getParameterViews())
			parameterView.getParameterName().setPrefWidth(maxLabelWidth);
	}

	private void maxWidthTextField() {
		modifierNames.setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - maxLabelWidth - marginBetweenLabelAndTextField);
		for (ParameterView parameterView : getPresenter().selectedParameterListViewProperty().get().getPresenter().getParameterViews())
			parameterView.getValueView().setPrefWidth(getRoot().getWidth() - 2 * marginBetweenRootAndChildren - maxLabelWidth - marginBetweenLabelAndTextField);
	}
}
