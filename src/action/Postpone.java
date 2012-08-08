/**
 * extends findexec
 * Allows postponing of events - changing the time to a later time
 * Works on a similar principle as edit, only just time is changed
 * @author Ankit Ganla
 */
package action;

import java.util.Calendar;

import parser.Parser;
import storage.DataManager;
import data.Event;
import data.EventDateTime;

public class Postpone extends FindExec
{

	private boolean undoableCheck = false;
	private Event originalEvent;
	private static Event eventBeingPostponed = null;

	private EventDateTime oldStartTime;

	public Postpone() {
		commandName = "postpone";
	}

	/**
	 * @param userCommand
	 * @return Event[] of events that were postponed
	 */
	public Event[] execute(String userCommand) {
		if (eventBeingPostponed == null) {
			return super.execute(userCommand);
		} else {
			return execute(Parser.parseEvent(userCommand.substring(9)));
		}
	}

	/**
	 * @param eventToBePostponed
	 * @return Event[] containing the postponed event
	 */
	public Event[] execute(Event eventToBePostponed) {
		if (eventBeingPostponed == null) {
			eventBeingPostponed = DataManager.getEventById(eventToBePostponed
					.getId());
			return new Event[] { eventBeingPostponed };
		} else {
			boolean postponed = postpone(eventBeingPostponed,
					eventToBePostponed);
			if (postponed) {
				undoableCheck = true;
				Event[] resultOfPostpone = new Event[1];
				resultOfPostpone[0] = eventBeingPostponed;
				eventBeingPostponed = null;
				return resultOfPostpone;
			} else {
				eventBeingPostponed = null;
				return null;
			}
		}

	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @return boolean - true if event is postponed, false otherwise
	 */
	private boolean postpone(Event oldEvent, Event newEvent) {
		originalEvent = oldEvent;
		boolean checkPostponed = false;
		oldStartTime = new EventDateTime(oldEvent.getStart().getTimeInMilli());

		if (newEvent.getStart().getDate().isDefaultTime()) {
			oldEvent.getStart().set(Calendar.HOUR_OF_DAY,
					newEvent.getStart().get(Calendar.HOUR_OF_DAY));
			oldEvent.getStart().set(Calendar.MINUTE,
					newEvent.getStart().get(Calendar.MINUTE));
		}

		oldEvent.setStart(newEvent.getStart());
		oldEvent.setEnd(newEvent.getEnd());
		oldEvent.setDuration(newEvent.getDuration());

		checkPostponed = DataManager.replace(oldEvent, oldEvent);
		return checkPostponed;

	}

	/**
	 * @return Event[] containing undone events Original events are restored as
	 *         they were before the postpone
	 */
	@Override
	public Event[] undo() {
		Event[] undone = new Event[1];
		originalEvent.setStart(oldStartTime);
		if (DataManager.replace(originalEvent, originalEvent)) {
			undone[0] = originalEvent;
			return undone;
		}

		return null;
	}

	/**
	 * @return boolean - true if action should be added to undoable stack Only
	 *         legitimate postpone actions are to be added to the stack
	 */
	@Override
	public boolean isUndoable() {
		return undoableCheck;
	}

	@Override
	public String errorMessage() {
		return null;
	}

}
