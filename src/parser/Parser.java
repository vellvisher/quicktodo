/**
 * This class contains functions to parse the user input and extract information that is needed from it
 * Critical to functionality
 * Uses regular expressions
 * DO NOT MODIFY if not absolutely sure
 * @author Vaarnan Drolia
 */

package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import core.Utility;
import data.Event;
import data.EventDateTime;

public class Parser
{
	private static final String[] daysOfWeek = { "monday", "tuesday",
			"wednesday", "thursday", "friday", "saturday", "sunday", "mon",
			"tue", "wed", "thu", "fri", "sat", "sun" };
	private static final String[] days = { "today", "tomorrow", "tmr" };// ,
																		// "everyday"};
	private static final String[] months = { "jan", "feb", "mar", "apr", "may",
			"jun", "jul", "aug", "sep", "oct", "nov", "dec", "january",
			"february", "march", "april", "may", "june", "july", "august",
			"september", "october", "november", "december" };
	private static final String TIME_REPRESENTATION = "((((0?[1-9])|(1[0-2]))(([:\\.][0-5][0-9])?(\\s?[AP]M)))|"
			+ // For 24hour time
			"((([01][0-9])|(2[0-3]))([:\\.][0-5][0-9])))";
	private static final String END_TIME_SEPARATOR = "((\\sto\\s)|(to)|(-))";
	private static final String START_END_TIME_REGEX = TIME_REPRESENTATION
			+ END_TIME_SEPARATOR + TIME_REPRESENTATION;

	/**
	 * Parses the string to return an event object with fields set to the
	 * correct values extracts all fields from a string and sets them correctly
	 * 
	 * @param command
	 * @return Event object
	 */
	public static Event parseEvent(String command) {
		if (extractId(command) != null) {
			Event eventWithId = new Event();
			eventWithId.setId(extractId(command));
			return eventWithId;
		}
		String[] escapedParts = extractEscapedParts(command);
		command = escapedParts[0];
		boolean deadlineEvent = false;
		String[] eventDetails = {
				null, // name
				extractDate(command), extractTime(command),
				extractDuration(command), extractPriority(command), "default" };
		List<String> labelsList = extractLabels(command);
		if (eventDetails[3] == null) {
			eventDetails[3] = "";
		}
		if (eventDetails[4] == null) {
			eventDetails[4] = "-1";
		}
		eventDetails[0] = extractName(command, eventDetails, labelsList);
		for (int i = 1; i < escapedParts.length; i++) {
			eventDetails[0] = eventDetails[0].replaceFirst("(\\$_\\$)",
					escapedParts[i]);
		}

		if (eventDetails[1] != null
				&& command.contains("by " + eventDetails[1])) {
			deadlineEvent = true;
		}
		Event e;
		EventDateTime[] startEndTimes = Utility.stringToEventDateTime(
				eventDetails[1], eventDetails[2]);
		e = new Event(eventDetails[0], startEndTimes[0], startEndTimes[1],
				extractDurationInMinutes(eventDetails[3]),
				Integer.parseInt(eventDetails[4]), labelsList, eventDetails[5]);

		e.setDeadlineEvent(deadlineEvent);

		if (command.startsWith("*")) {
			e.setStarred(true);
			if (e.getName() != null && !e.getName().equals("")) {
				e.setName(e.getName().substring(1));
			}
		}

		return e;// eventDetails;
	}

	/**
	 * 
	 * @param command
	 * @return boolean- true if input specifies an end time
	 */
	public static String[] extractEscapedParts(String command) {
		Pattern doubleQuotes = Pattern.compile("\"[^\"]+\"");
		Matcher doubleQuotesMatcher = doubleQuotes.matcher(command);
		ArrayList<String> escapedParts = new ArrayList<String>();
		escapedParts.add(command);
		while (doubleQuotesMatcher.find()) {
			escapedParts.add(doubleQuotesMatcher.group());
			command = command.replace(doubleQuotesMatcher.group(), "$_$");
		}
		escapedParts.set(0, command);
		return (String[]) escapedParts.toArray(new String[escapedParts.size()]);
	}

