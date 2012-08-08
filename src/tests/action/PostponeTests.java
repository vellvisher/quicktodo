package tests.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import storage.DataManager;
import storage.FileDatabase;
import action.Add;
import action.Edit;
import action.Find;
import action.Postpone;
import data.Event;
import data.EventDateTime;

public class PostponeTests
{

	private static Add addTestEvent = new Add();
	private static Find findEvent = new Find();
	static FileDatabase newdatabase = new FileDatabase();

	Postpone postponeTestEvent = new Postpone();
	private static ArrayList<Event> testCase1;
	private static ArrayList<Event> testCase2;
	private static ArrayList<Event> testCase3;
	private static ArrayList<Event> testCase1mod;
	private static ArrayList<Event> testCase2mod;
	private static ArrayList<Event> testCase3mod;
	private static Event case1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DataManager.loadLists();

		if (newdatabase.clearDatabase())
			System.out.println("db empty");

		testCase1 = new ArrayList<Event>();
		Event[] result = addTestEvent.execute("add dance performance at 9pm");
		Collections.addAll(testCase1, result);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		Event[] foundEvent = findEvent.execute("dance performance");
		EventDateTime oldEventStart = foundEvent[0].getStart();
		EventDateTime oldEventEnd = foundEvent[0].getEnd();
		int oldEventDur = foundEvent[0].getDuration();

		postponeTestEvent.execute("postpone " + foundEvent[0].getId());
		postponeTestEvent.execute("postpone 9pm");

		Event[] random = findEvent.execute("dance performance");
		EventDateTime newEventStart = random[0].getStart();
		EventDateTime newEventEnd = random[0].getEnd();
		int newEventDur = random[0].getDuration();

		System.out.println(oldEventStart.toString());
		System.out.println(newEventStart.toString());
		assertSame(newEventStart, oldEventStart);
	}

}
