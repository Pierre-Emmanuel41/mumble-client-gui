package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.dictionary.interfaces.IMessageCode;
import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import javafx.beans.property.BooleanProperty;
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

		addButton(getPresenter().joinServerCode(), getPresenter().joinServerDisableProperty(), e -> getPresenter().onJoinServerClicked());
		addButton(getPresenter().addServerCode(), getPresenter().addServerDisabledProperty(), e -> getPresenter().onAddServerClicked());
		addButton(getPresenter().editServerCode(), getPresenter().editServerDisableProperty(), e -> getPresenter().onEditServerClicked());
		addButton(getPresenter().deleteServerCode(), getPresenter().deleteServerDisableProperty(), e -> getPresenter().onDeleteServerClicked());
	}

	private void addButton(IMessageCode code, BooleanProperty disabledProperty, EventHandler<ActionEvent> handler) {
		Button button = getStyle().createButton(code);
		button.disableProperty().bind(disabledProperty);
		button.setOnAction(handler);
		add(button);
	}

	private void add(Node node) {
		getRoot().getChildren().add(node);
		FlowPane.setMargin(node, new Insets(5, 5, 5, 5));
	}
}
