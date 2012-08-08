/**
 * This class allows sync to google calendar
 * @author Vaarnan Drolia
 */
package googlecalendar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import storage.UpdateListener;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import data.Event;
import data.EventList;
import data.EventDateTime;

public class GoogleCalendar implements UpdateListener, Runnable
{
	private Logger logger = Logger.getLogger(GoogleCalendar.class.getName());
	private static final String USER_CALENDAR_URL = "https://www.google.com/calendar/feeds/default/private/full";
	private static final String APPLICATION_NAME = "QuickToDo";
	public static final String APPLICATION_NAME_ID_PROPERTY = String.format(":%s:%s:",APPLICATION_NAME, "Id");
	private CalendarService calenService = new CalendarService(APPLICATION_NAME);
	private ArrayList<Event[]> toSyncList = new ArrayList<Event[]>();
	private String username;
	private String password;
	private static boolean loggedIn = false;
	private static URL userCalendarUrl;

	/**
	 * 
	 * @param username
	 * @param password
	 * @return boolean- true if login successful, false otherwise
	 */
	public boolean login(String username, String password) {
		this.username = username;
		this.password = password;
		try {
			calenService.setUserCredentials(username, password);
			userCalendarUrl = new URL(
					USER_CALENDAR_URL);
			loggedIn = true;
		} catch (AuthenticationException e) {
			logger.error(e.getStackTrace());
			loggedIn = false;
		} catch (MalformedURLException e) {
			logger.error(e.getStackTrace());
			loggedIn = false;
		}
		return loggedIn;
	}

	/**
	 * 
	 * @return boolean- true if login successful, false otherwise
	 */
	public boolean login() {
		int count = 0;
		while (!loggedIn && count != 5) {
			login(username, password);
		}

		return loggedIn;
	}

	/**
	 * 
	 * @return boolean- true if logout is successful
	 */
	public boolean logout() {
		calenService = new CalendarService(APPLICATION_NAME);
		loggedIn = false;
		this.username = null;
		this.password = null;
		return true;
	}

	/**
	 * Gets a list of all entries in the calendar
	 * 
	 * @return
	 */
	private List<CalendarEventEntry> getAllEntries() {
		CalendarEventFeed resultFeed = null;

		if (!loggedIn) {
			return null;
		}

		try {
			resultFeed = calenService.getFeed(userCalendarUrl,
					CalendarEventFeed.class);
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		} catch (ServiceException e) {
			logger.error(e.getStackTrace());
		}
		List<CalendarEventEntry> entries = resultFeed.getEntries();
		return entries;
	}

	/**
	 * 
	 * @param startPeriod
	 * @param endPeriod
	 * @return
	 */
	public EventList getUserEntries(EventDateTime startPeriod,
			EventDateTime endPeriod) {
		CalendarQuery periodQuery = new CalendarQuery(userCalendarUrl);
		periodQuery.setMinimumStartTime(getDateTime(startPeriod));
		periodQuery.setMaximumStartTime(getDateTime(endPeriod));

		CalendarEventFeed resultFeed = null;
		if (!loggedIn) {
			return null;
		}

		try {
			resultFeed = calenService.query(periodQuery,
					CalendarEventFeed.class);
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		} catch (ServiceException e) {
			logger.error(e.getStackTrace());
		}

		return calendarEntryListToEventList(resultFeed.getEntries());
	}

