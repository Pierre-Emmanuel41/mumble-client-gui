package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ServerManagementPresenter extends PresenterBase {
	private SimpleLanguageProperty joinServerProperty, addServerProperty, editServerProperty, deleteServerProperty, refreshServerProperty;

	public ServerManagementPresenter(Stage primaryStage) {
		super(primaryStage);

		joinServerProperty = createLanguageProperty(EMessageCode.JOIN_SERVER);
		addServerProperty = createLanguageProperty(EMessageCode.ADD_SERVER);
		editServerProperty = createLanguageProperty(EMessageCode.EDIT_SERVER);
		deleteServerProperty = createLanguageProperty(EMessageCode.DELETE_SERVER);
		refreshServerProperty = createLanguageProperty(EMessageCode.REFRESH_SERVERS);
	}

	/**
	 * @return The message to display in order to join the server.
	 */
	public StringProperty joinServerProperty() {
		return joinServerProperty;
	}

	/**
	 * @return The message to display in order to add a server.
	 */
	public StringProperty addServerProperty() {
		return addServerProperty;
	}

	/**
	 * @return The message to display in order to edit the server.
	 */
	public StringProperty editServerProperty() {
		return editServerProperty;
	}

	/**
	 * @return The message to display in order to delete the server.
	 */
	public StringProperty deleteServerProperty() {
		return deleteServerProperty;
	}

	/**
	 * @return The message to display in order to refresh server status.
	 */
	public StringProperty refreshServersProperty() {
		return refreshServerProperty;
	}

	public void onJoinServerClicked(ActionEvent event) {
		System.out.println("Joining server");
	}

	public void onAddServerClicked(ActionEvent event) {
		System.out.println("Adding new server");
	}

	public void onEditServerClicked(ActionEvent event) {
		System.out.println("Editing server");
	}

	public void onDeleteServerClicked(ActionEvent event) {
		System.out.println("Deleting server");
	}

	public void onRefreshServersClicked(ActionEvent event) {
		System.out.println("Refreshing servers");
	}
}
