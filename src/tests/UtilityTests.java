package tests;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Utility;
import data.EventDateTime;

public class UtilityTests extends Utility
{

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStringToEventTime() {
		EventDateTime calen = new EventDateTime(2011, 12, 5);
		assertEquals(calen,
				Utility.stringToEventDateTime("05.12.2011", null)[0]);
		assertEquals(calen, Utility.stringToEventDateTime("5/12/11", null)[0]);
		assertEquals(calen, Utility.stringToEventDateTime("05-12", null)[0]);
		assertEquals(calen, Utility.stringToEventDateTime("5/12", null)[0]);

		EventDateTime calen2 = new EventDateTime(2011, 12, 5, 5, 30);
		assertEquals(calen2,
				Utility.stringToEventDateTime("05.12.2011", "05:30am")[0]);
		assertEquals(calen2,
				Utility.stringToEventDateTime("5/12/11", "5:30am")[0]);
		assertEquals(calen2,
				Utility.stringToEventDateTime("05-12", "05:30am")[0]);
		assertEquals(calen2, Utility.stringToEventDateTime("5/12", "5:30am")[0]);

		EventDateTime calen3 = new EventDateTime(2011, 12, 5, 17, 30);
		assertEquals(calen3,
				Utility.stringToEventDateTime("05.12.2011", "05:30pm")[0]);
		assertEquals(calen3,
				Utility.stringToEventDateTime("5/12/11", "5:30pm")[0]);
		assertEquals(calen3, Utility.stringToEventDateTime("05-12", "17:30")[0]);
		assertEquals(calen3, Utility.stringToEventDateTime("5/12", "17:30")[0]);

		EventDateTime calen4 = EventDateTime.getCurrentTime();
		calen4.set(Calendar.HOUR_OF_DAY, 2);
		calen4.set(Calendar.MINUTE, 30);
		calen4.set(Calendar.SECOND, 0);
		if (calen4.compareTo(EventDateTime.getCurrentTime()) < 0) {
			calen4.inc(Calendar.DAY_OF_MONTH);
		}
		assertEquals(calen4, Utility.stringToEventDateTime(null, "02:30am")[0]);

		// EventDateTime calen5 = EventDateTime.current();
		// calen5.set(Calendar.HOUR_OF_DAY, calen5.get(Calendar.HOUR_OF_DAY +
		// 1));
		// calen5.set(Calendar.MINUTE, 30);
		// calen5.set(Calendar.SECOND, 0);
		// assertEquals(calen5, Utility.stringToEventDateTime("null",
		// "05:30pm")[0]);
		//
		// EventDateTime calen6 = new EventDateTime(2012,1,14);
		// assertEquals(calen6, Utility.stringToEventDateTime("14.01.2012",
		// "null")[0]);

		// Wait chill
		// GregCalendar calenOneExtra = (GregCalendar)Calendar.getInstance();
		// calen.add(GregCalendar.DAY_OF_MONTH, 1);
		// assertEquals(calenOneExtra, Utility.stringToGreg("15.10.2011"));
		// assertEquals(calenOneExtra, Utility.stringToGreg("15/10/11"));
		// assertEquals(calenOneExtra, Utility.stringToGreg("15-10"));
		// assertEquals(calenOneExtra, Utility.stringToGreg("15\10"));
	}

	@Test
	public void testGetDaySuffix() {
		assertEquals("st", Utility.getDaySuffix(1));
		assertEquals("nd", Utility.getDaySuffix(2));
		assertEquals("rd", Utility.getDaySuffix(3));
		assertEquals("th", Utility.getDaySuffix(4));
		assertEquals("th", Utility.getDaySuffix(10));
		assertEquals("th", Utility.getDaySuffix(11));
		assertEquals("th", Utility.getDaySuffix(12));
		assertEquals("th", Utility.getDaySuffix(13));
		assertEquals("th", Utility.getDaySuffix(14));
		assertEquals("st", Utility.getDaySuffix(21));
		assertEquals("nd", Utility.getDaySuffix(22));
		assertEquals("rd", Utility.getDaySuffix(23));
		assertEquals("th", Utility.getDaySuffix(24));
		assertEquals("th", Utility.getDaySuffix(30));
		assertEquals("st", Utility.getDaySuffix(31));
	}
}