	/**
	 * 
	 * @param userEntries
	 * @return
	 */
	private EventList calendarEntryListToEventList(
			List<CalendarEventEntry> userEntries) {
		EventList list = new EventList();
		for (CalendarEventEntry entry : userEntries) {
			list.addEvent(calendarEntryToEvent(entry));
		}
		return list;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	private Event calendarEntryToEvent(CalendarEventEntry e) {
		Event eventObject = new Event();
		eventObject.setName(e.getTitle().getPlainText());
		eventObject.setDescription(e.getContent()
				.getLang());
		List<When> eventTime = e.getTimes();
		eventObject.setStart(getEventDateTime(eventTime.get(0).getStartTime()));
		eventObject.getStart().setTimeSet(!eventTime.get(0).getStartTime().isDateOnly());
		EventDateTime end = getEventDateTime(eventTime.get(0).getStartTime());
		if (end.equals(eventObject.getStart())) {
			end = null;
		}
		eventObject.setEnd(end);
		for (ExtendedProperty ep : e.getExtendedProperty()) {
			if (ep.getName().equals(APPLICATION_NAME_ID_PROPERTY)) {
				eventObject.setId(ep.getValue());
			}
		}
		return eventObject;
	}

	/**
	 * 
	 * @return
	 */
	public EventList getSelfEvents() {
		//TODO Implement using extended property
		return null;//getCalendarTextQuery(DESCRIPTION_IDENTIFIER);
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public EventList getCalendarTextQuery(String text) {
		Query textQuery = new Query(userCalendarUrl);
		textQuery.setFullTextQuery(text);
		CalendarEventFeed textResultsFeed = null;
		try {
			textResultsFeed = calenService.query(textQuery,
					CalendarEventFeed.class);
		} catch (IOException e) {
			logger.error(e.getStackTrace());
			return null;
		} catch (ServiceException e) {
			logger.error(e.getStackTrace());
			return null;
		}
		if (textResultsFeed.getEntries().size() > 0) {
			return calendarEntryListToEventList(textResultsFeed.getEntries());
		}
		return null;
	}

	/**
	 * 
	 * @param ev
	 * @return
	 */
	public CalendarEventEntry addEvent(Event ev) {
		return addEvent(ev, 15, Method.ALERT);
	}
	
	/**
	 * 
	 * @param ev
	 * @return
	 */
	public CalendarEventEntry addEvent(Event ev, int reminderMins, Method reminderMethod) {

		CalendarEventEntry newEntry = new CalendarEventEntry();
		newEntry.setTitle(new PlainTextConstruct(ev.getName()));
		newEntry.setContent(new PlainTextConstruct(ev.getDescription()));

		DateTime startTime = getDateTime(ev.getStart());
		DateTime endTime = getDateTime(ev.getEnd());

		if (startTime == null) {
			// No start time for the event
			return new CalendarEventEntry();
		}
		When eventTime = new When();
		eventTime.setStartTime(startTime);
		if (endTime != null) {
			eventTime.setEndTime(endTime);
		} // else sets start and end to same time

		newEntry.addTime(eventTime);

		// Send the request and receive the response:
		try {
			CalendarEventEntry insertedEntry = calenService.insert(
					userCalendarUrl, newEntry);
			if (insertedEntry == null) {
				return null;
			}
			if (reminderMins > 0) {
				Method methodType = reminderMethod;
				Reminder reminder = new Reminder();
				reminder.setMinutes(reminderMins);
				reminder.setMethod(methodType);
				insertedEntry.getReminder().add(reminder);
				insertedEntry.update();
			}
			ExtendedProperty eventId = new ExtendedProperty();
			eventId.setName(APPLICATION_NAME_ID_PROPERTY);
			eventId.setValue(ev.getId());
			insertedEntry.addExtendedProperty(eventId);
			insertedEntry.update();
			return insertedEntry;
		} catch (IOException e) {
			logger.error(e.getStackTrace());
			return null;
		} catch (ServiceException e) {
			logger.error(e.getStackTrace());
			return null;
		}
		// insertedEntry.

	}

	private DateTime getDateTime(EventDateTime eventDateTime) {
		EventDateTime defaultTime = new EventDateTime();

		if (eventDateTime == null || eventDateTime.isDefaultTime()) {
			return null;
		} else if (defaultTime.equals(eventDateTime.getTime())) {
			return DateTime.parseDate(eventDateTime.toDateXml());
		} else {
			return DateTime.parseDateTime(eventDateTime.toDateTimeXml());
		}
	}

	private EventDateTime getEventDateTime(DateTime dateTime) {
		return EventDateTime.xmlToEventTime(dateTime.toString());
	}

	/**
	 * 
	 * @return boolean- true if user is logged in, false otherwise
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * 
	 * @param eventToDelete
	 * @return
	 */
	public CalendarEventEntry deleteEvent(Event eventToDelete) {
		if (eventToDelete.getStart().isDefaultTime()) {
			return new CalendarEventEntry();
		}
		List<CalendarEventEntry> searchList = getAllEntries();
		for (CalendarEventEntry entry : searchList) {
			Event gcalEntry = calendarEntryToEvent(entry);
			if (eventToDelete.getName().equals(gcalEntry.getName())
					&& eventToDelete.getStart().equals(gcalEntry.getStart())) {
				try {
					entry.delete();
					return entry;
				} catch (IOException e) {
					logger.error(e.getStackTrace());
					return entry;
				} catch (ServiceException e) {
					logger.error(e.getStackTrace());
					return entry;
				}
			}
		}

		return null;
	}

	/**
	 * 
	 */
	@Override
	public void updated(Event oldEvent, Event newEvent) {
		toSyncList.add(new Event[] { oldEvent, newEvent });
		(new Thread(this)).start();
	}

	/**
	 * 
	 */
	public synchronized void sync() {
		while (!toSyncList.isEmpty()) {
			Event[] element = toSyncList.get(0);
			Event oldEvent = element[0];
			Event newEvent = element[1];
			if (update(oldEvent, newEvent)) {
				toSyncList.remove(0);
			}
		}
	}

	private boolean update(Event oldEvent, Event newEvent) {
		if (loggedIn || login()) {
			if (oldEvent == null) {
				if (addEvent(newEvent) != null) {
					return true;
				}
			} else if (newEvent == null) {
				if (deleteEvent(oldEvent) != null) {
					return true;
				}
			} else if (oldEvent != null && newEvent != null) {
				if (deleteEvent(oldEvent) != null && addEvent(newEvent) != null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		sync();
	}
}
