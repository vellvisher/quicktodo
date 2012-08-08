/**
 * This class helps the user find a free slot of a specified duration on a given date
 * It returns all free slots
 * @author Harsha Narayan
 * @author Vaarnan Drolia
 */
package action;

import java.util.ArrayList;

import data.Event;
import data.EventDateTime;
import action.CheckFree;

import java.util.Calendar;

import core.Utility;

import parser.Parser;

public class Schedule extends Action
{

	private static final int TOTAL_MIN_DAY = 1440;
	private static CheckFree checkFreeObj = new CheckFree();
	private static Event[] eventsPrevSameDays;

	/**
	 * returns the result of the schedule action
	 * 
	 * @param userCommand
	 * @return Event[]
	 */
	@Override
	public Event[] execute(String userCommand) {
		String params = userCommand.replace("schedule ", "");
		Event scheduleEvent = Parser.parseEvent(params);
		if (scheduleEvent != null && isValidCommand(userCommand)) {
			return schedule(scheduleEvent);
		} else {
			return null;
		}
	}

	/**
	 * validates the userCommand
	 * 
	 * @param s
	 * @return boolean- true if validated
	 */
	private boolean isValidCommand(String s) {
		Event scheduleEvent = Parser.parseEvent(s);
		if (scheduleEvent.getDuration() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param scheduleEvent
	 * @return array of free slots as events
	 */
	public static Event[] schedule(Event scheduleEvent) {

		int durationNeeded = scheduleEvent.getDuration();
		int freeMinCount = 0;
		ArrayList<Event> freeSlots = new ArrayList<Event>();
		ArrayList<Boolean> recordOfMins = new ArrayList<Boolean>();

		recordOfMins = trackingArray(scheduleEvent);

		for (int i = 0; i < recordOfMins.size(); i++) {
			if (recordOfMins.get(i) == false) {
				freeMinCount = 0;
			} else {
				freeMinCount++;
				if (freeMinCount == durationNeeded) {
					int startMin = i - freeMinCount + 1;
					int hour = startMin / 60;
					int min = startMin % 60;

					Event freeSlotEvent = new Event();
					freeSlotEvent.setName("free slot");

					EventDateTime freeSlotTime = new EventDateTime(
							scheduleEvent.getStart().getTimeInMilli());
					freeSlotTime.set(Calendar.HOUR_OF_DAY, hour);
					freeSlotTime.set(Calendar.MINUTE, min);
					freeSlotEvent.setStart(freeSlotTime);

					freeSlotEvent.setDuration(durationNeeded);
					if (freeSlotEvent.getStart().compareTo(
							EventDateTime.getCurrentTime()) > 0) {
						freeSlots.add(freeSlotEvent);
					}
					freeMinCount = 0;
				}
			}
		}

		Event[] arrayOfFreeSlots = freeSlots
				.toArray(new Event[freeSlots.size()]);

		return arrayOfFreeSlots;
	}

	/**
	 * 
	 * @param checkFreeTime
	 * @return boolean- true if the time is free, false if there is an event
	 *         there
	 */
	public static boolean timeFlag(EventDateTime checkFreeTime) {
		Event checkFreeEvent = new Event();
		checkFreeEvent.setStart(checkFreeTime);
		Event[] checkIfEvents = checkFreeObj.checkFree(checkFreeEvent,
				eventsPrevSameDays);
		if (checkIfEvents.length == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param scheduleEvent
	 * @return list of booleans for each minute, checking if it is free or not.
	 *         If free, the end time is stored
	 */
	public static ArrayList<Boolean> trackingArray(Event scheduleEvent) {
		ArrayList<Boolean> checkByMin = new ArrayList<Boolean>();
		eventsPrevSameDays = Utility.eventsOnPrevSameDay(scheduleEvent);
		Event tempEvent = new Event();
		tempEvent.setStart(new EventDateTime(scheduleEvent.getStart()
				.getTimeInMilli()));
		for (int i = 0; i < TOTAL_MIN_DAY; i++) {
			checkByMin.add(timeFlag(tempEvent.getStart()));
			tempEvent.getStart().inc(Calendar.MINUTE);
		}
		return checkByMin;
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
		return "Could not find a schedule";
	}

	/**
	 * @return String containing the action being executed in this case,
	 *         schedule
	 */
	@Override
	public String getCommandName() {
		return "schedule";
	}

}
