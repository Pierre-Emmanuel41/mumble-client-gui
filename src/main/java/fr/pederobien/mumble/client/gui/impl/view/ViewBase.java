package fr.pederobien.mumble.client.gui.impl.view;

import fr.pederobien.mumble.client.gui.MumbleClientApplication;
import fr.pederobien.mumble.client.gui.impl.presenter.PresenterBase;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class ViewBase<T extends PresenterBase, U extends Parent> {
	private T presenter;
	private U root;

	protected ViewBase(T presenter, U root) {
		this.presenter = presenter;
		this.root = root;
	}

	/**
	 * @return The root that contains all graphical components.
	 */
	public U getRoot() {
		return root;
	}

	/**
	 * @return The presenter associated to this view.
	 */
	public T getPresenter() {
		return presenter;
	}

	/**
	 * Run the specified Runnable on the JavaFX Application Thread at some unspecified time in the future. This method, which may be
	 * called from any thread, will post the Runnable to an event queue and then return immediately to the caller. The Runnables are
	 * executed in the order they are posted. A runnable passed into the runLater method will be executed before any Runnable passed
	 * into a subsequent call to runLater. If this method is called after the JavaFX runtime has been shutdown, the call will be
	 * ignored: the Runnable will not be executed and no exception will be thrown.
	 *
	 * <p>
	 * NOTE: applications should avoid flooding JavaFX with too many pending Runnables. Otherwise, the application may become
	 * unresponsive. Applications are encouraged to batch up multiple operations into fewer runLater calls. Additionally, long-running
	 * operations should be done on a background thread where possible, freeing up the JavaFX Application Thread for GUI operations.
	 * </p>
	 *
	 * <p>
	 * This method must not be called before the FX runtime has been initialized. For standard JavaFX applications that extend
	 * {@see Application}, and use either the Java launcher or one of the launch methods in the Application class to launch the
	 * application, the FX runtime is initialized by the launcher before the Application class is loaded. For Swing applications that
	 * use JFXPanel to display FX content, the FX runtime is initialized when the first JFXPanel instance is constructed. For SWT
	 * application that use FXCanvas to display FX content, the FX runtime is initialized when the first FXCanvas instance is
	 * constructed.
	 * </p>
	 *
	 * @param runnable the Runnable whose run method will be executed on the JavaFX Application Thread
	 *
	 * @throws IllegalStateException if the FX runtime has not been initialized
	 */
	protected void dispatch(Runnable runnable) {
		Platform.runLater(runnable);
	}

	/**
	 * @return The primary stage of the application.
	 */
	protected Stage getPrimaryStage() {
		return MumbleClientApplication.getStage();
	}
}
