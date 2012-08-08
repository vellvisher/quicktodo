package tests.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import action.Add;
import action.Find;
import action.Toggle;

import storage.DataManager;
import data.Event;

//FIXME Fix test according to new toggle
public class ToggleTests
{

	private static Add addTestEvent = new Add();
	private static Toggle ToggleEvent = new Toggle();
	// Add addTestEvent2 = new Add();
	// Add addTestEvent3 = new Add();

	Find findEvent = new Find();
	private static ArrayList<Event> testCase1;
	private static ArrayList<Event> testCase2;
	private static ArrayList<Event> testCase3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		DataManager.loadLists();

		// testCase1 = new ArrayList<Event>();
		Event[] result = addTestEvent.execute("add meet Ankit later");
		result = addTestEvent.execute("meet Ankit again on Friday");
		result = addTestEvent.execute("meet Ankit this Wednesday");
		result = addTestEvent.execute("meet Ankit tomorrow night");
		result = addTestEvent.execute("meet Ankit on 15th october");
		result = addTestEvent.execute("meet Ankit on October 16th");

		// Collections.addAll(testCase1,result);
		// testCase2 = new ArrayList<Event>();
		// result =
		// addTestEvent.execute("add Big Boss meeting 05.12.2011 07:30pm #Harsha");
		// Collections.addAll(testCase2, result);
		// testCase3 = new ArrayList<Event>();
		// result =
		// addTestEvent.execute("add Harsha birthday 16.10.2011 #Poornima");
		// Collections.addAll(testCase3, result);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {

		Event[] returnedArray = findEvent.execute("meet Ankit");
		for (int i = 0; i < returnedArray.length; i++) {
			// assertFalse(returnedArray[i].getTogglered());
			ToggleEvent.execute("Toggle " + returnedArray[i].getId());
		}

		returnedArray = findEvent.execute("meet Ankit");

		for (int i = 0; i < returnedArray.length; i++) {
			// assertTrue(returnedArray[0].getTogglered());
		}

	}

}
