package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.javafx.configuration.impl.components.SimpleButton;
import fr.pederobien.mumble.client.gui.impl.EGuiCode;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class ServerManagementView extends ViewBase<ServerManagementPresenter, FlowPane> {

	/**
	 * Creates a view in order to join/add/remove/modify a server.
	 * 
	 * @param presenter The presenter associated to this node.
	 */
	public ServerManagementView(ServerManagementPresenter presenter) {
		super(presenter, new FlowPane());

		getRoot().setAlignment(Pos.CENTER);

		SimpleButton joinButton = new SimpleButton(EGuiCode.JOIN_SERVER);
		joinButton.disableProperty().bind(getPresenter().joinDisableProperty());
		joinButton.setOnAction(e -> getPresenter().onJoin());
		add(joinButton);

		SimpleButton addButton = new SimpleButton(EGuiCode.ADD_SERVER);
		addButton.disableProperty().bind(getPresenter().addDisableProperty());
		addButton.setOnAction(e -> getPresenter().onAdd());
		add(addButton);

		SimpleButton editButton = new SimpleButton(EGuiCode.EDIT_SERVER);
		editButton.disableProperty().bind(getPresenter().editDisableProperty());
		editButton.setOnAction(e -> getPresenter().onEdit());
		add(editButton);

		SimpleButton deleteButton = new SimpleButton(EGuiCode.DELETE_SERVER);
		deleteButton.disableProperty().bind(getPresenter().deleteDisableProperty());
		deleteButton.setOnAction(e -> getPresenter().onDelete());
		add(deleteButton);
	}

	private void add(Node node) {
		getRoot().getChildren().add(node);
		FlowPane.setMargin(node, new Insets(5, 5, 5, 5));
	}
}
