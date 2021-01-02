package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.dictionary.EMessageCode;
import fr.pederobien.mumble.client.gui.properties.SimpleFontProperty;
import fr.pederobien.mumble.client.gui.properties.SimpleLanguageProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ServerManagementPresenter extends PresenterBase {
	private SimpleLanguageProperty joinServerLanguageProperty, addServerLanguageProperty, editServerLanguageProperty, deleteServerLanguageProperty,
			refreshServerLanguageProperty;

	private SimpleFontProperty fontProperty;

	public ServerManagementPresenter(Stage primaryStage) {
		super(primaryStage);

		joinServerLanguageProperty = createLanguageProperty(EMessageCode.JOIN_SERVER);
		addServerLanguageProperty = createLanguageProperty(EMessageCode.ADD_SERVER);
		editServerLanguageProperty = createLanguageProperty(EMessageCode.EDIT_SERVER);
		deleteServerLanguageProperty = createLanguageProperty(EMessageCode.DELETE_SERVER);
		refreshServerLanguageProperty = createLanguageProperty(EMessageCode.REFRESH_SERVERS);

		fontProperty = createFontProperty();
	}

	/**
	 * @return The message to display in order to join the server.
	 */
	public StringProperty joinServerProperty() {
		return joinServerLanguageProperty;
	}

	/**
	 * @return The message to display in order to add a server.
	 */
	public StringProperty addServerProperty() {
		return addServerLanguageProperty;
	}

	/**
	 * @return The message to display in order to edit the server.
	 */
	public StringProperty editServerProperty() {
		return editServerLanguageProperty;
	}

	/**
	 * @return The message to display in order to delete the server.
	 */
	public StringProperty deleteServerProperty() {
		return deleteServerLanguageProperty;
	}

	/**
	 * @return The message to display in order to refresh server status.
	 */
	public StringProperty refreshServersProperty() {
		return refreshServerLanguageProperty;
	}

	/**
	 * @return The font to display messages.
	 */
	public ObjectProperty<Font> fontProperty() {
		return fontProperty;
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
