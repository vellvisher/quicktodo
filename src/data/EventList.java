/**
 * This is an array implementation of a list
 * All events are stored under some list
 * If a list is not specified, a default list is specified
 * List implementation will be further developed
 * @author Vaarnan Drolia
 */
package data;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import core.Utility;

public class EventList
{
	private static final String DEFAULT_LIST_NAME = "default";
	private Logger logger = Logger.getLogger(EventList.class.getName());
	private ArrayList<Event> eventList;
	private String listName;

	/**
	 * default constructor
	 */
	public EventList() {
		this(DEFAULT_LIST_NAME);
		logger.debug("Creating list with default name");
	}

	/**
	 * 
	 * @return the size of the list
	 */
	public int size() {
		return eventList.size();
	}

	/**
	 * EventList constructor with specified name
	 * 
	 * @param name
	 */
	public EventList(String name) {
		eventList = new ArrayList<Event>();
		listName = name;
	}

	/**
	 * 
	 * @param eventToAdd
	 */
	public void addEvent(Event eventToAdd) {
		eventList.add(eventToAdd);
	}

	/**
	 * 
	 * @param oldEvent
	 * @param newEvent
	 * @return
	 */
	public boolean replace(Event oldEvent, Event newEvent) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(oldEvent.toString(), newEvent.toString()));
		}
		if (eventList.remove(oldEvent)) {
			eventList.add(newEvent);
			return true;
		}
		logger.warn("Unable to replace event");
		return false;
	}

	/**
	 * 
	 * @param eventToRemove
	 */
	public boolean remove(Event eventToRemove) {
		return eventList.remove(eventToRemove);
	}

	/**
	 * 
	 * @return returns name of the list
	 */
	public String getName() {
		return listName;
	}

	/**
	 * sets the name of the list to a new name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		listName = name;
	}

	/**
	 * @return list of events
	 */
	public ArrayList<Event> getEvents() {
		return eventList;
	}

	/**
	 * maps the list entered to the current eventList
	 * 
	 * @param listOfEvents
	 */
	public void setEvents(ArrayList<Event> listOfEvents) {
		eventList = listOfEvents;
	}

	/**
	 * boolean- true if two eventLists are equal
	 */
	public boolean equals(Object eventListObject) {
		if (eventListObject ==  null) {
			logger.warn("object entered was null");
			return false;
		}
		if (!(eventListObject instanceof EventList)) {
			logger.warn("object entered was not a valid EventList");
			return false;
		}

		EventList compareTo = (EventList) eventListObject;

		if (this.getName().equals(compareTo.getName())
				&& this.eventList.size() == compareTo.size()) {
			for (int i = 0; i < this.eventList.size(); i++) {
				if (!this.eventList.get(i).equals(compareTo.getEvents().get(i))) {
					return false;
				}
			}
			logger.debug("Objects are identical");
			return true;
		}
		logger.debug("Objects are not equal");
		return false;
	}

	/**
	 * 
	 * @param checkForEvent
	 * @return boolean - true if the list contains the event passed as param
	 */
	public boolean contains(Event checkForEvent) {
		return eventList.contains(checkForEvent);
	}
}
