package tests.action;

import static org.junit.Assert.*;

import java.util.Calendar;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import parser.Parser;

import action.Add;
import action.Delete;
import action.Edit;
import storage.DataManager;
import data.Event;
import data.EventDateTime;

public class EditTests
{
	private static final String TIME_TAG = ":TIME:";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DataManager.loadLists();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testBasicEditUndo() {
		EventDateTime tempDate = EventDateTime.getCurrentTime();
		tempDate.add(Calendar.DAY_OF_MONTH, 2);
		String[] test1String = {"this event will get edited  " +TIME_TAG,
				"*this is what it will become " + TIME_TAG};
		test1String[0] = test1String[0].replace(TIME_TAG, tempDate.toPrettyString());
		tempDate.add(Calendar.DAY_OF_MONTH, 4);
		test1String[1] = test1String[1].replace(TIME_TAG, tempDate.toPrettyString());
		
		Add test1Add = new Add();
		Event[] result = test1Add.execute("add " + test1String[0]);
		
		assertNotNull(result);
		assertEquals(1, result.length);
		Event oldEvent = result[0];
		
		Edit test1Edit = new Edit();
		result = test1Edit.execute("edit " + oldEvent.getId());
		
		assertEquals(1, result.length);
		assertEquals(oldEvent, result[0]);
		
		result = test1Edit.execute("edit " + test1String[1]);
		
		assertEquals(1, result.length);
		Event editedEvent = result[0];
		
		try {
			assertEquals(oldEvent.getId(), editedEvent.getId());
			assertEquals(editedEvent, DataManager.getEventById(oldEvent.getId()));
			Event expectedEditedEvent = Parser.parseEvent(test1String[1]);
			expectedEditedEvent.setId(editedEvent.getId());
			assertEquals(expectedEditedEvent, editedEvent);
			
			result = test1Edit.undo();
			
			assertEquals(1, result.length);
			Event undoneEvent = result[0];
			
			assertEquals(oldEvent, undoneEvent);
			assertEquals(oldEvent, DataManager.getEventById(oldEvent.getId()));
			Event expectedUndoneEvent = Parser.parseEvent(test1String[0]);
			expectedEditedEvent.setId(oldEvent.getId());
			assertEquals(expectedUndoneEvent, oldEvent);
		
		} finally {
			Delete cleanUp = new Delete();
			assertEquals(oldEvent, cleanUp.execute("delete " + editedEvent.getId())[0]);
		}
	}
}
