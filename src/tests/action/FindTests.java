package tests.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import action.Add;
import action.Find;

import data.Event;

import parser.Parser;
import storage.DataManager;

public class FindTests
{

	private static Add addTestEvent = new Add();
	// Add addTestEvent2 = new Add();
	// Add addTestEvent3 = new Add();

	Find findTestEvent = new Find();
	private static ArrayList<Event> testCase1;
	private static ArrayList<Event> testCase2;
	private static ArrayList<Event> testCase3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DataManager.loadLists();

		testCase1 = new ArrayList<Event>();
		Event[] result = addTestEvent
				.execute("add meeting 05.12.2011 05:30pm #Harsha");
		Collections.addAll(testCase1, result);
		testCase2 = new ArrayList<Event>();
		result = addTestEvent
				.execute("add Big Boss meeting 05.12.2011 07:30pm #Harsha");
		Collections.addAll(testCase2, result);
		testCase3 = new ArrayList<Event>();
		result = addTestEvent
				.execute("add Harsha birthday 16.10.2011 #Poornima");
		Collections.addAll(testCase3, result);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFind() {

		ArrayList<Event> findEvent0 = new ArrayList<Event>();
		Collections.addAll(findEvent0, findTestEvent.execute("05-12"));

		assertEquals(findEvent0.indexOf(testCase1.get(0)),
				findEvent0.lastIndexOf(testCase1.get(0)));
		assertEquals(findEvent0.indexOf(testCase2.get(0)),
				findEvent0.lastIndexOf(testCase2.get(0)));
		assertEquals(-1, findEvent0.indexOf(testCase3.get(0)));

		ArrayList<Event> findEvent = new ArrayList<Event>();
		Collections.addAll(findEvent, findTestEvent.execute("meeting"));

		assertEquals(findEvent.indexOf(testCase1.get(0)),
				findEvent.lastIndexOf(testCase1.get(0)));
		assertEquals(findEvent.indexOf(testCase2.get(0)),
				findEvent.lastIndexOf(testCase2.get(0)));

		ArrayList<Event> findEvent2 = new ArrayList<Event>();
		Collections.addAll(findEvent2, findTestEvent.execute("Big Boss"));

		assertEquals(findEvent2.indexOf(testCase2.get(0)),
				findEvent2.lastIndexOf(testCase2.get(0)));
		assertEquals(-1, findEvent2.indexOf(testCase1.get(0)));

		ArrayList<Event> findEvent3 = new ArrayList<Event>();
		Collections.addAll(findEvent3, findTestEvent.execute("birthday"));

		assertEquals(-1, findEvent3.indexOf(testCase1.get(0)));
		assertEquals(-1, findEvent3.indexOf(testCase2.get(0)));
		assertEquals(findEvent.indexOf(testCase3.get(0)),
				findEvent.lastIndexOf(testCase3.get(0)));

		// ArrayList<Event> findEvent4 = new ArrayList<Event>();
		// Collections.addAll(findEvent4, findTestEvent.execute("Poornima"));

		// assertEquals(findEvent4.indexOf(testCase3.get(0)),findEvent4.lastIndexOf(testCase3.get(0)));

	}

	@Test
	public void testFindById() {
		// assertEquals(1, new Find().execute("find __$!193882259!$__").length);
	}
}