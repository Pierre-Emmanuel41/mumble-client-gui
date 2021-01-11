package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.function.Function;

import fr.pederobien.mumble.client.gui.impl.view.PlayerChannelView;
import fr.pederobien.mumble.client.gui.interfaces.observers.presenter.IObsChannelPresenter;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.observers.IObsChannel;
import fr.pederobien.utils.IObservable;
import fr.pederobien.utils.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

public class ChannelPresenter extends PresenterBase implements IObsChannel, IObservable<IObsChannelPresenter> {
	private IChannel channel;
	private Observable<IObsChannelPresenter> observers;

	private ObservableList<Object> players;
	private StringProperty channelNameProperty;

	public ChannelPresenter(IChannel channel) {
		this.channel = channel;
		this.channel.addObserver(this);

		observers = new Observable<IObsChannelPresenter>();
		players = FXCollections.observableArrayList(channel.getPlayers());
		channelNameProperty = new SimpleStringProperty(channel.getName());
	}

	@Override
	public void onChannelRename(IChannel channel, String oldName, String newName) {
		channelNameProperty.setValue(newName);
	}

	@Override
	public void onPlayerAdded(IChannel channel, String player) {
		dispatch(() -> players.add(player));
	}

	@Override
	public void onPlayerRemoved(IChannel channel, String player) {
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

	/**
	 * @return The creator of player view.
	 */
	public Function<Object, Parent> playerViewConstructor() {
		return item -> new PlayerChannelView(PlayerChannelPresenter.getOrCreatePlayerPresenter((String) item)).getRoot();
	}

	public void onChannelClicked() {
		observers.notifyObservers(obs -> obs.onChannelClicked(channel));
	}
}
