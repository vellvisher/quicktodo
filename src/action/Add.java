/**
 * extends Action
 * Add class adds an event to the database
 * added event is stored in a result array
 * @author Vaarnan Drolia 
 */
package action;

import core.Utility;
import parser.Parser;

import storage.DataManager;

import data.Event;

public class Add extends Action
{

	private Event addedEvent;
	private boolean undoableCheck;

	/**
	 * @param userCommand
	 *            - command entered by the user
	 * @return Event[] an array of Event objects containing all events added
	 *         Returns null if no event is added
	 */
	public Event[] execute(String userCommand) {
		String params = userCommand;
		if (userCommand.toLowerCase().startsWith("add ")) {
			params = Utility.removeFirstWord(userCommand);
		}
		Event newEvent;
		newEvent = parseEvent(params);

		boolean isAdded = add(newEvent);
		if (isAdded) {
			addedEvent = newEvent;
			undoableCheck = true;
			Event[] resultOfAdd = new Event[1];
			resultOfAdd[0] = newEvent;
			return resultOfAdd;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param eventToBeAdded
	 * @return boolean - true if event is added to database, false otherwise
	 */
	public boolean add(Event eventToBeAdded) {
		return DataManager.add(eventToBeAdded, eventToBeAdded.getList());
	}

	/**
	 * 
	 * @param userCommand
	 * @return Event object containing the fields parsed and updated
	 */
	private Event parseEvent(String userCommand) {
		return Parser.parseEvent(userCommand);
	}

	/**
	 * @return Event[] containing the events for which the add was undone To
	 *         undo an add, events must be removed from the system
	 */
	@Override
	public Event[] undo() {
		Event[] undone = new Event[1];
		Delete deleteObj = new Delete();
		if (deleteObj.delete(addedEvent)) {
			undone[0] = addedEvent;
			return undone;
		}
		return null; // undo unsuccessful
	}

	/**
	 * @return boolean - true if the action should be added to the undo stack
	 *         Only actions that are valid adds must be added to the undo stack
	 */
	@Override
	public boolean isUndoable() {
		return undoableCheck;
	}

	/**
	 * 
	 */
	@Override
	public String errorMessage() {
		return "Item could not be added";
	}

	/**
	 * @return returns a string with the command being executed. In this case,
	 *         add
	 */
	@Override
	public String getCommandName() {
		return "add";
	}

	public static boolean isCorrectInput(String input) {
		if (!input.startsWith("add ")) {
			return false;
		}
		String eventName = Parser.parseEvent(input.replace("add ", ""))
				.getName();
		return !(eventName == null || "".equals(eventName));
	}
}
