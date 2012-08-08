/**
 * This functions checks if the user is free at a particuar time on a particular date
 * If there are clashing events they are returned to the user
 * @author Harsha Narayan
 * @author Vaarnan Drolia
 */
package action;

import java.util.ArrayList;
import core.Utility;
import parser.Parser;
import storage.DataManager;
import data.Event;

public class CheckFree extends Action implements DataMiner
{

	/**
	 * result of the checkfree action
	 */
	@Override
	public Event[] execute(String s) {

		String params = s.replace("free? ", "");
		Event checkFreeEvent = Parser.parseEvent(params);
		if (checkFreeEvent != null && isValidCommand(s)) {
			return checkFree(checkFreeEvent);
		} else {
			return null;
		}
	}

	/**
	 * checks for a valid checkfree usercommand
	 * 
	 * @param s
	 * @return
	 */
	private boolean isValidCommand(String s) {
		Event checkFreeEvent = Parser.parseEvent(s);
		if (checkFreeEvent.getStart().getTime().isDefaultTime()) {
			return false;
		}
		return true;
	}

	/**
	 * array of clashing events are returned
	 * 
	 * @param checkFreeEvent
	 * @return
	 */
	public Event[] checkFree(Event checkFreeEvent) {
		return checkFree(checkFreeEvent,
				Utility.eventsOnPrevSameDay(checkFreeEvent));
	}

	/**
	 * compares the event against events on the day to find clashes
	 * 
	 * @param checkFreeEvent
	 * @param checkFreeAgainst
	 * @return
	 */
	public Event[] checkFree(Event checkFreeEvent, Event[] checkFreeAgainst) {
		ArrayList<Event> clashingEvents = new ArrayList<Event>();
		long eventStart, eventEnd, checkFreeStart, checkFreeEnd = Long.MAX_VALUE;
		checkFreeStart = checkFreeEvent.getStart().getTimeInMilli();

		if (checkFreeEvent.getEnd() != null) {
			checkFreeEnd = checkFreeEvent.getEnd().getTimeInMilli();
		}

		if (checkFreeAgainst != null) {
			for (Event e : checkFreeAgainst) {
				if (e.getStart().getTime().isDefaultTime()) {
					continue;
				}

				if (e.getEnd() == null || e.getEnd().isDefaultTime()) {
					if (e.getStart().equals(checkFreeEvent.getStart())) {
						clashingEvents.add(e);
					}

					continue;
				}

				eventStart = e.getStart().getTimeInMilli();
				eventEnd = e.getEnd().getTimeInMilli();

				if (checkFreeStart >= eventStart && checkFreeStart <= eventEnd) {
					clashingEvents.add(e);
					continue;
				} else if (checkFreeEnd >= eventStart
						&& checkFreeEnd <= eventEnd) {
					clashingEvents.add(e);
				}
			}
		}

		Event[] clashingEventsArray = clashingEvents
				.toArray(new Event[clashingEvents.size()]);

		if (clashingEventsArray == null) {
			return null;
		}

		return clashingEventsArray;
	}

	/**
	 * 
	 * gets all events from the storage
	 * 
	 * @return
	 */
	public Event[] getAllEvents() {
		return DataManager.getAllEventsInStorage();
	}

	/**
	 * undo is irrelevant here
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * undo is irrelevant here
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "This check could not be performed";
	}

	/**
	 * @return string containing the command being executed in this case, free?
	 */
	@Override
	public String getCommandName() {
		return "free?";
	}

}
