package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import storage.DataManager;

import data.Event;

import action.Add;
import action.Overdue;

public class OverdueTests
{

	private static Add addTestEvent = new Add();
	Overdue overdueTestEvent = new Overdue();
	private static ArrayList<Event> testCase1;
	private static ArrayList<Event> testCase2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DataManager.loadLists();

		testCase1 = new ArrayList<Event>();
		Event[] result = addTestEvent.execute("add meeting 05.10.2009 5:30pm");
		Collections.addAll(testCase1, result);

		testCase2 = new ArrayList<Event>();
		Event[] result2 = addTestEvent
				.execute("add birthday 05.10.2013 5:30pm");
		Collections.addAll(testCase2, result2);
	}

	@Test
	public void test() {
		ArrayList<Event> OverdueEvent0 = new ArrayList<Event>();
		Collections.addAll(OverdueEvent0, overdueTestEvent.execute("overdue"));
		assertEquals(OverdueEvent0.indexOf(testCase1.get(0)),
				OverdueEvent0.lastIndexOf(testCase1.get(0)));
		assertEquals(-1, OverdueEvent0.indexOf(testCase2.get(0)));

	}

}
