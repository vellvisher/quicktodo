/**
 * implements Cloneable
 * deals with GregorianCalendar
 * This class deals with time and calendar objects
 * It is important to implement time fields of an event which are crucial to a to-do manager
 * @author Vaarnan Drolia
 */
package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;

import core.Utility;

public class EventDateTime implements Cloneable
{
	private Logger logger = Logger.getLogger(Event.class.getName());
	private GregorianCalendar calen;
	private long timeInMilli;
	private boolean timeSet;
	private static final SimpleDateFormat ISO_DATE_TIME = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static final SimpleDateFormat ISO_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat DAY_MONTH_YEAR = new SimpleDateFormat(
			"dd-MM-yyyy");
	private static final SimpleDateFormat DAY_MONTH_YEAR_HOUR_MIN = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm");

	static {
		ISO_DATE_TIME.setLenient(false);
		ISO_DATE.setLenient(false);
	}

	/**
	 * default constructor for EventDateTime
	 */
	public EventDateTime() {
		logger.debug("Creating default EventDateTime Object");
		calen = new GregorianCalendar(1970, 0, 1, 0, 0);
		calen.setLenient(true);
		timeInMilli = calen.getTimeInMillis();
		timeSet = false;
	}

	/**
	 * EventDateTime constructor where time is passed as param in millis
	 * 
	 * @param timeInMillis
	 */
	public EventDateTime(long timeInMillis) {
		logger.debug(timeInMillis);
		calen = new GregorianCalendar(1970, 0, 1, 0, 0);
		calen.setTimeInMillis(timeInMillis);
		calen.setLenient(true);
		timeInMilli = calen.getTimeInMillis();
		// setTimeSet(false);
	}

