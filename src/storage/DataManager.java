/**
 * class DataManager
 * 
 * DataManager provides functionality to action classes to perform actions that are required
 * abstracts the file handling and data storage from the action classes
 * allows us to easily modify the data storage without disturbing higher level functionality
 * 
 * current functionality-
 * expected functionality-
 * 
 * 
 * @author Ankit Ganla
 * @author Vaarnan Drolia
 */

package storage;

import googlecalendar.GoogleCalendar;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import core.Utility;

import data.Event;
import data.EventDateTime;
import data.EventList;
import data.ListCollection;

public class DataManager
{
	public static int MAX_NUM_OF_LISTS = 20;
	private static ListCollection eventListCollection = new ListCollection();
	private static String defaultListName = "default";
	private static String currentListName = defaultListName;
	private static FileDatabase quickDatabase = null;
	private static ArrayList<UpdateListener> updateListenerList = new ArrayList<UpdateListener>();
	private static HashMap<String, Event> eventHashMap = new HashMap<String, Event>();
	private static GoogleCalendar googleCalendar;
	private static Logger logger = Logger.getLogger(DataManager.class.getName());
	public DataManager() {

	}
	
	/**
	 * 
	 * @param eventToBeAdded
	 * @return boolean- true if event is successfully added, false otherwise
	 */
	public static boolean add(Event eventToBeAdded) {
		logger.debug(eventToBeAdded);
		assert(eventToBeAdded != null);
		return add(eventToBeAdded, eventToBeAdded.getList());
	}

	/**
	 * 
	 * @param eventToBeAdded
	 * @param listName
	 * @return boolean- true if event is successfully added to specified list,
	 *         false otherwise
	 */
	public static boolean add(Event eventToBeAdded, String listName) {
		if (logger.isDebugEnabled()) {
			logger.debug(eventToBeAdded + "," + listName);
		}

		assert(eventToBeAdded != null);
		
		if (listName == null || listName.trim().equals("")) {
			eventToBeAdded.setList(currentListName);
			listName = currentListName;
		}

		String[] namesOfLists = getListNames();
		
		for (String nameOfList : namesOfLists) {
			if (eventToBeAdded.getList().equalsIgnoreCase(nameOfList)) {
				eventToBeAdded.setList(nameOfList);
			}
		}
		
		eventListCollection.addEvent(listName, eventToBeAdded);
		
		if (eventToBeAdded.getId() == null) {
			eventToBeAdded.setId(generateUniqueId());
		}

		eventHashMap.put(eventToBeAdded.getId(), eventToBeAdded);

		return saveLists(null, eventToBeAdded);
	}

	private static String generateUniqueId() {
		String eventId;
		logger.debug("Generating unique Id for event");
		eventId = Utility.generateId();
		
		while (eventHashMap.containsKey(eventId)) {
			logger.debug("EventId already taken. Generating new id");
			eventId = Utility.generateId();
		}
		
		return eventId;
	}
 	
	/**
	 * 
	 * @return boolean- true if lists are loaded into live storage, false
	 *         otherwise
	 */
	public static boolean loadLists() {
		quickDatabase = new FileDatabase();
		String[] listNames = getListNames();
		if (listNames.length == 0) {
			logger.info("no lists detected, creating database");
			return createDefaultDatabase();
		}

		for (String listName : listNames) {
			if (listName == null || listName.trim().equals("")) {
				continue;
			}
			
			eventListCollection.addList(listName,
					quickDatabase.readList(listName));
		}

		//TODO Task can be delegated to a thread?
		for (EventList eventList : eventListCollection.getLists()) {
			for (Event e : eventList.getEvents()) {
				if (e.getStart() == null) {
					e.setStart(new EventDateTime());
				}
				if (e.getId() == null) {
					e.setId(generateUniqueId());
				}
				eventHashMap.put(e.getId(), e);
			}
		}
		return true;
	}

	private static boolean createDefaultDatabase() {
		EventList eventList = new EventList(defaultListName);
		eventListCollection.addList(defaultListName, eventList);
		return quickDatabase.writeEventList(eventList);
	}

