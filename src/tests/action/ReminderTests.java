package tests.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.Parser;
import storage.DataManager;

import data.Event;
import data.EventDateTime;

import action.Reminder;

public class ReminderTests
{
	private static final String TIME_TAG = ":TIME:";
	private String[] eventStrings = {"this event will come first " +TIME_TAG,
			"*this is an important passed event " + TIME_TAG,
			"this event comes after the important overdue one " + TIME_TAG, 
			"*this event is the important event " + TIME_TAG,
			"*this is an important event which is going to come second " + TIME_TAG,
			"*this is an important event without time",
			"this event will come after the important ones " + TIME_TAG,
			"this will come second after the important ones " + TIME_TAG,
			"this event will probably come last",
			"this event will also be close somewhere at the end", 
			"this event should not come", 
			"this event cannot come " + TIME_TAG};
	private Event[] testEvents = new Event[eventStrings.length];
	private Event[] expectedEvents1;
	private Event[] expectedEvents2;
	
	@Before
	public void setUp() throws Exception {
		EventDateTime tempDate = EventDateTime.getCurrentTime();
		
		eventStrings[0] = eventStrings[0].replace(TIME_TAG, tempDate.toPrettyString());
		
		eventStrings[1] = eventStrings[1].replace(TIME_TAG, tempDate.toPrettyString());
		
		tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.MINUTE, +1);
		eventStrings[2] = eventStrings[2].replace(TIME_TAG, tempDate.toPrettyString());
		
		tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.MINUTE, +1);
		eventStrings[3] = eventStrings[3].replace(TIME_TAG, tempDate.toPrettyString());

		tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.MINUTE, +1);
		eventStrings[4] = eventStrings[4].replace(TIME_TAG, tempDate.toPrettyString());		
		
		tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.DAY_OF_MONTH, 1);
		tempDate.setTimeSet(false);
		eventStrings[6] = eventStrings[6].replace(TIME_TAG, tempDate.toPrettyString());
		
		tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.DAY_OF_MONTH, 12);
		eventStrings[7] = eventStrings[7].replace(TIME_TAG, tempDate.toPrettyString());

		tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.DAY_OF_MONTH, 1);
		eventStrings[11] = eventStrings[11].replace(TIME_TAG, tempDate.toPrettyString());
		
		for (int i = 0; i < testEvents.length; i++) {
			testEvents[i] = Parser.parseEvent(eventStrings[i]);
			testEvents[i].setId("$" + 10000 + i + "$");
		}
		testEvents[0].getStart().add(Calendar.DAY_OF_MONTH, -5);
		
		testEvents[1].getStart().add(Calendar.DAY_OF_MONTH, -5);
		testEvents[1].getStart().add(Calendar.HOUR, 1);
		
		testEvents[2].getStart().add(Calendar.HOUR_OF_DAY, -1);
		
		testEvents[3].getStart().add(Calendar.HOUR_OF_DAY, 3);

		testEvents[4].getStart().add(Calendar.DAY_OF_MONTH, 3);
		
		testEvents[10].setTicked(true);
		testEvents[11].setTicked(true);
		
		expectedEvents1 = Arrays.copyOf(testEvents, testEvents.length - 4);
		expectedEvents2 = new Event[2];
		expectedEvents2[0] = testEvents[8];
		expectedEvents2[1] = testEvents[9];
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsUndoable() {
		assertFalse((new Reminder()).isUndoable());
	}

	@Test
	public void testReminder() {
		Reminder remObject = new Reminder();
		for (int i = 0; i < 5; i++) {
			List<Event> shuffledEventsList = new ArrayList<Event>();
			shuffledEventsList.addAll(Arrays.asList(testEvents));
			Collections.shuffle(shuffledEventsList);
			Event[] shuffledEvents = (Event[]) shuffledEventsList.toArray(new Event[shuffledEventsList
					.size()]);
			Event[] resultArray = remObject.reminder(shuffledEvents);
			assertArrayEquals(expectedEvents1, Arrays.copyOf(resultArray, resultArray.length - 2));
			for (Event expectedEvent : expectedEvents2) {
				assertTrue(Arrays.asList(resultArray).contains(expectedEvent));
			}
		}
	}

	@Test
	public void loadTest() {
		DataManager.loadLists();
		new Reminder().execute("");
	}
	
	@Test
	public void testIsCorrectInput() {
		
	}
}
