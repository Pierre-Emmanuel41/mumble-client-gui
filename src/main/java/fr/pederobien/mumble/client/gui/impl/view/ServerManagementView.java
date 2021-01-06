package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import javafx.beans.property.BooleanProperty;
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

		addButton(getPresenter().joinServerLanguageProperty(), getPresenter().joinServerDisableProperty(), e -> getPresenter().onJoinServerClicked(e));
		addButton(getPresenter().addServerLanguageProperty(), getPresenter().addServerDisabledProperty(), e -> getPresenter().onAddServerClicked(e));
		addButton(getPresenter().editServerLanguageProperty(), getPresenter().editServerDisableProperty(), e -> getPresenter().onEditServerClicked(e));
		addButton(getPresenter().deleteServerLanguageProperty(), getPresenter().deleteServerDisableProperty(), e -> getPresenter().onDeleteServerClicked(e));
		addButton(getPresenter().refreshServersLanguageProperty(), getPresenter().refreshServersDisableProperty(), e -> getPresenter().onRefreshServersClicked(e));
	}

	private void addButton(StringProperty languageProperty, BooleanProperty disabledProperty, EventHandler<ActionEvent> handler) {
		Button button = new Button();
		button.textProperty().bind(languageProperty);
		button.disableProperty().bind(disabledProperty);
		button.fontProperty().bind(getPresenter().fontProperty());
		button.setOnAction(handler);
		add(button);
	}

	private void add(Node node) {
		getRoot().getChildren().add(node);
		FlowPane.setMargin(node, new Insets(5, 5, 5, 5));
	}
}
