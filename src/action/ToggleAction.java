/**
 * This class implements the toggle action
 * for toggle type star- starred events will be unstarred and vice versa
 * for toggle type tick- ticked events will be unticked and vice versa
 * @author Vaarnan Drolia
 */
package action;

import storage.DataManager;
import data.Event;

public class ToggleAction extends Action
{
	enum TOGGLE_TYPE {
		TICK, STAR
	};

	private Event toggledEvent;
	private TOGGLE_TYPE type;

	/**
	 * sets the type of toggle action, star or tick
	 * 
	 * @param type
	 */
	public ToggleAction(TOGGLE_TYPE type) {
		this.type = type;
	}

	/**
	 * @param eventToBeToggled
	 * @return event array with toggled event(s)
	 */
	public Event[] execute(Event eventToBeToggled) {
		toggledEvent = eventToBeToggled;
		boolean toggleSuccessful = toggle(DataManager
				.getEventById(eventToBeToggled.getId()));

		if (toggleSuccessful) {
			Event[] result = new Event[1];
			result[0] = eventToBeToggled;
			return result;
		}

		return null;
	}

	/**
	 * 
	 * @param eventToToggle
	 * @return boolean- true if the toggle was successful, false otherwise
	 */
	public boolean toggle(Event eventToToggle) {
		if (type.equals(TOGGLE_TYPE.STAR)) {
			eventToToggle.toggleStarred();
		} else if (type.equals(TOGGLE_TYPE.TICK)) {
			eventToToggle.toggleTicked();
		}
		return DataManager.replace(eventToToggle, eventToToggle);
	}

	/**
	 * toggles an event again, reversing the previous toggle
	 * 
	 * @return event array with toggled result
	 */
	@Override
	public Event[] undo() {
		return execute(toggledEvent);
	}

	/**
	 * @return boolean- always returns true because each toggle is Undoable
	 */
	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String errorMessage() {
		return String.format("Item could not be $1%s/un$1%s", type.toString()
				.toLowerCase());
	}

	/**
	 * this class is built only to toggle the tick or star the execute function
	 * simply returns a null array The Toggle class, which calls this call,
	 * returns to the calling function
	 */
	@Override
	public Event[] execute(String userCommand) {
		return null;
	}

	/**
	 * @return a string which identifies the toggle action being executed - tick
	 *         or star
	 */
	@Override
	public String getCommandName() {
		return type.name().toLowerCase();
	}
}
