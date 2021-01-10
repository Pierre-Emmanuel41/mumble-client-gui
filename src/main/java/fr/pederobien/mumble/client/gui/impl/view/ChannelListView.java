package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.fxstyle.impl.wrapper.ListViewWrapper;
import fr.pederobien.mumble.client.gui.impl.presenter.ChannelListPresenter;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public class ChannelListView extends ViewBase<ChannelListPresenter, StackPane> {
	private boolean consumeSelectionChanged;

	public ChannelListView(ChannelListPresenter presenter) {
		super(presenter, new StackPane());
		consumeSelectionChanged = false;

		ListViewWrapper<Object> listWrapper = getStyle().createListView(getPresenter().getChannels()).background(Background.EMPTY);
		listWrapper.visibleIfNotEmpty().cellView(getPresenter().channelCellFactory(), null);
		listWrapper.onSelectedItemChanged((obs, oldValue, newValue) -> {
			if (consumeSelectionChanged) {
				consumeSelectionChanged = false;
				return;
			}
			getPresenter().onChannelSelectedChanged(oldValue, newValue);
			consumeSelectionChanged = true;
			dispatch(() -> listWrapper.get().getSelectionModel().clearSelection());
		});

		getRoot().getChildren().add(listWrapper.get());
	}
}
