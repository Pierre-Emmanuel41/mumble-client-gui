package fr.pederobien.mumble.client.gui.impl.presenter;

import java.util.Map;

import fr.pederobien.mumble.client.gui.event.ParameterValueChangeRequestEvent;
import fr.pederobien.mumble.client.gui.impl.generic.OkCancelPresenter;
import fr.pederobien.mumble.client.gui.impl.view.ParameterView;
import fr.pederobien.mumble.client.interfaces.IParameter;
import fr.pederobien.mumble.client.interfaces.IParameterList;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;
import fr.pederobien.utils.event.LogEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParameterListPresenter extends OkCancelPresenter implements IEventListener {
	private IParameterList parameterList;
	private BooleanProperty okDisableProperty;
	private ObservableList<ParameterView> parameterViews;

	private boolean isNotValid, isIdentical;

	public ParameterListPresenter(IParameterList parameterList) {
		this.parameterList = parameterList;

		parameterViews = FXCollections.observableArrayList();
		for (Map.Entry<String, IParameter<?>> entry : parameterList)
			parameterViews.add(new ParameterView(new ParameterPresenter(entry.getValue())));

		// OK button not disabled if there are no parameter
		okDisableProperty = new SimpleBooleanProperty(parameterList.size() != 0);
		EventManager.registerListener(this);
	}

	@Override
	public StringProperty titleTextProperty() {
		return null;
	}

	@Override
	public boolean onOkButtonClicked() {
		if (okDisableProperty.get())
			return false;

		// Result initialized to true if there are no parameter
		boolean result = parameterList.size() == 0;
		for (ParameterView view : parameterViews)
			result |= view.getPresenter().onOkButtonClicked();

		return result;
	}

	@Override
	public void onClosing() {
		getParameterViews().forEach(view -> view.getPresenter().onClosing());
		EventManager.unregisterListener(this);
	}

	@Override
	public BooleanProperty okDisableProperty() {
		return okDisableProperty;
	}

	/**
	 * @return The list that contains the view associated to each parameter registered in the underlying parameter list.
	 */
	public ObservableList<ParameterView> getParameterViews() {
		return parameterViews;
	}

	/**
	 * @param recheck True if an iteration over the registered parameter should be done, false otherwise.
	 * 
	 * @return True at least one parameter has not a valid value. Please see {@link ParameterPresenter#isNotValid()} for more details.
	 */
	public boolean isNotValid(boolean recheck) {
		if (!recheck)
			return isNotValid;

		isNotValid = false;
		for (ParameterView view : parameterViews)
			isNotValid |= view.getPresenter().isNotValid();
		return isNotValid;
	}

	/**
	 * @param recheck True if an iteration over the registered parameter should be done, false otherwise.
	 * 
	 * @return True at least one parameter value is different. Please see {@link ParameterPresenter#isIdentical()} for more details.
	 */
	public boolean isIdentical(boolean recheck) {
		if (!recheck)
			return isIdentical;

		isIdentical = true;
		for (ParameterView view : parameterViews)
			isIdentical &= view.getPresenter().isIdentical();
		return isIdentical;
	}

	/**
	 * Check value of {@link ParameterPresenter#isValid()} and {@link ParameterPresenter#isIdentique()} of each registered parameter
	 * presenter in order to update the {@link #okDisableProperty} property.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	private void onParameterValueChange(ParameterValueChangeRequestEvent event) {
		if (!parameterList.getParameters().containsValue(event.getParameter()))
			return;

		isNotValid = false;
		isIdentical = true;
		for (ParameterView view : parameterViews) {
			isNotValid |= view.getPresenter().isNotValid();
			isIdentical &= view.getPresenter().isIdentical();
		}

		okDisableProperty.set(isNotValid || isIdentical);
		String clazz = "ParameterListPresenter";
		String formatter = "[%s] is not valid %s, is identical %s, okDisable %s";
		EventManager.callEvent(new LogEvent(formatter, clazz, isNotValid, isIdentical, okDisableProperty.get()));
	}
}
