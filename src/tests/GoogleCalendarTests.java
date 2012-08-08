package tests;

import static org.junit.Assert.*;
import googlecalendar.GoogleCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.ExtendedProperty;

import core.Utility;

import parser.Parser;

import data.Event;
import data.EventList;

public class GoogleCalendarTests
{
	private static GoogleCalendar gcalObj;
	private static Event testEvent;
	private static Event testEvent2;
	private static Event testEvent3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		gcalObj = new GoogleCalendar();
		testEvent = Parser.parseEvent("testAddEvent 1 01/01/2012 4pm");
		testEvent2 = Parser.parseEvent("testAddEvent 2 tmr 4pm");
		testEvent3 = Parser.parseEvent("testAddEvent 3 01/01/2012");
		
		testEvent.setId(Utility.generateId());
		testEvent2.setId(Utility.generateId());
		testEvent3.setId(Utility.generateId());
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLogin() {
		gcalObj.login("qtdalpha", "p@55w0rd.");
		assertTrue(gcalObj.isLoggedIn());
	}

	@Test
	public void testAddEvent() {
		CalendarEventEntry test1 = gcalObj.addEvent(testEvent);
		assertNotNull(test1);

		CalendarEventEntry test2 = gcalObj.addEvent(testEvent2);
		assertNotNull(test2);

		CalendarEventEntry test3 = gcalObj.addEvent(testEvent3);
		assertNotNull(test3);
		
		for (ExtendedProperty ep : test1.getExtendedProperty()) {
			if (ep.getName().equals(GoogleCalendar.APPLICATION_NAME_ID_PROPERTY)) {
				assertEquals(testEvent.getId(), ep.getValue());				
			}
		}

		for (ExtendedProperty ep : test2.getExtendedProperty()) {
			if (ep.getName().equals(GoogleCalendar.APPLICATION_NAME_ID_PROPERTY)) {
				assertEquals(testEvent2.getId(), ep.getValue());				
			}
		}
		
		for (ExtendedProperty ep : test3.getExtendedProperty()) {
			if (ep.getName().equals(GoogleCalendar.APPLICATION_NAME_ID_PROPERTY)) {
				assertEquals(testEvent3.getId(), ep.getValue());				
			}
		}
	}

	@Test
	public void testFindEvent() {
		EventList allEvents = gcalObj.getCalendarTextQuery("testAddEvent");
		assertEquals(3, allEvents.size());
		assertTrue(allEvents.contains(testEvent));
		assertTrue(allEvents.contains(testEvent2));
		assertTrue(allEvents.contains(testEvent3));
	}

	@Test
	public void testDeleteEvent() {
		assertNotNull(gcalObj.deleteEvent(testEvent));
		assertNotNull(gcalObj.deleteEvent(testEvent2));
		assertNotNull(gcalObj.deleteEvent(testEvent3));
	}
}
