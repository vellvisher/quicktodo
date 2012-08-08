/**
 * Edits the event and updates storage
 * @author Ankit Ganla
 */
package action;

import parser.Parser;
import storage.DataManager;

import data.Event;

public class Edit extends FindExec
{

	private static Event eventBeingEdited = null;
	private boolean undoableCheck = false;
	private Event editedEvent;
	private Event originalEvent;
	private boolean edited = false;

	public Edit() {
		commandName = "edit";
	}

	/**
	 * @param userCommand
	 * @return Event[] of events edited
	 */
	public Event[] execute(String userCommand) {
		if (eventBeingEdited == null) {
			return super.execute(userCommand);
		} else {
			return execute(Parser.parseEvent(userCommand.substring(5)));
		}
	}

	/**
	 * @param eventToBeEdited
	 * @return Event[] containing the edited event
	 */
	public Event[] execute(Event eventToBeEdited) {
		if (eventBeingEdited == null) {
			eventBeingEdited = DataManager
					.getEventById(eventToBeEdited.getId());
			return new Event[] { eventBeingEdited };
		} else {
			if (eventToBeEdited.getName() == null
					|| eventToBeEdited.getName().trim().equals("")) {
				edited = false;
			} else {
				edited = editStorage(eventBeingEdited, eventToBeEdited);
			}
			if (edited) {
				undoableCheck = true;
				originalEvent = eventBeingEdited;
				editedEvent = eventToBeEdited;
				Event[] result = new Event[1];
				result[0] = eventToBeEdited;
				eventBeingEdited = null; // reset to null
				return result;
			} else {
				eventBeingEdited = null; // reset to null
				return null; // edit was not successful
			}
		}
	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @return boolean- true if newEvent replaced oldEvent in storage
	 */
	private boolean editStorage(Event oldEvent, Event newEvent) {
		boolean oldStarred = oldEvent.getStarred();
		boolean oldTicked = oldEvent.getTicked();
		if (oldStarred) {
			newEvent.setStarred(true);
		}

		if (oldTicked) {
			newEvent.setTicked(true);
		}
		return DataManager.replace(oldEvent, newEvent);
	}

	public boolean isEdited() {
		return edited;
	}

	/**
	 * @return Event[] of undone events In this case, original events restored
	 */
	@Override
	public Event[] undo() {
		Event[] undone = new Event[1];
		if (editStorage(editedEvent, originalEvent)) {
			undone[0] = originalEvent;
			return undone;
		}
		return null; // undo unsuccessful
	}

	/**
	 * @return boolean - true if action is undoable only legitimate edit objects
	 *         should be added to the undo stack
	 */
	@Override
	public boolean isUndoable() {
		return undoableCheck;
	}

	@Override
	public String errorMessage() {
		return "Item could not be edited";
	}

	/**
	 * @return string containing the command being executed in this case, edit
	 */
	@Override
	public String getCommandName() {
		return commandName;
	}

}
