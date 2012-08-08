/**
 * This class returns all events to remind the user of his to-do's
 * It sorts all events
 * 1- Overdue Events
 * 2- Starred Events
 * 3- Regular Events
 * @author Harsha Narayan
 * @author Vaarnan Drolia
 */

package action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import storage.DataManager;
import data.Event;

public class Reminder extends Action implements DataMiner
{
	
	private Comparator<Event> compareEventStartTime = new Comparator<Event>() {
		
		@Override
		public int compare(Event o1, Event o2) {
			if (o1.equals(o2)) {
				return 0;
			}
			if (o1.getStart().isDefaultTime() && o2.getStart().isDefaultTime()) {
				return o1.getName().compareTo(o2.getName());
			} else if (o1.getStart().isDefaultTime()) {
				return 1;
			} else if (o2.getStart().isDefaultTime()) {
				return -1;
			} else {
				if (o1.getStart().compareTo(o2.getStart()) == 0) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return o1.getStart().compareTo(o2.getStart());
				}
			}
		}
	};

	private Overdue overdueObj = new Overdue();
	private Archive archiveObj = new Archive();
	private Logger logger = Logger.getLogger(Reminder.class.getName());
	/**
	 * @param userCommand
	 * @return Event[] of all events, sorted to remind
	 */
	@Override
	public Event[] execute(String userCommand) {
		return reminder(getAllEvents());
	}

	/**
	 * 
	 * @param allEvents
	 * @return Event[] of all events sorted for reminding
	 */
	public Event[] reminder(Event[] allEvents) {
		if (allEvents == null || allEvents.length == 0) {
			return allEvents;
		}
		
		HashSet<Event> allEventsSet = new HashSet<Event>();
		allEventsSet.addAll(Arrays.asList(allEvents));
		
		ArrayList<Event> reminderList = new ArrayList<Event>();
		
		Event[] archiveEvents = archiveObj.archive(new Event(), allEvents);
		if (archiveEvents != null) {
			allEventsSet.removeAll(Arrays.asList(archiveEvents));
		}

		Event[] overdueEvents = overdueObj.overdue(allEvents);
		ArrayList<Event> overdueEventList = new ArrayList<Event>();
		if (overdueEvents != null) {
			overdueEventList = new ArrayList<Event>(Arrays.asList(overdueEvents));
		}
		overdueEventList = sort(overdueEventList);
		reminderList.addAll(overdueEventList);
		allEventsSet.removeAll(overdueEventList);
		
		ArrayList<Event> starredEvents = getAllStarred(allEventsSet);
		reminderList.addAll(starredEvents);
		allEventsSet.removeAll(starredEvents);
		
		ArrayList<Event> remainingEvents = new ArrayList<Event>(allEventsSet);
		sort(remainingEvents);
		reminderList.addAll(remainingEvents);
		return (Event[]) reminderList.toArray(new Event[reminderList.size()]);
	}

	/**
	 * 
	 * @param allEvents
	 * @return ArrayList of all starred events
	 */
	private ArrayList<Event> getAllStarred(HashSet<Event> allEvents) {

		ArrayList<Event> starredEvents = new ArrayList<Event>();
		for (Event event : allEvents) {
			if (event.getStarred()) {
				starredEvents.add(event);
			}
		}
		return sort(starredEvents);
	}

	/**
	 * 
	 * @param listOfEvents
	 * @return sorted arraylist of events passes as param
	 */
	private ArrayList<Event> sort(ArrayList<Event> listOfEvents) {
		Collections.sort(listOfEvents, compareEventStartTime);
		return listOfEvents;
	}

	/**
	 * 
	 * @return all events in storage as an array
	 */
	private static Event[] getAllEvents() {
		return DataManager.getAllEventsInStorage();
	}

	/**
	 * undo is irrelevant for this class
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * undo is irrelevant for this class
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "Reminders cannot be displayed";
	}

	/**
	 * @return string- command currently being executed in this case, reminder
	 */
	@Override
	public String getCommandName() {
		return "reminder";
	}
}
