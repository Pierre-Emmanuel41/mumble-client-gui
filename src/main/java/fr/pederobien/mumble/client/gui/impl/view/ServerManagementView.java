package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class ServerManagementView extends ViewBase<ServerManagementPresenter, FlowPane> {

	public ServerManagementView(ServerManagementPresenter presenter) {
		super(presenter, new FlowPane());

		getRoot().setAlignment(Pos.CENTER);

		addButton(new Button(), getPresenter().joinServerProperty(), e -> getPresenter().onJoinServerClicked(e));
		addButton(new Button(), getPresenter().addServerProperty(), e -> getPresenter().onAddServerClicked(e));
		addButton(new Button(), getPresenter().editServerProperty(), e -> getPresenter().onEditServerClicked(e));
		addButton(new Button(), getPresenter().deleteServerProperty(), e -> getPresenter().onDeleteServerClicked(e));
		addButton(new Button(), getPresenter().refreshServersProperty(), e -> getPresenter().onRefreshServersClicked(e));
	}

	private void addButton(Button button, StringProperty bindingProperty, EventHandler<ActionEvent> handler) {
		button.textProperty().bind(bindingProperty);
		button.fontProperty().bind(getPresenter().fontProperty());
		button.setOnAction(handler);
		add(button);
	}

	private void add(Node node) {
		getRoot().getChildren().add(node);
		FlowPane.setMargin(node, new Insets(5, 5, 5, 5));
	}
}
