/**
 * extends Action
 * Get all overdue events for the user 
 * Useful for reminders
 * @author Harsha Narayan
 */
package action;

import java.util.ArrayList;
import storage.DataManager;
import data.Event;
import data.EventDateTime;

public class Overdue extends Action implements DataMiner
{

	EventDateTime eventDateTime = new EventDateTime();

	/**
	 * @param userCommand
	 * @return Event[] containing all overdue events
	 */
	public Event[] execute(String userCommand) {

		if (userCommand.startsWith("overdue")) {
			Event[] allOverdueEvents = overdue(getAllEvents());
			return allOverdueEvents;
		}

		return null;
	}

	/**
	 * 
	 * @param eventToCheck
	 * @return boolean - true if event is overdue, false otherwise
	 */
	public boolean checkIfOverdue(Event eventToCheck) {
		if (!eventToCheck.getStart().isDefaultTime()
				&& eventToCheck.getStart().compareTo(EventDateTime.getCurrentTime()) < 0
				&& !eventToCheck.getTicked()) {
			return true;
		} else { 
			return false;
		}
	}

	/**
	 * 
	 * @param Event
	 *            [] checkForOverdue
	 * @return Event[] of all overdue events from the array entered
	 */
	public Event[] overdue(Event[] checkForOverdue) {

		ArrayList<Event> overdueEventsList = new ArrayList<Event>();
		if (checkForOverdue.length == 0) {
			return null;
		}
		for (int i = 0; i < checkForOverdue.length; i++) {
			if (checkIfOverdue(checkForOverdue[i])) {
				overdueEventsList.add(checkForOverdue[i]);
			}
		}
		if (overdueEventsList.size() == 0) {
			return null;
		}
		return (Event[]) overdueEventsList.toArray(new Event[overdueEventsList.size()]);

	}

	/**
	 * 
	 * @return Event[] of all events in the database
	 */
	public static Event[] getAllEvents() {
		return DataManager.getAllEventsInStorage();
	}

	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * 
	 * this object is never pushed onto the undo stack
	 * 
	 * @return always false
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "You have no overdue items :-)";
	}

	/**
	 * @return string containing the command being executed in this case,
	 *         overdue
	 */
	@Override
	public String getCommandName() {
		return "overdue";
	}
}