	/**
	 * EventDateTime constructor
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 */
	public EventDateTime(int year, int month, int dayOfMonth) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(Integer.toString(year), 
				Integer.toString(month), Integer.toString(dayOfMonth)));
		}
		calen = new GregorianCalendar(year, month - 1, dayOfMonth, 0, 0);
		calen.setLenient(false);
		timeInMilli = calen.getTimeInMillis();
		setTimeSet(false);
	}

	/**
	 * EventDateTime constructor
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hourOfDay
	 * @param minute
	 */
	public EventDateTime(int year, int month, int dayOfMonth, int hourOfDay,
			int minute) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(Integer.toString(year), 
				Integer.toString(month), Integer.toString(dayOfMonth),
				Integer.toString(hourOfDay), Integer.toString(minute)));
		}
		calen = new GregorianCalendar(year, month - 1, dayOfMonth, hourOfDay,
				minute);
		calen.setLenient(false);
		timeInMilli = calen.getTimeInMillis();
		timeSet = true;
	}

	/**
	 * EventDateTime constructor
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 */
	public EventDateTime(int year, int month, int dayOfMonth, int hourOfDay,
			int minute, int second) {
		if (logger.isDebugEnabled()) {
			logger.debug(Utility.functionArgumentList(Integer.toString(year), 
				Integer.toString(month), Integer.toString(dayOfMonth),
				Integer.toString(hourOfDay), Integer.toString(minute),
				Integer.toString(second)));
		}
		
		calen = new GregorianCalendar(year, month - 1, dayOfMonth, hourOfDay,
				minute, second);
		calen.setLenient(false);
		timeInMilli = calen.getTimeInMillis();
		timeSet = true;
	}

	/**
	 * 
	 * @return currrent time as an EventDateTime object
	 */
	public static EventDateTime getCurrentTime() {
		Calendar curr = GregorianCalendar.getInstance();
		curr.setLenient(false);
		curr.getTimeInMillis();
		EventDateTime currTime = new EventDateTime(curr.get(Calendar.YEAR),
				curr.get(Calendar.MONTH) + 1, curr.get(Calendar.DAY_OF_MONTH),
				curr.get(Calendar.HOUR_OF_DAY), curr.get(Calendar.MINUTE),
				curr.get(Calendar.SECOND));
		return currTime;
	}

	/**
	 * 
	 * @param property
	 * @return particular property (day, month, year, hour of day, minute of
	 *         day)
	 */
	public int get(int property) {
		int value = calen.get(property);
		if (property == Calendar.MONTH) {
			value += 1;
		}
		return value;
	}

	/**
	 * set a particular calendar field with a new value
	 * 
	 * @param field
	 * @param value
	 */
	public void set(int field, int value) {
		if (field == Calendar.MONTH) {
			value -= 1;
		}
		calen.set(field, value);
		if (field == Calendar.HOUR_OF_DAY || field == Calendar.HOUR
				|| field == Calendar.MINUTE) {
			timeSet = true;
		}
		timeInMilli = calen.getTimeInMillis();
	}

	/**
	 * set new Calendar values
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void set(int year, int month, int day) {
		calen.set(year, month - 1, day);
		timeInMilli = calen.getTimeInMillis();
	}

	/**
	 * set new Calendar values
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hourOfDay
	 * @param minute
	 */
	public void set(int year, int month, int day, int hourOfDay, int minute) {
		calen.set(year, month - 1, day, hourOfDay, minute);
		timeInMilli = calen.getTimeInMillis();
		timeSet = true;
	}

	/**
	 * set new Calendar values
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 */
	public void set(int year, int month, int day, int hourOfDay, int minute,
			int second) {
		calen.set(year, month - 1, day, hourOfDay, minute, second);
		timeInMilli = calen.getTimeInMillis();
		timeSet = true;
	}

	/**
	 * used to compare two EventDateTime objects
	 * 
	 * @return boolean true if two EventDateTime objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EventDateTime)) {
			return false;
		}
		EventDateTime gc = (EventDateTime) obj;
		return this.getTimeInMilli() == gc.getTimeInMilli()
				&& (this.getTimeSet() == gc.getTimeSet());
	}

	/**
	 * Gets whether the time is default or set
	 */
	public boolean getTimeSet() {
		return timeSet;
	}

	/**
	 * Sets the timeSet parameter
	 * 
	 * @param timeSet
	 */
	public void setTimeSet(boolean timeSet) {
		this.timeSet = timeSet;
	}

	/**
	 * @return EventDateTime object as a string
	 */
	public String toString() {
		return Long.toString(getTimeInMilli());
	}

	/**
	 * @return EventDateTime object as a string
	 */
	public String toPrettyString() {
		if (isDefaultTime()) {
			return "";
		} else if (getTimeSet()) {
			return DAY_MONTH_YEAR_HOUR_MIN.format(calen.getTimeInMillis());
		} else {
			return DAY_MONTH_YEAR.format(calen.getTimeInMillis());
		}
	}
	
	/**
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.timeInMilli = time;
		calen.setTimeInMillis(time);
	}

	/**
	 * 
	 * @return time in Milliseconds
	 */
	public long getTimeInMilli() {
		return timeInMilli;
	}

	/**
	 * 
	 * @param time
	 */
	public void setTimeInMilli(long time) {
		this.timeInMilli = time;
		calen.setTimeInMillis(time);
	}

	/**
	 * implements the calen.getDisplayName() function
	 * 
	 * @param field
	 * @param style
	 * @return
	 */
	public String getDisplayName(int field, int style) {
		return calen.getDisplayName(field, style, Locale.getDefault());
	}

	/**
	 * This function compares two times in milliseconds a.compareto(b) where a
	 * and b are EventDateTime objects will return a(time in millis) - b(time in
	 * millis)
	 * 
	 * @param other
	 * @return
	 */
	public int compareTo(EventDateTime other) {
		long difference = this.getTimeInMilli() - other.getTimeInMilli();
		if (difference < 0) {
			return -1;
		} else if (difference == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Increments the value of said field by 1
	 * 
	 * @param field
	 */
	public void inc(int field) {
		this.add(field, 1);
	}

	/**
	 * adds the value to the said field
	 * 
	 * @param field
	 * @param value
	 */
	public void add(int field, int value) {
		this.calen.add(field, value);
		timeInMilli = this.calen.getTimeInMillis();
	}

	/**
	 * 
	 * @return
	 */
	public String toDateTimeXml() {
		return ISO_DATE_TIME.format(new Date(timeInMilli));
	}

	public String toDateXml() {
		return ISO_DATE.format(new Date(timeInMilli));
	}

	/**
	 * converts an xmlTime object in to an EventDateTime object
	 * 
	 * @param xmlTime
	 * @return time
	 */
	public static EventDateTime xmlToEventTime(String xmlTime) {
		Date date;
		date = xmlToDateObj(xmlTime);
		if (date == null) {
			return null;
		}
		return new EventDateTime(date.getTime());
	}

	/**
	 * converts an xmlTime object in to a Date object
	 * 
	 * @param xmlTime
	 * @return time
	 */
	private static Date xmlToDateObj(String xmlTime) {
		try {
			return ISO_DATE_TIME.parse(xmlTime);
		} catch (ParseException e) {
			try {
				return ISO_DATE.parse(xmlTime);
			} catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 
	 * @return time as an EventDateTime object
	 */
	public EventDateTime getTime() {
		EventDateTime newEventTime = new EventDateTime();
		newEventTime.set(Calendar.HOUR_OF_DAY, this.get(Calendar.HOUR_OF_DAY));
		newEventTime.set(Calendar.MINUTE, this.get(Calendar.MINUTE));
		newEventTime.getTimeInMilli();
		newEventTime.setTimeSet(timeSet);
		return newEventTime;
	}

	/**
	 * 
	 * @return date as an EventDateTime object
	 */
	public EventDateTime getDate() {
		return new EventDateTime(this.get(Calendar.YEAR),
				this.get(Calendar.MONTH), this.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 
	 * @return boolean - true if time was set by default
	 */
	public boolean isDefaultTime() {
		return this.equals(new EventDateTime()) && !timeSet;
	}
}
