/**
 * List Collection is our live storage
 * It holds all the lists in a hashmap
 * This is always synced with the permanent storage
 * Hence access for data is provided from here
 * If data is modified, it is modified here.
 * DataManager then updates the permanent storage with changes made here
 * @author Vaarnan Drolia
 * 
 */
package data;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import core.Utility;

public class ListCollection
{
	private Logger logger = Logger.getLogger(ListCollection.class.getName());
	private HashMap<String, EventList> eventListMap;
	private static final int DEFAULT_NUMBER_OF_LISTS = 2;

	public ListCollection() {
		this(DEFAULT_NUMBER_OF_LISTS);
		logger.debug("Creating new list");
	}

	/**
	 * creates a listcollection of the specified size
	 * 
	 * @param initSize
	 */
	public ListCollection(int initSize) {
		logger.debug(initSize);
		
		eventListMap = new HashMap<String, EventList>(initSize);
	}

	/**
	 * adds a list of a specific name to the collection of lists
	 * 
	 * @param listName
	 */
	public void addList(String listName) {
		logger.debug(listName);
		addList(listName, new EventList(listName));
	}

	/**
	 * adds a specified event to a particular list
	 * 
	 * @param listName
	 * @param eventToBeAdded
	 */
	public void addEvent(String listName, Event eventToBeAdded) {
		if (logger.isDebugEnabled()) {
			Utility.functionArgumentList(listName, eventToBeAdded.toString());
		}
		
		EventList list = eventListMap.get(listName);
		if (list == null) {
			list = new EventList(listName);
			eventListMap.put(listName, list);
		}
		
		list.addEvent(eventToBeAdded);
	}

	/**
	 * 
	 * @param nameOfList
	 * @return retrieves the eventlist from the hashmap
	 */
	public EventList get(String nameOfList) {
		logger.debug(nameOfList);
		return eventListMap.get(nameOfList);
	}

	// FIXME what exactly does this function do?!
	/**
	 * 
	 * 
	 * @param listName
	 * @param eventList
	 */
	public void addList(String listName, EventList eventList) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(listName, eventList.getName()));
		}
		eventListMap.put(listName, eventList);
	}

	/**
	 * 
	 * @return all the eventlists in the listmap
	 */
	public EventList[] getLists() {
		return eventListMap.values().toArray(new EventList[eventListMap.size()]);
	}

	/**
	 * removes the event from the specified list
	 * 
	 * @param eventToBeRemoved
	 * @param listName
	 */
	public void removeEventFromList(Event eventToBeRemoved, String listName) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(eventToBeRemoved.toString(), listName));
		}
		EventList eventList = eventListMap.get(listName);
		eventList.remove(eventToBeRemoved);
	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @param listName
	 * @return boolean true if the replace was successful, false otherwise
	 */
	public boolean replace(Event oldEvent, Event newEvent, String listName) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(oldEvent.toString(),
					newEvent.toString(), listName));
		}
		EventList eventList = eventListMap.get(listName);
		if (eventList == null) {
			logger.warn("List does not exist");
			return false;
		}
		logger.debug("Replaced object");
		return eventList.replace(oldEvent, newEvent);
	}

	/**
	 * 
	 * @param listName
	 * @return an arraylist of all the events in the specified list
	 */
	public ArrayList<Event> getAllEventsInList(String listName) {
		logger.debug(listName);
		EventList eventList = eventListMap.get(listName);
		if (eventList == null) {
			logger.warn("List does not exist");
			return null;
		}
		ArrayList<Event> listOfEvents = new ArrayList<Event>();
		listOfEvents = eventList.getEvents();
		return listOfEvents;
	}
}
