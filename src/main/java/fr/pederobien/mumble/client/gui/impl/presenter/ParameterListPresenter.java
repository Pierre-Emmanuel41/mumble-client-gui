package fr.pederobien.mumble.client.gui.impl.presenter;

import fr.pederobien.mumble.client.gui.impl.view.ParameterView;
import fr.pederobien.mumble.client.player.interfaces.IParameter;
import fr.pederobien.mumble.client.player.interfaces.IParameterList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParameterListPresenter extends PresenterBase {
	private IParameterList parameterList;
	private ObservableList<ParameterView> parameterViews;

	/**
	 * Creates a presenter in order to display each parameter contained in the given list.
	 * 
	 * @param parameterList The list that contains parameter to display.
	 */
	public ParameterListPresenter(IParameterList parameterList) {
		this.parameterList = parameterList;

		parameterViews = FXCollections.observableArrayList();
		for (IParameter<?> parameter : parameterList)
			parameterViews.add(new ParameterView(new ParameterPresenter(parameter)));
	}

	/**
	 * Update the current value of each parameter registered in the underlying parameters list. If a parameter is associated to a
	 * range, then the minimum/maximum value of the range is also updated.
	 * 
	 * @return True if all parameters have been successfully updated, false otherwise.
	 */
	public boolean apply() {
		if (parameterList.size() == 0)
			return true;

		// Result initialized to true if there are no parameter
		boolean result = true;
		for (ParameterView view : parameterViews)
			result &= view.getPresenter().apply();

		return result;
	}

	/**
	 * Unregister the presenter of each parameter for event handling.
	 */
	public void onClosing() {
		getParameterViews().forEach(view -> view.getPresenter().onClosing());
	}

	/**
	 * @return The list that contains the view associated to each parameter registered in the underlying parameter list.
	 */
	public ObservableList<ParameterView> getParameterViews() {
		return parameterViews;
	}

	/**
	 * @return True if each value of parameters is valid, false otherwise.
	 */
	public boolean isValid() {
		boolean isValid = true;
		for (ParameterView view : parameterViews)
			isValid &= view.getPresenter().isValid();

		return isValid;
	}

	/**
	 * @return True if the new value (resp. minimum and maximum) of a parameter is identical to its current value (resp. minimim and
	 *         maximum).
	 */
	public boolean isIdentical() {
		boolean isIdentical = true;
		for (ParameterView view : parameterViews)
			isIdentical &= view.getPresenter().isIdentical();

		return isIdentical;
	}
}
