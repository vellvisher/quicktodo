/**
 * @author Vaarnan Drolia
 */
package data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import core.Utility;

public class Event
{
	private Logger logger = Logger.getLogger(Event.class.getName());
	private String name;
	private EventDateTime start;
	private int duration;
	private int priority;
	private List<String> labels;
	private String list;
	private String id;

	private EventDateTime end;
	private String description;
	private boolean ticked;
	private boolean starred;
	private boolean deadlineEvent;

	public Event() {
		name = "";
		start = new EventDateTime();
		duration = 0;
		priority = -1;
		labels = null;
		list = null;
		end = null;
		description = null;
		// repetition = -1;
		ticked = false;
		id = null;
		starred = false;
		deadlineEvent = false;
	}

	/**
	 * event constructor
	 * 
	 * @param name
	 */
	public Event(String name) {
		this();
		logger.debug(name);
		this.name = name;
	}

	/**
	 * event constructor
	 * 
	 * @param name
	 */
	public Event(String eventName, EventDateTime startDateTime,
			EventDateTime endDateTime, int eventDuration, int stringToPriority,
			List<String> labels2, String eventList) {
		this();
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(eventName, startDateTime.toString(),
					endDateTime.toString(), Integer.toString(eventDuration), 
					Integer.toString(stringToPriority), labels2.toString(), eventList.toString()));
		}

		name = eventName;
		start = startDateTime;
		end = endDateTime;
		duration = eventDuration;

		if (!endDateTime.isDefaultTime()) {
			assert (start.compareTo(end) <= 0);
			duration = Utility.millisToMins(end.getTimeInMilli()
					- start.getTimeInMilli());
		}
		if (eventDuration > 0) {
			long endTime = start.getTimeInMilli();
			endTime += Utility.minsToMillis(duration);
			end = new EventDateTime(endTime);
		}

		priority = stringToPriority;
		labels = labels2;
		list = eventList;
	}

	/**
	 * 
	 * @return int the priority of the event
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * set the priority of the function
	 * 
	 * @param priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * gets a list of all labels associated with the event
	 */
	public List<String> getLabels() {
		return labels;
	}

	/**
	 * sets all labels associated with the event
	 * 
	 * @param labels
	 */
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	/**
	 * 
	 * @return String the list that the event belongs to
	 */
	public String getList() {
		return list;
	}

	/**
	 * 
	 * Sets the list of an event
	 * 
	 * @param list
	 */
	public void setList(String list) {
		this.list = list;
	}

	/**
	 * 
	 * @return the name of the event
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the event
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the description of the event
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * sets the description of the event
	 * 
	 * @param desc
	 */
	public void setDescription(String desc) {
		this.description = desc;
	}

	/**
	 * gets the start time of an event
	 * 
	 * @return
	 */
	public EventDateTime getStart() {
		return start;
	}

	/**
	 * sets the start time of an event
	 * 
	 * @param start
	 */
	public void setStart(EventDateTime start) {
		this.start = start;
	}

	/**
	 * get the end time of an event
	 * 
	 * @return
	 */
	public EventDateTime getEnd() {
		return end;
	}

	/**
	 * set the end time of an event
	 * 
	 * @param end
	 */
	public void setEnd(EventDateTime end) {
		this.end = end;
		if (end != null) {
			duration = Utility.millisToMins(end.getTimeInMilli() - start.getTimeInMilli());
		}
	}

	/**
	 * get the duration of the event
	 * 
	 * @return
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * set the duration of an event
	 * 
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
		if (duration > 0) {
			end = new EventDateTime(start.getTimeInMilli());
			end.add(Calendar.MILLISECOND, Utility.millisToMins(duration));
		}
	}

	/**
	 * true if the event is ticked
	 * 
	 * @return
	 */
	public boolean getTicked() {
		return this.ticked;
	}

	/**
	 * sets the ticked value
	 * 
	 * @param value
	 */
	public void setTicked(boolean value) {
		this.ticked = value;
	}

	/**
	 * toggles the ticked value
	 * 
	 * @return
	 */
	public boolean toggleTicked() {
		if (ticked) {
			ticked = false;
		} else {
			ticked = true;
		}

		return ticked;
	}

	/**
	 * true if event is starred
	 * 
	 * @return
	 */
	public boolean getStarred() {
		return starred;
	}

	/**
	 * sets the star value of an event
	 * 
	 * @param value
	 */
	public void setStarred(boolean value) {
		starred = value;
	}

	/**
	 * toggles the star value of an event
	 * 
	 * @return
	 */
	public boolean toggleStarred() {
		if (starred) {
			starred = false;
		} else {
			starred = true;
		}
		return starred;
	}

	public ArrayList<String> toStringArrayList() {
		ArrayList<String> eventString = new ArrayList<String>();
		if (this.getName() != null) {
			eventString.add(this.getName());
		}

		if (!(this.getStart().getTime().isDefaultTime())) {
			if (this.getDeadlineEvent()) {
				eventString.add(" by ");
			} else {
				eventString.add(" at ");
			}
			int hourTime;
			int minTime;
			String time = "";
			hourTime = this.getStart().get(Calendar.HOUR);
			minTime = this.getStart().get(Calendar.MINUTE);
			if (hourTime == 0) {
				hourTime = 12;
			}
			String suffix = this.getStart().getTime()
					.getDisplayName(Calendar.AM_PM, Calendar.SHORT)
					.toLowerCase();
			time += hourTime;
			if (minTime > 0) {
				time += ".";
				if (minTime < 10) {
					time += "0";
				}
				time += minTime;
			}

			eventString.add(time + suffix);
		}
		if (this.getDuration() > 0) {
			int min[] = Utility.minutesToHour(this.getDuration());
			eventString.add(" for ");

			if (min[1] == 0) {
				eventString.add(min[0] + "hrs");
			} else if (min[0] == 0) {
				eventString.add(min[1] + "mins");
			} else {
				eventString.add(min[0] + "hrs " + min[1] + "mins");
			}
		}

		if (!(this.getStart().getDate().isDefaultTime())) {
			int day = this.getStart().get(Calendar.DAY_OF_MONTH);
			String month = this.getStart().getDate()
					.getDisplayName(Calendar.MONTH, Calendar.SHORT);
			if (this.getDeadlineEvent()) {
				eventString.add(" by ");
			} else {
				eventString.add(" on ");
			}
			String dateString = day + Utility.getDaySuffix(day) + " "
					+ month;

			int year = this.getStart().getDate().get(Calendar.YEAR);

			if (year != EventDateTime.getCurrentTime().get(Calendar.YEAR)) {
				dateString += ", " + year;
			}

			eventString.add(dateString);
		}

		if (this.getLabels() != null) {
			for (String label : this.getLabels()) {
				eventString.add(" @");
				eventString.add(label);
			}
		}
		return eventString;
	}

	/**
	 * converts an event to string
	 */
	public String toString() {
		ArrayList<String> stringArray = toStringArrayList();
		StringBuilder eventString = new StringBuilder("");
		for (String part : stringArray) {
			eventString.append(part);
		}
		return eventString.toString();
	}

	/**
	 * compares two events for equality
	 */
	public boolean equals(Object to) {
		// FIXME this function needs to be checked
		String thisObjString = null;
		String compareToObjString = null;
		if (to == null) {
			return false;
		}
		if (!(to instanceof Event)) {
			return false;
		}
		Event compareTo = (Event) to;

		thisObjString = this.toString();
		compareToObjString = compareTo.toString();
		return thisObjString.equals(compareToObjString);
	}

	/**
	 * gets the event Id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * sets the event Id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * true if event has a deadline
	 * 
	 * @return
	 */
	public boolean getDeadlineEvent() {
		return deadlineEvent;
	}

	/**
	 * set the deadline of an event
	 * 
	 * @param newDeadline
	 */
	public void setDeadlineEvent(boolean newDeadline) {
		this.deadlineEvent = newDeadline;
	}
}
