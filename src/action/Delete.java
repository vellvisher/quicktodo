/**
 * Extends FindExec
 * Deletes an event from the database
 * deleted event is stored in a result array
 * @author Vaarnan Drolia
 */
package action;

import storage.DataManager;
import data.Event;

public class Delete extends FindExec
{
	private Event eventDeleted;
	private boolean undoableCheck = false;

	public Delete() {
		commandName = "delete";
	}

	/**
	 * @param eventToBeDeleted
	 * @return Event[] containing the event(s) deleted
	 */
	public Event[] execute(Event eventToBeDeleted) {
		Event eventToDelete = DataManager
				.getEventById(eventToBeDeleted.getId());

		boolean deleted = delete(eventToDelete);
		if (deleted) {
			undoableCheck = true;
			eventDeleted = eventToDelete;
			Event[] resultOfDelete = new Event[1];
			resultOfDelete[0] = eventToDelete;
			return resultOfDelete;
		}

		return null;

	}

	/**
	 * 
	 * @param eventToBeDeleted
	 * @return boolean - true if event is removed from database, false otherwise
	 */
	public boolean delete(Event eventToBeDeleted) {
		boolean checkIfDeleted = false;
		checkIfDeleted = DataManager.remove(eventToBeDeleted);
		return checkIfDeleted;
	}

	/**
	 * @return event[] containing undone events In this case, these are
	 *         previously deleted events added back to the database
	 */
	@Override
	public Event[] undo() {
		Event[] undoneArray = new Event[1];
		Add addObject = new Add();
		if (addObject.add(eventDeleted)) {
			undoneArray[0] = eventDeleted;
			return undoneArray;
		}
		return null;// undo was not successful
	}

	/**
	 * @return boolean - true if object should be added to undoable stack Only
	 *         legitimate delete objects should be added to the stack
	 */
	@Override
	public boolean isUndoable() {
		return undoableCheck;
	}

	@Override
	public String errorMessage() {
		return "Item could not be deleted";
	}

	/**
	 * @return string containing command currently executed In this case, delete
	 */
	@Override
	public String getCommandName() {
		return "delete";
	}
}
