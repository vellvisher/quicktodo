package tests.action;

import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;

import storage.DataManager;

import core.Utility;

import data.Event;

import data.EventDateTime;

import action.Add;
import action.CheckFree;
import action.Delete;
import action.Find;

public class AddTests
{

	EventDateTime eventDateTime = new EventDateTime();
	private static Add addTestEvent = new Add();
	private static Delete deleteTestEvent = new Delete();
	private static Find findTestEvent = new Find();
	private static CheckFree checkFreeTest = new CheckFree();

	private static String[] allEvents;
	private static String[] datelessEvents;
	private static String[] weirdEvents;
	private static String[] time;
	private static String[] time2;
	private static String[] nameOfMonths;
	private static String[] listAEvents;
	private static String[] listBEvents;
	private static String[] listCEvents;
	private static String[] listDEvents;
	private static String[] listEEvents;
	private static String[] listFEvents;
	private static String[] listGEvents;
	private static String[] listHEvents;

	private ArrayList<Event> results = new ArrayList<Event>();

	@Before
	public void setUpBeforeClass() throws Exception {
		DataManager.loadLists();

		allEvents = new String[] {
				"add go for housewarming party on :DATE: at :TIME:",
				"add pizza party at :TIME:", "add pizza party :TIME:",
				"add meeting :DATE: :TIME: @work @important",
				// TODO "add sleep on :DATE: for one hour",
				"add meet Milli on :DATE: for 3 hrs"

		};

		datelessEvents = new String[] { "add do CS2103 homework tmr",
		// TODO"add watch the 3rd SAW movie day after tomorrow",
		// TODO"add go shopping day aft tmr",
		// TODO"add submit demo script next mon",
		// TODO"add go to clinic next week",
		// TODO"add write novel next wk",
		// TODO"add go for F1 event in aug",
		// TODO"add book tickets to India in dec"

		};

		// endtime and other combinations possible
		weirdEvents = new String[] {
		// TODO"add go for holiday from :DATE1: to :DATE2:",
		// TODO"add orientation camp from :DATE1: for 3 days",
		// TODO"add charity event on :DATE1: from :TIME1: to :TIME2:",
		// TODO"add new work project from jan to may",
		// TODO"add CS2103 tutorial every thurs from :TIME1: to :TIME2:"
		};

		time = new String[] { "4pm", "20:21", "13:10", "05AM", "5:10pm", "2pm" };
		time2 = new String[] { "9pm", "23:59", "14.25", "06AM", "3:20pm", "9pm" };
		nameOfMonths = new String[] { "jan", "mar", "may", "aug", "oct", "dec" };

	}

	@Test
	public void test() {

		listAEvents = new String[10];
		listBEvents = new String[10];
		listCEvents = new String[10 * allEvents.length];
		listDEvents = new String[10 * allEvents.length];
		listEEvents = new String[10 * weirdEvents.length];
		listFEvents = new String[10 * weirdEvents.length];
		listGEvents = new String[10 * weirdEvents.length];
		listHEvents = new String[10 * weirdEvents.length];
		EventDateTime curTime = EventDateTime.getCurrentTime();

		for (int j = 0; j < 35; j++) {
			for (int i = 0; i < allEvents.length; i++) {
				curTime.add(Calendar.DAY_OF_MONTH, j);
				listAEvents[i] = allEvents[i].replace(":TIME:", time[i]);
				listBEvents[i] = listAEvents[i]
						.replace(
								":DATE:",
								Integer.toString(curTime
										.get(Calendar.DAY_OF_MONTH))
										+ "/"
										+ Integer.toString(curTime
												.get(Calendar.MONTH)));
				addTestEvent.execute(listBEvents[i]);

			}

		}
		for (int k = 0; k < 35; k++) {
			for (int x = 0; x < allEvents.length; x++) {
				EventDateTime timeNow = EventDateTime.getCurrentTime();
				timeNow.add(Calendar.MONTH, k);
				listCEvents[x] = allEvents[x].replace(":TIME:", time[x]);
				listDEvents[x] = listCEvents[x].replace(
						":DATE:",
						timeNow.get(Calendar.DAY_OF_MONTH)
								+ Utility.getDaySuffix(timeNow
										.get(Calendar.DAY_OF_MONTH)) + " "
								+ nameOfMonths[x]);
				addTestEvent.execute(listDEvents[x]);
			}

		}

		for (int a = 0; a < datelessEvents.length; a++) {
			addTestEvent.execute(datelessEvents[a]);
		}

		for (int m = 0; m < 35; m++) {
			for (int n = 0; n < (weirdEvents.length); n++) {
				curTime.add(Calendar.DAY_OF_MONTH, n);
				listEEvents[n] = weirdEvents[n]
						.replace(
								":DATE1:",
								Integer.toString(curTime
										.get(Calendar.DAY_OF_MONTH))
										+ "/"
										+ Integer.toString(curTime
												.get(Calendar.MONTH)));
				curTime.add(Calendar.DAY_OF_MONTH, n + 1);
				listFEvents[n] = listEEvents[n]
						.replace(
								":DATE2:",
								Integer.toString(curTime
										.get(Calendar.DAY_OF_MONTH))
										+ "/"
										+ Integer.toString(curTime
												.get(Calendar.MONTH)));
				listGEvents[n] = listFEvents[n].replace(":TIME1:", time[n]);
				listHEvents[n] = listGEvents[n].replace(":TIME2:", time2[n]);
				addTestEvent.execute(listHEvents[n]);

			}

		}

		// DataManager.exportToTxt();

	}

	@Test
	public void findTest() {
		Event[] findEvent = findTestEvent.execute("find meet Milli");
	}

	@Test
	public void checkIfTest() {
		Event[] checkIfEvent = checkFreeTest.execute("check if 30/10 6:30pm");
	}

}
