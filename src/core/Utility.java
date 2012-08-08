/**This class will contain miscellaneous functions which other classes may require.
 * It should almost always have only static functions which return some value
 * @author Vaarnan Drolia
 * @author Poornima Muthukumar
 */
package core;

import java.util.Calendar;
import java.util.Random;

import org.apache.log4j.Logger;

import action.Find;
import data.Event;
import data.EventDateTime;
import parser.Parser;
import storage.FileHandler;

public class Utility
{

	private static final String[] daysOfWeek = { "sun", "mon", "tue", "wed",
			"thu", "fri", "sat" };
	private static final String[][] days = { { "today", "0" },
			{ "tomorrow", "1" }, { "tmr", "1" }, { "everyday", "7" } };
	private static String[][] months = { { "january", "1" },
			{ "february", "2" }, { "march", "3" }, { "april", "4" },
			{ "may", "5" }, { "june", "6" }, { "july", "7" },
			{ "august", "8" }, { "september", "9" }, { "october", "10" },
			{ "november", "11" }, { "december", "12" }, { "jan", "1" },
			{ "feb", "2" }, { "mar", "3" }, { "apr", "4" }, { "may", "5" },
			{ "jun", "6" }, { "jul", "7" }, { "aug", "8" }, { "sep", "9" },
			{ "oct", "10" }, { "nov", "11" }, { "dec", "12" } };
	private static Logger logger = Logger.getLogger(Utility.class.getName());

	public static String removeFirstWord(String userCommand) {
		return userCommand.replace(getHead(userCommand), "").trim();
	}