	/**
	 * 
	 * @return String array containing names of all lists currently in storage
	 */
	public static String[] getListNames() {
		return quickDatabase.getEventListNames();
	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @return true if changes in list are updated in permanent storage, false
	 *         otherwise
	 */
	private static boolean saveLists(Event oldEvent, Event newEvent) {
		if (logger.isDebugEnabled()) {
			logger.debug(oldEvent + "," + newEvent);
		}
		return saveLists(new Event[]{oldEvent}, new Event[]{newEvent});
	}

	/**
	 * 
	 * @param oldEvents
	 * @param newEvents
	 * @return true if changes in list are updated in permanent storage, false
	 *         otherwise
	 */
	private static boolean saveLists(Event[] oldEvents, Event[] newEvents) {
		if (logger.isDebugEnabled()) {
			logger.debug(oldEvents + "," + newEvents);
		}
		EventList[] eventListArray = eventListCollection.getLists();
		boolean flag = true;

		for (EventList eventList : eventListArray) {
			if (!quickDatabase.writeEventList(eventList)) {
				logger.warn("List was not written to the file list - " + eventList.getName());
				flag = false;
			}
		}

		for (int i = 0; i < oldEvents.length; i++) {
			fireStateChanged(oldEvents[i], newEvents[i]);
		}
		return flag;
	}
	
	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 */
	private static void fireStateChanged(Event oldEvent, Event newEvent) {
		logger.debug(oldEvent + "," + newEvent);
		for (UpdateListener listener : updateListenerList) {
			listener.updated(oldEvent, newEvent);
		}
	}

	/**
	 * 
	 * @param eventToRemove
	 * @return boolean- true if event is successfully removed from database,
	 *         false if otherwise
	 */
	public static boolean remove(Event eventToRemove) {
		logger.debug(eventToRemove);
		assert(eventToRemove != null);
		return remove(eventToRemove, eventToRemove.getList());
	}

	/**
	 * 
	 * @param eventToRemove
	 * @param listName
	 * @return true if event is successfully removed from specified list in
	 *         database, false if otherwise
	 */
	public static boolean remove(Event eventToRemove, String listName) {
		logger.debug(eventToRemove + "," + listName);
		if (listName == null) {
			listName = defaultListName;
		}
		assert(eventToRemove != null);
		eventToRemove.setList(listName);
		eventListCollection.removeEventFromList(eventToRemove, listName);
		eventHashMap.remove(eventToRemove.getId());
		return saveLists(eventToRemove, null);
	}

	/**
	 * 
	 * @param listName
	 * @return Array of all events in a specified list
	 */
	public static Event[] getAllEventsInList(String listName) {
		logger.debug(listName);
		ArrayList<Event> allEventsInList = eventListCollection
				.getAllEventsInList(listName);
		return (Event[]) allEventsInList.toArray(new Event[allEventsInList.size()]);
	}

	/**
	 * 
	 * @return event array of ALL events currently in database
	 */
	public static Event[] getAllEventsInStorage() {
		logger.debug("In all eventsInStorage");
		ArrayList<Event> allEventsInList = new ArrayList<Event>();
		String[] namesOfLists = getListNames();

		for (String listName : namesOfLists) {
			allEventsInList.addAll(eventListCollection
					.getAllEventsInList(listName));
		}
		return (Event[]) allEventsInList.toArray(new Event[allEventsInList.size()]);
	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @return boolean- true if event is replaced in database, false otherwise
	 */
	public static boolean replace(Event oldEvent, Event newEvent) {
		logger.debug(oldEvent + "," + newEvent);
		assert(oldEvent != null);
		if (!replace(oldEvent, newEvent, oldEvent.getList())) {
			logger.warn("Event could not be replaced");
			return false;
		}
		logger.debug("Event was successfully replaced");
		return true;
	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @param listName
	 * @return boolean- true if event is replaced in database, false otherwise
	 */
	public static boolean replace(Event oldEvent, Event newEvent,
			String listName) {
		logger.debug(oldEvent + "," + newEvent + "," + listName);
		
		return replaceAll(new Event[]{oldEvent}, new Event[]{newEvent}, listName);
	}

	/**
	 * 
	 * @param oldEvents
	 * @param newEvents
	 * @param listName
	 * @return boolean- true if all events in the array are successfully
	 *         replaced, false if even one fails
	 */
	public static boolean replaceAll(Event[] oldEvents, Event[] newEvents,
			String listName) {
		logger.debug(oldEvents + "," + newEvents + "," + listName);
		
		boolean replacedAll = true;
		
		if (oldEvents.length != newEvents.length) {
			logger.error("Number of new events not equal to old events in replace");
			return false;
		}

		for (int i = 0; i < oldEvents.length; i++) {
			if (!eventListCollection.replace(oldEvents[i], newEvents[i], listName)) {
				replacedAll = false;
			}
			newEvents[i].setId(oldEvents[i].getId());
			eventHashMap.put(oldEvents[i].getId(), newEvents[i]);
		}
		saveLists(oldEvents, newEvents);
		return replacedAll;
	}

	/**
	 * Add an UpdateListener which will get notified of updates to the database
	 * @param listener
	 */
	public static void addUpdateListener(UpdateListener listener) {
		logger.debug(listener);
		updateListenerList.add(listener);
	}

	/**
	 * 
	 * @param id
	 * @return event object related to the id passed as param
	 */
	public static Event getEventById(String id) {
		logger.debug(id);
		return eventHashMap.get(id);
	}

	/**
	 * 
	 * @param gcal
	 */
	public static void setGoogleCalendar(GoogleCalendar gcal) {
		googleCalendar = gcal;
	}

	/**
	 * 
	 * @return GoogleCalendar object
	 */
	public static GoogleCalendar getGoogleCalendar() {
		return googleCalendar;
	}

	/**
	 * 
	 */
	public static void exportToTxt() {
		quickDatabase.exportDatabaseToText(eventListCollection.getLists());
	}
}