	/**
	 * Extracts the id from a user string
	 * 
	 * @param command
	 * @return
	 */
	public static String extractId(String command) {
		Pattern p = Pattern.compile("(__\\$!\\d+!\\$__)");
		Matcher m = p.matcher(command);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	/**
	 * Extracts multiple ids
	 * 
	 * @param command
	 * @return
	 */
	public static String[] extractIds(String command) {
		Pattern p = Pattern.compile("(__\\$!\\d+!\\$__)");
		Matcher m = p.matcher(command);
		if (m.find()) {
			ArrayList<String> matches = new ArrayList<String>();
			matches.add(m.group());
			while (m.find()) {
				matches.add(m.group());
			}
			return (String[]) matches.toArray(new String[matches.size()]);
		}
		return null;
	}

	/**
	 * Extracts only an event name
	 * 
	 * @param command
	 * @param eventDetails
	 * @param labels
	 * @return
	 */
	private static String extractName(String command, String[] eventDetails,
			List<String> labels) {
		String eventName = command;
		if (labels != null) {

			for (String label : labels) {
				eventName = eventName.replace("@" + label, "");
			}
		}

		if (eventDetails[1] != null) {
			eventName = eventName.replace("on " + eventDetails[1], "");
			eventName = eventName.replace("by " + eventDetails[1], "");
			eventName = eventName.replace(eventDetails[1], "");
		}
		if (eventDetails[2] != null && !"".equals(eventDetails[2])) {
			eventName = eventName.replace("at " + eventDetails[2], "");
			eventName = eventName.replace(eventDetails[2], "");
		}
		if (eventDetails[3] != null && !"".equals(eventDetails[3])) {
			eventName = eventName.replace("for " + eventDetails[3], "");
			eventName = eventName.replace(eventDetails[3], "");
		}
		if (eventDetails[4] != null) {
			eventName = eventName.replace(eventDetails[4], "");
		}
		// if (eventDetails[5] != null) {
		// eventName = eventName.replace("#" + eventDetails[5], "");
		// }
		Pattern regExtraWhiteSpace = Pattern.compile("\\s{2}");
		Matcher regExtraWhiteSpaceMatcher = regExtraWhiteSpace
				.matcher(eventName);

		eventName = regExtraWhiteSpaceMatcher.replaceAll(" ").trim();
		return eventName;
	}

	/**
	 * Extracts duration from a string
	 * 
	 * @param command
	 * @return
	 */
	public static String extractDuration(String command) {
		Pattern durationPattern = Pattern
				.compile(
						"(\\b((for)\\s+)?(((((1?[1-9])|(10)|(2[0-4]))((\\s+)?((hrs?)|(hours?))))((\\s+)(([1-5][0-9])|[0-9])(\\s+)?(min(ute)?s?)))|"
								+ "((((1?[1-9])|(10)|(2[0-4]))((\\s+)?((hrs?)|(hours?))))|((\\s+)?(([1-5][0-9])|[0-9])(\\s+)?(min(ute)?s?)))))",
						Pattern.CASE_INSENSITIVE);
		Matcher durationMatcher = durationPattern.matcher(command);
		if (durationMatcher.find()) {
			return durationMatcher.group().trim();
		} else {
			return null;
		}
	}

	// TODO Remove
	public static String extractBy(String command) {
		Pattern p = Pattern.compile("\\b(by)\\s[\\S]+"); // get all strings with
															// " by xxx"
		Matcher m = p.matcher(command);
		if (m.find()) {
			return m.group().substring(3);
		}
		return null;
	}

	// TODO Replace with more generic
	public static String extractDateAfter(String command) {
		String by = "by";
		String on = "on";
		String dateBy = null;
		String dateOn = null;

		Pattern pBy = Pattern.compile("\\b(" + by + ")\\s[\\S]+");
		Pattern pOn = Pattern.compile("\\b(" + on + ")\\s[\\S]+");

		Matcher mBy = pBy.matcher(command);
		while (mBy.find()) {
			if (isDate(mBy.group().substring(3))) {
				dateBy = mBy.group();
			}
		}

		Matcher mOn = pOn.matcher(command);
		while (mOn.find()) {
			if (isDate(mOn.group().substring(3))) {
				dateOn = mOn.group();
			}
		}

		if (!(dateBy == null ^ dateOn == null))
			return null;
		if (dateBy != null)
			return dateBy;
		return dateOn;
	}

	// TODO Replace with more generic
	public static String extractByDate(String command) {
		Pattern p = Pattern.compile("\\b(by)\\s[\\S]+");
		Matcher m = p.matcher(command);
		while (m.find()) {
			if (isDate(m.group().substring(3))) {
				return m.group();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param param
	 * @return boolean- true if param is a date
	 */
	private static boolean isDate(String param) {
		for (String day : days) {
			if (day.equalsIgnoreCase(param))
				return true;
		}

		for (String dayOfWeek : daysOfWeek) {
			if (dayOfWeek.equalsIgnoreCase(param))
				return true;
		}

		// check if it is a valid date
		if (isDayNumber(param)) {
			return true;
		}

		if (isDateString(param))
			return true;
		return false;
	}

	/**
	 * 
	 * @param param
	 * @return boolean- true if param is a string that contains a date
	 */
	private static boolean isDateString(String param) {
		// TODO Fix reg ex
		Pattern datePat = Pattern
				.compile("^(\\d{1,2}[-\\.\\\\\\/]){1,2}\\d{1,4}$"); // is
																	// xx.xx.xxxx
		// or xx.xx.xxx xx.xx.xx xx.xx.xxxx xx.xx xx.xxx xx.xxxx
		Matcher matcher = datePat.matcher(param);
		return matcher.find();
	}

	private static boolean correctDatePostfix(int n, String post) {
		if (post == null || post.trim() == "") {
			return true;
		}
		if (n % 10 >= 1 && n % 10 <= 3 && (n < 11 || n > 13)) {
			if (n % 10 == 1) {
				return post.equals("st");
			} else if (n % 10 == 2) {
				return post.equals("nd");
			} else if (n % 10 == 3) {
				return post.equals("rd");
			}
		}
		return post.equals("th");
	}

	private static boolean isDayNumber(String param) {
		Pattern digits = Pattern.compile("^\\d{1,2}");
		Matcher match = digits.matcher(param);
		if (match.find()) {
			int i = Integer.parseInt(match.group());
			if (i < 1 || i > 31) {
				return false;
			}
			Pattern post = Pattern.compile("[snrt][tdh]$");
			match = post.matcher(param);
			String postfix = null;
			if (match.find()) {
				postfix = match.group();
			}
			return correctDatePostfix(i, postfix);
		}
		return false;
	}

	public static List<String> extractLabels(String command) {
		Pattern labelRegEx = Pattern.compile("@[.[^@#]]+");
		Matcher labelMatcher = labelRegEx.matcher(command);
		ArrayList<String> labels = new ArrayList<String>();
		while (labelMatcher.find()) {
			labels.add(labelMatcher.group().trim().substring(1));
		}
		if (labels.size() != 0)
			return labels;
		return null;
	}

	public static String extractList(String command) {
		Pattern listRegEx = Pattern.compile("#[.[^@#]]+");
		Matcher listMatcher = listRegEx.matcher(command);
		if (listMatcher.find()) {
			return listMatcher.group().trim().substring(1);
		}
		return null;
	}

	public static String extractTime(String command) {
		Pattern pStartEnd = Pattern.compile("(\\b" + START_END_TIME_REGEX
				+ "\\b)", Pattern.CASE_INSENSITIVE);
		Pattern pStartOnly = Pattern.compile("(" + TIME_REPRESENTATION
				+ ")", Pattern.CASE_INSENSITIVE);
		Matcher m = pStartEnd.matcher(command);
		if (m.find()) {
			return m.group();
		} else {
			m = pStartOnly.matcher(command);
			if (m.find()) {
				return m.group();
			}
		}
		return null;
	}

	public static String extractTimeAfterAt(String command) {
		Pattern p = Pattern.compile("\\b(at)\\s[\\S]+");
		Matcher m = p.matcher(command);
		while (m.find()) {
			if (isTime(m.group().substring(3))) {
				return m.group().substring(3);
			}
		}
		return null;
	}

	private static boolean isTime(String param) {
		// FIXME Fix reg-exp to do time correction
		Pattern p = Pattern.compile("^\\d{1,2}((:\\d\\d)|(am)|(pm))");
		Matcher m = p.matcher(param);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static String extractPriority(String command) {
		Pattern p = Pattern.compile("\\b(imp)\\s[\\S]+");
		Matcher m = p.matcher(command);
		while (m.find()) {
			if (isPriority(m.group().substring(4))) {
				return m.group().substring(4);
			}
		}
		return null;
	}

	private static boolean isPriority(String param) {
		Pattern p = Pattern.compile("(high)|(med)|(low)");
		Matcher m = p.matcher(param);
		if (m.find()) {
			return true;
		}
		return false;
	}
	
	public static String extractDate(String command) {
		Pattern dateRegEx;
		Matcher dateRegMatcher;

		// FIXME Complex
		for (String day : days) {
			dateRegEx = Pattern.compile("\\b" + day + "\\b",
					Pattern.CASE_INSENSITIVE);
			dateRegMatcher = dateRegEx.matcher(command);
			if (dateRegMatcher.find()) {
				return dateRegMatcher.group();
			}
		}

		// FIXME Complex
		for (String dayOfWeek : daysOfWeek) {
			dateRegEx = Pattern.compile("\\b" + dayOfWeek + "\\b",
					Pattern.CASE_INSENSITIVE);
			dateRegMatcher = dateRegEx.matcher(command);
			if (dateRegMatcher.find()) {
				return dateRegMatcher.group();
			}
		}

		String matchYear = "((20)?1[1-9])";
		String matchDayWithPrefix = "\\b(([23]?1st)|(2?2nd)|(2?3rd)|([12]?[4-9]th)|(1[1-3]th)|([123]0th))([,\\s-]|(, ))";
		// FIXME Complex
		for (String month : months) {
			dateRegEx = Pattern.compile(matchDayWithPrefix + month
					+ "(([,\\s-]|(, ))" + matchYear + ")?\\b",
					Pattern.CASE_INSENSITIVE);
			dateRegMatcher = dateRegEx.matcher(command);
			if (dateRegMatcher.find()) {
				return dateRegMatcher.group();
			}
		}
		String dateSep = "[-\\.\\\\\\/]";
		dateRegEx = Pattern
				.compile(
						"(^|\\b)(((([012]?[1-9])|([1-3]0)|(31))"
								+ dateSep
								+ "(([0]?[1-9])|(1[0-2]))("
								+ dateSep
								+ matchYear
								+ ")?)|"
								+ "(([23]?1st)|(2?2nd)|(2?3rd)|([12]?[4-9]th)|(1[1-3]th)|([123]0th)))($|\\b)",
						Pattern.CASE_INSENSITIVE);
		dateRegMatcher = dateRegEx.matcher(command);
		if (dateRegMatcher.find()) {
			return dateRegMatcher.group();
		}
		return null;
	}
	
	public static int[] extractDateArray(String command) {
		Pattern dateRegEx;
		Matcher dateRegMatcher;

		// FIXME Complex
		for (String day : days) {
			dateRegEx = Pattern.compile("\\b" + day + "\\b",
					Pattern.CASE_INSENSITIVE);
			dateRegMatcher = dateRegEx.matcher(command);
			if (dateRegMatcher.find()) {
				return Utility.dayToArray(dateRegMatcher.group());
			}
		}

		// FIXME Complex
		for (String dayOfWeek : daysOfWeek) {
			dateRegEx = Pattern.compile("\\b" + dayOfWeek + "\\b",
					Pattern.CASE_INSENSITIVE);
			dateRegMatcher = dateRegEx.matcher(command);
			if (dateRegMatcher.find()) {
				return Utility.dayToArray(dateRegMatcher.group());
			}
		}

		String matchYear = "((20)?1[1-9])";
		String matchDayWithPrefix = "\\b(([23]?1st)|(2?2nd)|(2?3rd)|([12]?[4-9]th)|(1[1-3]th)|([123]0th))([,\\s-]|(, ))";
		// FIXME Complex
		for (String month : months) {
			dateRegEx = Pattern.compile(matchDayWithPrefix + month
					+ "(([,\\s-]|(, ))" + matchYear + ")?\\b",
					Pattern.CASE_INSENSITIVE);
			dateRegMatcher = dateRegEx.matcher(command);
			if (dateRegMatcher.find()) {
				return Utility.monthToArray(dateRegMatcher.group());
			}
		}
		String dateSep = "[-\\.\\\\\\/]";
		dateRegEx = Pattern
				.compile(
						"(^|\\s)(((([012]?[1-9])|([1-3]0)|(31))"
								+ dateSep
								+ "(([0]?[1-9])|(1[0-2]))("
								+ dateSep
								+ matchYear
								+ ")?)|"
								+ "(([23]?1st)|(2?2nd)|(2?3rd)|([12]?[4-9]th)|(1[1-3]th)|([123]0th)))($|\\s)",
						Pattern.CASE_INSENSITIVE);
		dateRegMatcher = dateRegEx.matcher(command);
		if (!dateRegMatcher.find()) {
			return null;
		}

		String date = dateRegMatcher.group().trim();
		Pattern dateTokenized = Pattern.compile(dateSep);
		String[] dateTokenizedArray = dateTokenized.split(date);

		if (dateTokenizedArray.length == 1) {
			int day = Integer.parseInt(dateTokenizedArray[0].substring(0,
					dateTokenizedArray[0].length() - 2));
			return new int[] { day };
		}

		int[] dateIntArray = new int[dateTokenizedArray.length];
		for (int i = 0; i < dateTokenizedArray.length; i++) {
			dateIntArray[i] = Integer.parseInt(dateTokenizedArray[i]);
		}
		return dateIntArray;
	}

	// time = new String[]{"4pm", "20:21", "13:10", "05AM", "5:10pm",
	// "12:10 am"};
	// timeInt = new int[][]{ {16,0}, {20,21}, {13,10}, {5,0}, {17,10},
	// {12,10}};

	public static int[] extractTimeArray(String command) {
		Pattern p = Pattern.compile(START_END_TIME_REGEX,
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(command);
		if (m.find()) {
			p = Pattern.compile(END_TIME_SEPARATOR);
			String[] times = p.split(command);
			int[] startEndTime = new int[4];
			int[] tempTime = extractTimeArray(times[0]);
			startEndTime[0] = tempTime[0];
			startEndTime[1] = tempTime[1];

			tempTime = extractTimeArray(times[1]);
			startEndTime[2] = tempTime[0];
			startEndTime[3] = tempTime[1];
			if (startEndTime[2] < startEndTime[0]
					|| (startEndTime[2] == startEndTime[0] && startEndTime[3] < startEndTime[1])) {
				return null;
			}

			return startEndTime;
		}

		p = Pattern.compile(TIME_REPRESENTATION, Pattern.CASE_INSENSITIVE);
		m = p.matcher(command);

		if (!m.find()) {
			return null;
		}
		String time = m.group().trim().replace(" ", "");
		Pattern timeTokenized = Pattern.compile("[:\\.]");
		String[] timeArray = timeTokenized.split(time);
		int hour, min;
		if (timeArray.length == 1) {
			hour = Integer.parseInt(timeArray[0].substring(0,
					timeArray[0].length() - 2));
			if (hour == 12) {
				hour -= 12;
			}
			if (timeArray[0].toLowerCase().contains("am")) {
				return new int[] { hour, 0 };
			} else {
				return new int[] { hour + 12, 0 };
			}
		} else {
			hour = Integer.parseInt(timeArray[0]);
			if (hour > 12) {
				min = Integer.parseInt(timeArray[1]);
			} else {
				min = Integer.parseInt(timeArray[1].substring(0, 2));
				if (timeArray[1].toLowerCase().contains("pm")) {
					hour += 12;

					if (hour == 24) {
						hour = 12;
					}
				}
			}
		}
		return new int[] { hour, min };
	}

	public static boolean validGoogleUsername(String username) {
		if (username == null || username.length() < 6 || username.length() > 30) {
			return false;
		}

		Pattern p = Pattern.compile("[a-z0-9A-Z\\.@]+");
		Matcher m = p.matcher(username);

		if (m.matches() && m.group(0).equals(username)) {
			return true;
		}
		return false;
	}

	public static boolean validGooglePassword(String password) {
		return password != null && password.length() >= 1;
	}

	public static int extractDurationInMinutes(String line) {
		String duration = extractDuration(line);
		if (duration == null || "".equals(duration)) {
			return 0;
		}
		int finalDurationMinutes = 0;
		duration = duration.trim();
		Matcher hourMatcher = Pattern.compile(
				"(((1?[1-9])|(10)|(2[0-4]))((\\s+)?((hrs?)|(hours?))))")
				.matcher(duration);
		if (hourMatcher.find()) {
			finalDurationMinutes += 60 * Integer.parseInt(hourMatcher.group()
					.split("((\\s+)?((hrs?)|(hours?)))")[0].trim());
		}

		Matcher minMatcher = Pattern.compile(
				"((\\s+)?(([1-5][0-9])|[0-9])(\\s+)?(min(ute)?s?))").matcher(
				duration);
		if (minMatcher.find()) {
			finalDurationMinutes += Integer.parseInt(minMatcher.group().split(
					"(\\s+)?(min(ute)?s?)")[0].trim());
		}

		// if
		// (duration.matches("((\\s+)?(([1-5][0-9])|[0-9])\\s+(min(ute)?s?))"))
		// {
		// finalDurationMinutes +=
		// Integer.parseInt(duration.split("((\\s+)?(([1-5][0-9])|[0-9])\\s+(min(ute)?s?))"))
		// }
		return finalDurationMinutes;
	}
}
