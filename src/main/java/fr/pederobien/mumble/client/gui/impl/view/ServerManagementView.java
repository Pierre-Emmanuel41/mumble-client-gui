package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

		Button joinButton = new Button();
		joinButton.fontProperty().bind(getPresenter().fontProperty());
		joinButton.textProperty().bind(getPresenter().joinServerTextProperty());
		joinButton.disableProperty().bind(getPresenter().joinDisableProperty());
		joinButton.setOnAction(e -> getPresenter().onJoin());
		add(joinButton);

		Button addButton = new Button();
		addButton.fontProperty().bind(getPresenter().fontProperty());
		addButton.textProperty().bind(getPresenter().addServerTextProperty());
		addButton.disableProperty().bind(getPresenter().addDisableProperty());
		addButton.setOnAction(e -> getPresenter().onAdd());
		add(addButton);

		Button editButton = new Button();
		editButton.fontProperty().bind(getPresenter().fontProperty());
		editButton.textProperty().bind(getPresenter().editServerTextProperty());
		editButton.disableProperty().bind(getPresenter().editDisableProperty());
		editButton.setOnAction(e -> getPresenter().onEdit());
		add(editButton);

		Button deleteButton = new Button();
		deleteButton.fontProperty().bind(getPresenter().fontProperty());
		deleteButton.textProperty().bind(getPresenter().deleteServerTextProperty());
		deleteButton.disableProperty().bind(getPresenter().deleteDisableProperty());
		deleteButton.setOnAction(e -> getPresenter().onDelete());
		add(deleteButton);
	}

	private void add(Node node) {
		getRoot().getChildren().add(node);
		FlowPane.setMargin(node, new Insets(5, 5, 5, 5));
	}
}
