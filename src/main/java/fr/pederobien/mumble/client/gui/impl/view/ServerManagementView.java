package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.impl.presenter.ServerManagementPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class ServerManagementView extends ViewBase<ServerManagementPresenter, FlowPane> {

	public ServerManagementView(ServerManagementPresenter presenter) {
		super(presenter, new FlowPane());

		getRoot().setAlignment(Pos.CENTER);

		add(getStyle().createButton(getPresenter().joinCode()).disable(getPresenter().joinDisableProperty()).onAction(e -> getPresenter().onJoin()).get());
		add(getStyle().createButton(getPresenter().addCode()).disable(getPresenter().addDisableProperty()).onAction(e -> getPresenter().onAdd()).get());
		add(getStyle().createButton(getPresenter().editCode()).disable(getPresenter().editDisableProperty()).onAction(e -> getPresenter().onEdit()).get());
		add(getStyle().createButton(getPresenter().deleteCode()).disable(getPresenter().deleteDisableProperty()).onAction(e -> getPresenter().onDelete()).get());

	}

	private void add(Node node) {
		getRoot().getChildren().add(node);
		FlowPane.setMargin(node, new Insets(5, 5, 5, 5));
	}
}
