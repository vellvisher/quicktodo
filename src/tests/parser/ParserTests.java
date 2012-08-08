package tests.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.Event;

import parser.Parser;

public class ParserTests
{
	private String[] commands;
	private String[] testDays;
	private String[] testDayPrefixes;
	private String[] testMonths;
	private String[] priority;
	private String[] time;
	private String[] endTime;
	private String[] timeCombiner;
	private int[][] timeInt;

	@Before
	public void setUp() throws Exception {

		testDays = new String[] { "monday", "tuesday", "wednesday", "thursday",
				"friday", "saturday", "sunday", "mon", "tue", "wed", "thu",
				"fri", "sat", "sun", "today", "tomorrow", "tmr", "1st", "2nd",
				"3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
				"11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th",
				"19th", "20th", "21st", "22nd", "23rd", "24th", "25th", "26th",
				"27th", "28th", "29th", "30th", "31st", "28/8/2011", "28/8",
				"08/01", "28.10.11", "8/12", "31-12-2013" };
		testDayPrefixes = new String[] { "1st", "2nd", "3rd", "4th", "5th",
				"6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th",
				"14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st",
				"22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th",
				"30th", "31st" };

		testMonths = new String[] { "jan", "feb", "mar", "apr", "may", "jun",
				"jul", "aug", "sep", "oct", "nov", "dec", "January",
				"february", "March", "april", "may", "june", "july", "august",
				"september", "october", "november", "December" };
		priority = new String[] { "high", "med", "low" };
		time = new String[] { "4pm", "20.21", "13:10", "05AM", "5.10pm",
				"12:10 am", "01:20"};
		endTime = new String[] { "5:30pm", "23:21", "15:10", "08AM", "5:30pm",
				"12:15 am", "02:30"};
		timeInt = new int[][] { { 16, 0 }, { 20, 21 }, { 13, 10 }, { 5, 0 },
				{ 17, 10 }, { 12, 10 } , {1, 20}};
		timeCombiner = new String[] { "-", " to " };
		commands = new String[] { ":TIME: buy joe's gift",
				"buy joe's :TIME: gift imp :IMP: by :DATE:",
				"buy joe's gift by :DATE: imp :IMP: @shopping @personal",
				"get dress from tailor on :DATE: at :TIME:",
				"get dress from tailor :DATE: :TIME:",
				"get dress from tailor on :DATE: in #Work",
				"get dress from tailor :TIME: by :DATE: imp :IMP:",
				"get dress from tailor by :DATE: :TIME: imp :IMP:",
				"get dress from tailor by :DATE: imp :IMP:",
				"watch football game on :DATE: at :TIME: for 1 hour",
		// "do homework tomorrow",
		// "do homework day after",
		// "do homework next week",
		// "do hw on :DATE: and :DATE:",
		// "do hw everyday",
		// "do hw every mon,wed,fri",
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExtractBy() {
		for (String comm : commands) {
			if (comm.indexOf("by :DATE:") != -1) {
				assertEquals(":DATE:", Parser.extractBy(comm));
			}
		}
	}

	@Test
	public void testExtractByDate() {
		for (String comm : commands) {
			if (comm.indexOf("by :DATE:") != -1) {
				for (String day : testDays)
					assertEquals("by " + day,
							Parser.extractByDate(comm.replace(":DATE:", day)));
			}
		}
		assertNull(Parser
				.extractByDate("comfortable numb by pink floyd on friday"));
	}

	@Test
	public void testExtractTime() {
		for (String comm : commands) {
			if (comm.indexOf(":TIME:") != -1) {
				for (int i = 0; i < time.length; i++) {
					assertEquals(time[i],
							Parser.extractTime(comm.replace(":TIME:", time[i])));
					for (String combiner : timeCombiner) {
						String startEndTime = time[i] + combiner + endTime[i];
						assertEquals(startEndTime, Parser.extractTime(comm
								.replace(":TIME:", startEndTime)));
					}
				}

			} else {
				assertNull(Parser.extractTime(comm));
			}
		}
		assertEquals("12:20 am",
				Parser.extractTime("meeting 12:20 am with supplier"));
		assertNull(Parser.extractTime("just for fun"));
	}

	// TODO Add generic testing for time like Date
	// @Test-OLD
	public void testExtractTimeAfterAt() {
		for (String comm : commands) {
			if (comm.indexOf(":TIME:") != -1) {
				for (String t : time) {
					assertEquals(t, Parser.extractTimeAfterAt(comm.replace(
							":TIME:", t)));
				}
			} else {
				assertNull(Parser.extractTimeAfterAt(comm));
			}
		}
	}

	// TODO Add generic testing for time like Date
	@Test
	public void testExtractPriority() {
		for (String comm : commands) {
			if (comm.indexOf(":IMP:") != -1) {
				for (String p : priority) {
					assertEquals(p,
							Parser.extractPriority(comm.replace(":IMP:", p)));
				}
			} else {
				assertNull(Parser.extractPriority(comm));
			}
		}
	}

	@Test
	public void testExtractDate() {
		for (String comm : commands) {
			if (comm.indexOf(":DATE:") != -1) {
				for (String day : testDays) {
					assertEquals(day,
							Parser.extractDate(comm.replace(":DATE:", day)));
				}
			} else {
				assertNull(Parser.extractDate(comm));
			}
		}
		assertEquals("friday",
				Parser.extractDate("comfortably numb by pink floyd on friday"));
		// TODO Following case works?
		assertEquals("today", Parser.extractDate("switch light on by today"));
		// TODO Implement part below
		// assertEquals("everyday",Parser.extractDate("switch light everyday"));
		// assertNull(Parser.extractDate("comfortable numb by thursday on friday"));
		// assertNull(Parser.extractDate("comfortable numb by thursday on 1st"));
		// assertNull(Parser.extractDate("comfortable numb by thursday on friday"));
		assertNull(Parser.extractDate("comfortable numb by 0th"));
		assertNull(Parser.extractDate("comfortable numb on 32nd"));
		assertNull(Parser.extractDate("comfortable numb on 42"));

		for (String comm : commands) {
			if (comm.indexOf(":DATE:") != -1) {
				for (String prefix : testDayPrefixes) {
					for (String month : testMonths) {
						assertEquals(prefix + " " + month,
								Parser.extractDate(comm.replace(":DATE:",
										prefix + " " + month)));
						assertEquals(prefix + "-" + month,
								Parser.extractDate(comm.replace(":DATE:",
										prefix + "-" + month)));
						assertEquals(prefix + "," + month,
								Parser.extractDate(comm.replace(":DATE:",
										prefix + "," + month)));
						assertEquals(prefix + ", " + month,
								Parser.extractDate(comm.replace(":DATE:",
										prefix + ", " + month)));
					}
				}
			} else {
				assertNull(Parser.extractDate(comm));
			}

		}
		assertEquals("24th Nov, 2012", Parser.extractDate("get my income tax return on 24th Nov, 2012"));
		assertEquals("24th dec, 2011", Parser.extractDate("get my income tax return on 24th dec, 2011"));
		assertEquals("24th jan, 15", Parser.extractDate("get my income tax return on 24th jan, 15"));
		assertEquals("19-11-2011", Parser.extractDate("this event will get edited  19-11-2011 17:31"));
	}

	@Test
	public void testExtractDateAfter() {
		for (String comm : commands) {
			if (comm.indexOf("by :DATE:") != -1) {
				for (String day : testDays) {
					assertEquals("by " + day, Parser.extractDateAfter(comm
							.replace(":DATE:", day)));
				}
			} else if (comm.indexOf("on :DATE:") != -1) {
				for (String day : testDays) {
					assertEquals("on " + day, Parser.extractDateAfter(comm
							.replace(":DATE:", day)));
				}
			} else {
				assertNull(Parser.extractDateAfter(comm));
			}
		}
		assertEquals(
				"on friday",
				Parser.extractDateAfter("comfortably numb by pink floyd on friday"));
		// TODO Following case works?
		assertEquals("by today",
				Parser.extractDateAfter("switch light on by today"));
		// TODO Implement part below
		// assertEquals("everyday",Parser.extractDate("switch light everyday"));
		assertNull(Parser
				.extractDateAfter("comfortable numb by thursday on friday"));
		assertNull(Parser
				.extractDateAfter("comfortable numb by thursday on 1st"));
		assertNull(Parser
				.extractDateAfter("comfortable numb by thursday on friday"));
		assertNull(Parser.extractDateAfter("comfortable numb by 0th"));
		assertNull(Parser.extractDateAfter("comfortable numb on 32nd"));
		assertNull(Parser.extractDateAfter("comfortable numb on 42"));
	}

	@Test
	public void testExtractLabels() {

		String[] tests = { "buy joe's gift by :DATE: @work",
				"buy joe's gift by :DATE: @shopping @personal stuff #goodList",
				"get dress from tailor on :DATE: at 3pm" };
		ArrayList<ArrayList<String>> labels = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("work");
		labels.add(temp);
		temp = null;
		temp = new ArrayList<String>();
		temp.add("shopping");
		temp.add("personal stuff");
		labels.add(temp);
		for (int i = 0; i < 2; i++) {
			assertEquals(labels.get(i), Parser.extractLabels(tests[i]));
		}
		assertNull(Parser.extractLabels(tests[2]));
	}

	@Test
	public void testExtractList() {

		String[] tests = { "buy joe's gift by :DATE: #work",
				"buy joe's gift by :DATE: @shopping @personal #good List",
				"get dress from tailor on :DATE: at 3pm" };
		String[] lists = { "work", "good List", null };
		for (int i = 0; i < tests.length; i++) {
			assertEquals(lists[i], Parser.extractList(tests[i]));
		}
	}

	@Test
	public void testParseEvent() {
		String command = "buy joe's gift by 25th #work";
		String[] result = { "buy joe's gift", "25th", null, null, null, "#work" };
		// assertArrayEquals(result, Parser.parseEvent(command));
	}

	@Test
	public void testParseStar() {
		Event event = Parser.parseEvent("*");
		assertTrue(event.getStarred());
		Event event2 = Parser.parseEvent("*abc on 5th");
		assertTrue(event2.getStarred());
		Event event3 = Parser.parseEvent("\"*abc\" on 5th");
		assertFalse(event3.getStarred());
	}

	@Test
	public void testExtractDateArray() {
		int[] test1 = { 28, 8, 2011 };
		assertArrayEquals(test1, Parser.extractDateArray("joe get 28/8/2011"));

		int[] test2 = { 28, 8 };
		assertArrayEquals(test2, Parser.extractDateArray("joe get 28/8"));

		int[] test3 = { 8, 1 };
		assertArrayEquals(test3, Parser.extractDateArray("joe get 08.01"));

		int[] test4 = { 28, 10, 11 };
		assertArrayEquals(test4, Parser.extractDateArray("joe get 28.10.11"));

		int[] test5 = { 8, 12 };
		assertArrayEquals(test5, Parser.extractDateArray("joe get 8/12"));

		int[] test6 = { 31, 12, 2013 };
		assertArrayEquals(test6, Parser.extractDateArray("joe get 31-12-2013"));

		int[] test7 = { 1 };
		assertArrayEquals(test7, Parser.extractDateArray("joe get 1st"));

		int[] test8 = { 2 };
		assertArrayEquals(test8, Parser.extractDateArray("joe get 2nd meal"));

		int[] test9 = { 3 };
		assertArrayEquals(test9, Parser.extractDateArray("joe get 3rd happy"));

		int[] test10 = { 15 };
		assertArrayEquals(test10, Parser.extractDateArray("joe get 15th guy"));

		int[] test11 = { 15, 3 };
		assertArrayEquals(test11, Parser.extractDateArray("joe get 15th March"));

		int[] test12 = { 10, 3, 2011 };
		assertArrayEquals(test12,
				Parser.extractDateArray("joe get 10th-mar-2011"));

		int[] test13 = { 1, 3, 11 };
		assertArrayEquals(test13, Parser.extractDateArray("joe get 1st,Mar,11"));

		int[] test14 = { 5, 3, 11 };
		assertArrayEquals(test14,
				Parser.extractDateArray("joe get 5th-March 11"));

		int[] test15 = { 15, 3, 2011 };
		assertArrayEquals(test15,
				Parser.extractDateArray("joe get 15th-March, 2011"));

	}

	@Test
	public void testExtractTimeArray() {
		String test1 = "joe get :TIME: go man @haha";
		for (int i = 0; i < time.length; i++) {
			assertArrayEquals(timeInt[i],
					Parser.extractTimeArray(test1.replace(":TIME:", time[i])));
		}
	}

	@Test
	public void testExtractId() {
		String test = "this is the __$!123456!$__ id";
		assertEquals("__$!123456!$__", Parser.extractId(test));

		String test2 = "  __$!123456!$__  ";
		assertEquals("__$!123456!$__", Parser.extractId(test2));

		String test3 = "__$!123456!$__";
		assertEquals("__$!123456!$__", Parser.extractId(test3));

		String test4 = "__$!-123456!$__";
		assertNull("__$!123456!$__", Parser.extractId(test4));

		String test5 = "__$!123456!$_";
		assertNull("__$!123456!$__", Parser.extractId(test5));

	}

	@Test
	public void testExtractIds() {
		String test = "__$!123456!$__ this __$!5679!$__ is the __$!222!$__ id __$!453!$__";
		assertEquals(4, Parser.extractIds(test).length);

		String test2 = "  __$!123456!$__  ";
		assertEquals("__$!123456!$__", Parser.extractIds(test2)[0]);

		String test3 = "__$!123456!$__";
		assertEquals("__$!123456!$__", Parser.extractIds(test3)[0]);

		String test4 = "__$!-123456!$__";
		assertNull("__$!123456!$__", Parser.extractIds(test4));

		String test5 = "__$!123456!$_";
		assertNull("__$!123456!$__", Parser.extractIds(test5));

	}

	@Test
	public void testValidGoogleUsername() {
		assertFalse(Parser.validGoogleUsername(null));
		assertFalse(Parser.validGoogleUsername(""));
		assertFalse(Parser.validGoogleUsername("abc"));
		assertFalse(Parser.validGoogleUsername("abcef"));
		assertTrue(Parser.validGoogleUsername("abcdef"));

		assertFalse(Parser.validGoogleUsername("abc;afd"));
		assertTrue(Parser.validGoogleUsername("abc.afd"));

		assertTrue(Parser.validGoogleUsername("aBc123afD"));

		assertTrue(Parser.validGoogleUsername("aBc123afD@asfd"));
		assertTrue(Parser.validGoogleUsername("aBc123afD@jlkgads.com"));

	}

	@Test
	public void testValidGooglePassword() {
		assertFalse(Parser.validGooglePassword(null));
		assertFalse(Parser.validGooglePassword(""));
		assertTrue(Parser.validGooglePassword("abc"));
		assertTrue(Parser.validGooglePassword("abcef"));
		assertTrue(Parser.validGooglePassword("abcdefr2"));

		assertTrue(Parser.validGooglePassword("abc;afdz"));
		assertTrue(Parser.validGooglePassword("abc.afdmn"));

		assertTrue(Parser.validGooglePassword("aBc123afD"));
	}

	@Test
	public void testExtractEscapedParts() {
		String[] result = Parser
				.extractEscapedParts("\"we\"s hall over\"come\" on 4th of july \"21st\"");

		assertArrayEquals(new String[] {
				"$_$s hall over$_$ on 4th of july $_$", "\"we\"", "\"come\"",
				"\"21st\"" }, result);
	}

	@Test
	public void testExtractDuration() {
		// append for
		String[] durationTests = { "2 hrs", "12 hrs", "1 hr", "3 hour",
				"20 mins", "45min", "20 minutes", "59 minute", "1 min",
				"1 hr 15 min", "10 hrs 2 mins", "13 hours", "23hrs" };
		// assertEquals("for 2 hrs 12 mins",
		// Parser.extractDuration("my event for today for 2 hrs 12 mins"));
		for (int i = 0; i < durationTests.length; i++) {
			assertEquals(
					durationTests[i],
					Parser.extractDuration("my event for today :TIME: to somewhere"
							.replace(":TIME:", durationTests[i])));
			assertEquals(
					"for " + durationTests[i],
					Parser.extractDuration("my event for today for :TIME: to somewhere"
							.replace(":TIME:", durationTests[i])));
		}
	}

	@Test
	public void testExtractDurationInMinutes() {
		String[] durationTests = { "2 hrs", "12 hrs", "1 hr", "3 hour",
				"20 mins", "45min", "20 minutes", "59 minute", "1 min",
				"1 hr 15 min", "10 hrs 2 mins", "13 hours", "23hours" };
		int[] durationMinutes = { 120, 720, 60, 180, 20, 45, 20, 59, 1, 75,
				602, 780, 1380 };
		for (int i = 0; i < durationTests.length; i++) {
			assertEquals(
					durationMinutes[i],
					Parser.extractDurationInMinutes("my event for today :TIME: to somewhere"
							.replace(":TIME:", durationTests[i])));
			assertEquals(
					durationMinutes[i],
					Parser.extractDurationInMinutes("my event for today for :TIME: to somewhere"
							.replace(":TIME:", durationTests[i])));
		}
	}
}
