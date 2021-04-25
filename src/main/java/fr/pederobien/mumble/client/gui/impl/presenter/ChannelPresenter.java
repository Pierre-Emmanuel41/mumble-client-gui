package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.impl.view.PlayerChannelView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsChannelPresenter;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.IOtherPlayer;
import fr.pederobien.mumble.client.interfaces.observers.IObsChannel;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ChannelPresenter extends PresenterBase implements IObsChannel, IObservable<IObsChannelPresenter> {
	private PlayerPresenter playerPresenter;
	private IChannel channel;
	private Observable<IObsChannelPresenter> observers;

	private ObservableList<Object> players;
	private StringProperty channelNameProperty;

	public ChannelPresenter(PlayerPresenter playerPresenter, IChannel channel) {
		this.playerPresenter = playerPresenter;
		this.channel = channel;
		this.channel.addObserver(this);

		observers = new Observable<IObsChannelPresenter>();
		players = FXCollections.observableArrayList(channel.getPlayers());
		channelNameProperty = new SimpleStringProperty(channel.getName());
	}

	@Override
	public void onChannelRename(IChannel channel, String oldName, String newName) {
		dispatch(() -> channelNameProperty.setValue(newName));
	}

	@Override
	public void onPlayerAdded(IChannel channel, IOtherPlayer player) {
		dispatch(() -> players.add(player));
	}

	@Override
	public void onPlayerRemoved(IChannel channel, IOtherPlayer player) {
		dispatch(() -> players.remove(player));
	}

	@Override
	public void addObserver(IObsChannelPresenter obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsChannelPresenter obs) {
		observers.removeObserver(obs);
	}

	/**
	 * @return An observable list that contains all registered players.
	 */
	public ObservableList<Object> getPlayers() {
		return players;
	}

	/**
	 * @return The property that display the channel name.
	 */
	public StringProperty channelNameProperty() {
		return channelNameProperty;
	}

	public <T> Callback<ListView<T>, ListCell<T>> playerViewFactory(Color enteredColor) {
		return listView -> getPropertyHelper().cellView(item -> new PlayerChannelView(new PlayerChannelPresenter(playerPresenter, (IOtherPlayer) item)).getRoot(),
				enteredColor);
	}

	public void onChannelClicked() {
		observers.notifyObservers(obs -> obs.onChannelClicked(channel));
	}
}
