/**
 * extends Action
 * This class finds an event or events based on a user string
 * @author Poornima Muthukumar
 * @author Harsha Narayan
 */
package action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import parser.Parser;

import storage.DataManager;
import data.Event;
import data.EventDateTime;

public class Find extends Action implements DataMiner
{
	private static final String NEXT_WEEK = "next week";

	/**
	 * @param userCommand
	 * @return Event[] of found events
	 */
	@Override
	public Event[] execute(String userCommand) {

		String params = "";
		if (userCommand.startsWith("find ")) {
			params = userCommand.replace("find ", "");
		} else if (userCommand.startsWith("search ")) {
			params = userCommand.replace("search ", "");
		}
		if (params.toLowerCase().contains("next week")
				&& !params.toLowerCase().contains("\"next week")) {
			return findFullWeek(params);
		}
		Event findEvent = Parser.parseEvent(params);
		return find(findEvent);
	}

	private Event[] findFullWeek(String params) {
		params = params.toLowerCase();
		Event findEvent = Parser.parseEvent(params.replace(NEXT_WEEK, ""));
		EventDateTime findForDay = EventDateTime.getCurrentTime();
		findForDay.set(Calendar.HOUR_OF_DAY,
				findEvent.getStart().get(Calendar.HOUR_OF_DAY));
		findForDay.set(Calendar.MINUTE,
				findEvent.getStart().get(Calendar.MINUTE));
		findEvent.setStart(findForDay);
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		for (int i = 1; i <= 7; i++) {
			Event[] eventsForDay = execute(findEvent);
			if (eventsForDay != null) {
				foundEvents.addAll(Arrays.asList(eventsForDay));
			}
			findEvent.getStart().inc(Calendar.DAY_OF_MONTH);
		}
		return (Event[]) foundEvents.toArray(new Event[foundEvents.size()]);
	}

	/**
	 * 
	 * @param eventToBeFound
	 * @return array of events matching the required event to be found
	 */
	public Event[] find(Event eventToBeFound) {
		Event[] eventArray;

		if (eventToBeFound.getId() != null) {
			eventArray = new Event[1];
			eventArray[0] = DataManager.getEventById(eventToBeFound.getId());
		} else {
			ArrayList<Event> eventList = new ArrayList<Event>();
			eventList = findEvent(eventToBeFound);
			eventArray = eventList.toArray(new Event[eventList.size()]);
		}
		if (eventArray.length == 0) {
			return null;
		}
		return eventArray;
	}

	/**
	 * Find events in given database
	 * @param eventToBeFound
	 * @param searchArray
	 * @return
	 */
	public Event[] find(Event eventToBeFound, Event[] searchArray) {
		Event[] eventArray;

		ArrayList<Event> eventList = new ArrayList<Event>();
		eventList = findEvent(eventToBeFound, searchArray);
			eventArray = eventList.toArray(new Event[eventList.size()]);

		if (eventArray.length == 0) {
			return null;
		}
		return eventArray;
	}
	
	/**
	 * @param eventToBeFound
	 * @return event array of found events
	 */
	public Event[] execute(Event eventToBeFound) {
		return find(eventToBeFound);
	}

	/**
	 * This function finds an event by trying to match its fields against all
	 * other events in the database
	 * 
	 * @param lookup
	 * @return
	 */
	public ArrayList<Event> findEvent(Event lookup) {
		return findEvent(lookup, DataManager.getAllEventsInStorage());
	}

	/**
	 * This function finds an event by trying to match its fields against all
	 * other events in the database
	 * @param lookup Event to look for
	 * @param searchEvents Event array to search in
	 * @return
	 */
	public ArrayList<Event> findEvent(Event lookup, Event[] searchEvents) {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		EventDateTime defaultTime = new EventDateTime();
		
		if (lookup.getName() != null) {
			lookup.setName(lookup.getName().toLowerCase());
		}

		if (lookup.getDescription() != null) {
			lookup.setDescription(lookup.getDescription().toLowerCase());
		}

		for (Event e : searchEvents) {
			if (("".equals(lookup.getName()) || e.getName().toLowerCase()
					.indexOf((lookup.getName())) != -1)
					&& (lookup.getStart().getDate() == null
							|| lookup.getStart().getDate().equals(defaultTime) || lookup
							.getStart().getDate()
							.equals(e.getStart().getDate()))
					&& (lookup.getStart().getTime() == null
							|| lookup.getStart().getTime().equals(defaultTime) || lookup
							.getStart().getTime()
							.equals(e.getStart().getTime()))
					&& (lookup.getDuration() == 0 || lookup.getDuration() == e
							.getDuration())
					&& (lookup.getEnd() == null
							|| lookup.getEnd().equals(defaultTime) || lookup
							.getEnd().equals(e.getEnd()))
					&& (lookup.getDescription() == null || e.getDescription()
							.toLowerCase().indexOf(lookup.getDescription()) != -1)
					&& (lookup.getStarred() == false || lookup.getStarred() == e
							.getStarred())
					&& (lookup.getTicked() == false || lookup.getTicked() == e
							.getTicked())
					&& (lookup.getList() == null || e.getList().startsWith(
							lookup.getList()))) {
				if (lookup.getLabels() == null) {
					foundEvents.add(e);
				} else if (e.getLabels() != null) {
					boolean flag = false;
					for (String lookupLabel : lookup.getLabels()) {
						lookupLabel = lookupLabel.toLowerCase();
						flag = false;
						for (String label : e.getLabels()) {
							if (label.toLowerCase().startsWith(lookupLabel)) {
								flag = true;
							}
						}
						if (flag == false) {
							break;
						}
					}
					if (flag) {
						foundEvents.add(e);
					}
				}
			}
		}

		return foundEvents;
	}

	
	/**
	 * undo is irrelevant in this class
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * undo is irrelevant in this class
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "Item not found";
	}

	/**
	 * @return string containing action being executed in this case, find
	 */
	@Override
	public String getCommandName() {
		return "find";
	}
}
