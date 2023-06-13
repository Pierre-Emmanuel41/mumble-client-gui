package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.javafx.configuration.impl.components.SimpleLabel;
import fr.pederobien.javafx.configuration.impl.components.SimpleTextField;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelStage;
import fr.pederobien.mumble.client.gui.impl.presenter.RenameChannelPresenter;
import javafx.scene.layout.FlowPane;

public class RenameChannelView extends ViewBase<RenameChannelPresenter, FlowPane> {

	/**
	 * Creates a view in order to rename a channel.
	 * 
	 * @param presenter The presenter associated to this view.
	 */
	public RenameChannelView(RenameChannelPresenter presenter) {
		super(presenter, new FlowPane());

		SimpleTextField channelNameTextField = new SimpleTextField(getPresenter().channelNameProperty());
		channelNameTextField.setTooltip(EGuiCode.CHANNEL_NAME_TOOLTIP);

		getPresenter().getFormView().addRow(new SimpleLabel(EGuiCode.RENAME_CHANNEL_NAME), channelNameTextField);

		OkCancelStage okCancelStage = new OkCancelStage(getPrimaryStage(), getPresenter());
		okCancelStage.show();
	}
}
