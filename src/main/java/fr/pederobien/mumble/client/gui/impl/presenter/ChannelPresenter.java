package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import fr.pederobien.mumble.client.gui.impl.view.PlayerChannelView;
import fr.pederobien.mumble.client.interfaces.IChannel;
import fr.pederobien.mumble.client.interfaces.observers.IObsChannel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

public class ChannelPresenter extends PresenterBase implements IObsChannel {
	private static final Map<IChannel, ChannelPresenter> PRESENTERS = new HashMap<IChannel, ChannelPresenter>();
	private IChannel channel;

	private ObservableList<Object> players;
	private StringProperty channelNameProperty;

	public static ChannelPresenter getOrCreateServerPresenter(IChannel channel) {
		ChannelPresenter presenter = PRESENTERS.get(channel);
		if (presenter != null)
			return presenter;

		presenter = new ChannelPresenter(channel);
		PRESENTERS.put(channel, presenter);
		return presenter;
	}

	private ChannelPresenter(IChannel channel) {
		this.channel = channel;
		this.channel.addObserver(this);

		players = FXCollections.observableArrayList(channel.getPlayers());
		channelNameProperty = new SimpleStringProperty(channel.getName());
	}

	@Override
	public void onChannelRename(IChannel channel, String oldName, String newName) {
		channelNameProperty.setValue(newName);
	}

	@Override
	public void onPlayerAdded(IChannel channel, String player) {
		players.add(player);
	}

	@Override
	public void onPlayerRemoved(IChannel channel, String player) {
		players.remove(player);
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
}