	public static String getHead(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	public static EventDateTime[] stringToEventDateTime(String dateString,
			String timeString) {
		if (dateString == null) {
			dateString = "";
		}

		if (timeString == null) {
			timeString = "";
		}

		int[] time = Parser.extractTimeArray(timeString);
		int[] date = Parser.extractDateArray(dateString);

		EventDateTime startCalen = new EventDateTime();
		EventDateTime endCalen = new EventDateTime();
		EventDateTime[] startEndCalen = new EventDateTime[2];
		startEndCalen[0] = startCalen;
		startEndCalen[1] = endCalen;

		int year = -1, month = -1, day = -1, hour = -1, min = -1;
		if (time != null) {
			year = EventDateTime.getCurrentTime().get(Calendar.YEAR);
			month = EventDateTime.getCurrentTime().get(Calendar.MONTH);
			day = EventDateTime.getCurrentTime().get(Calendar.DAY_OF_MONTH);
			hour = time[0];
			min = time[1];
			startCalen.set(year, month, day, hour, min);
		}

		if (date == null) {
			if (time != null) {
				if (startCalen.compareTo(EventDateTime.getCurrentTime()) < 0) {
					day++;
					startCalen.inc(Calendar.DAY_OF_MONTH);
				}
				if (time.length > 2) {
					endCalen.set(year, month, day, time[2], time[3]);
				}
			}
			return startEndCalen;
		}

		if (date.length == 3) {
			year = (date[2] % 2000) + 2000;
		}

		if (year < EventDateTime.getCurrentTime().get(Calendar.YEAR)) {
			year = EventDateTime.getCurrentTime().get(Calendar.YEAR);
		}

		if (date.length >= 2) {
			month = date[1];
		}

		if (date.length == 1) {
			month = EventDateTime.getCurrentTime().get(Calendar.MONTH);
		}
		day = date[0];
		startCalen.set(year, month, day);

		while (startCalen.compareTo(EventDateTime.getCurrentTime()) < 0
				&& (!isToday(startCalen))) {
			if (date.length == 2) {
				startCalen.inc(Calendar.YEAR);
				year++;
			} else {
				startCalen.inc(Calendar.MONTH);
				month++;
			}
		}
		if (isToday(startCalen) && time != null && 
				(startCalen.compareTo(EventDateTime.getCurrentTime()) < 0)) {
			startCalen.inc(Calendar.DAY_OF_MONTH);
			day++;
		}
		if (time != null && time.length > 2) {
			endCalen.set(year, month, day, time[2], time[3]);
		}
		return startEndCalen;
	}

	private static boolean isToday(EventDateTime calen) {
		EventDateTime current1 = EventDateTime.getCurrentTime();
		if (calen.get(Calendar.DAY_OF_MONTH) == current1
				.get(Calendar.DAY_OF_MONTH)
				&& calen.get(Calendar.MONTH) == current1.get(Calendar.MONTH)
				&& calen.get(Calendar.YEAR) == current1.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}

	public static int stringToPriority(String eventPriorityString) {
		// TODO Auto-generated method stub
		if (eventPriorityString.equals("low")) {
			return 2;
		}
		if (eventPriorityString.equals("med")) {
			return 1;
		}
		if (eventPriorityString.equals("high")) {
			return 0;
		}
		return -1;
	}

	public static String getDaySuffix(int day) {
		String suffix = "th";
		if (day % 10 == 1) {
			suffix = "st";
		} else if (day % 10 == 2) {
			suffix = "nd";
		} else if (day % 10 == 3) {
			suffix = "rd";
		}

		if (day >= 11 && day <= 13) {
			suffix = "th";
		}
		return suffix;
	}

	public static int[] dayToArray(String dayC) {
		int[] dateArray = new int[3];
		EventDateTime calen = EventDateTime.getCurrentTime();
		int currentDayOfWeek = calen.get(Calendar.DAY_OF_WEEK);

		for (int i = 0; i < daysOfWeek.length; i++) {
			if (dayC.toLowerCase().startsWith(daysOfWeek[i])) {
				int inc = (i + 1) - currentDayOfWeek;
				if (inc < 0) {
					inc += 7;
				}
				calen.add(Calendar.DAY_OF_MONTH, inc);
			}
		}

		for (String[] day : days) {
			if (dayC.toLowerCase().equals(day[0])) {
				calen.add(Calendar.DAY_OF_MONTH, Integer.parseInt(day[1]));
			}
		}
		dateArray[2] = calen.get(Calendar.YEAR);
		dateArray[1] = calen.get(Calendar.MONTH);
		dateArray[0] = calen.get(Calendar.DAY_OF_MONTH);
		return dateArray;
	}

	public static int[] monthToArray(String group) {
		int i;
		String day = "";
		for (i = 0; i < group.length(); i++) {
			if (!Character.isDigit(group.charAt(i))) {
				break;
			}
			day += group.charAt(i);
		}

		String gMonth = "";
		group = group.toLowerCase();

		for (i = 0; i < months.length; i++) {
			if (group.contains(months[i][0])) {
				gMonth = months[i][0];
				break;
			}
		}

		String year = group.substring(group.indexOf(gMonth) + gMonth.length());
		if ("".equals(year)) {
			return Parser.extractDateArray(day + "/" + months[i][1]);
		} else {
			for (int j = 0; j < year.length(); j++) {
				if (Character.isDigit(year.charAt(j))) {
					year = year.substring(j);
					break;
				}

			}
			return Parser.extractDateArray(day + "." + months[i][1] + "."
					+ year);
		}
	}

	/**
	 * 
	 * @param original
	 * @param newFile
	 * @return boolean- true if the checkSums are verified
	 */
	public static boolean verifyChecksum(String original, String newFile) {
		String newChecksum = FileHandler.getSha1Checksum(newFile);
		logger.info("Checksum of downloaded file : " + newChecksum);
		logger.info("Checksum of server file : " + original);
		return newChecksum.equals(original);
	}

	/**
	 * 
	 * @param zipFile
	 * @return true if the updated file is replaced
	 */
	public static boolean replaceWithUpdatedFile(String zipFile) {
		if (FileHandler.unzipFile(zipFile,
				".." + System.getProperty("file.separator"))) {
			return true;
		} else {
			return false;
		}
	}

	public static int[] minutesToHour(int duration) {
		int hourMin[] = new int[2];
		if (duration != 0) {
			hourMin[0] = duration / 60;
			hourMin[1] = duration % 60;
		}

		return hourMin;
	}

	public static int millisToMins(long mill) {
		long mins = mill / 60000;
		return (int) mins;
	}
	
	public static long minsToMillis(int mill) {
		long mins = mill * 60000;
		return mins;
	}

	public static Event[] eventsOnPrevSameDay(Event event) {
		Event dateOfEvent = new Event();
		Event prevDate = new Event();
		EventDateTime checkFreeDate = event.getStart().getDate();
		dateOfEvent.setStart(checkFreeDate);

		prevDate.setStart(checkFreeDate);

		Event[] eventsOnSameDay = new Find().find(dateOfEvent);
		checkFreeDate.add(Calendar.DAY_OF_MONTH, -1);

		Event[] eventsOnPrevDay = new Find().find(prevDate);

		if (eventsOnSameDay == null && eventsOnPrevDay == null) {
			return new Event[0];
		} else if (eventsOnPrevDay == null) {
			return eventsOnSameDay;
		} else if (eventsOnSameDay == null) {
			return eventsOnPrevDay;
		} else {
			Event[] eventsPrevSameDay = new Event[eventsOnPrevDay.length
					+ eventsOnSameDay.length];
			int i = 0;
			for (Event eventPrev : eventsOnPrevDay) {
				eventsPrevSameDay[i++] = eventPrev;
			}

			for (Event eventSameDay : eventsOnSameDay) {
				eventsPrevSameDay[i++] = eventSameDay;
			}
			return eventsPrevSameDay;
		}

	}
	
	/**
	 * 
	 * @param eventToGetId
	 * @return A string containing the Id of the event passed as param
	 */
	public static String generateId() {
		String eventId = String.format("__$!%d!$__", (new Random().nextInt(Integer.MAX_VALUE)));
		return eventId;
	}
	
	public static String functionArgumentList(String... parameters) {
		StringBuilder functionArguments = new StringBuilder("");
		int i;
		for (i = 0; i < parameters.length - 1; i++) {
			functionArguments.append(parameters[i]).append(", ");
		}
		return functionArguments.append(parameters[i]).toString();
	}
}
