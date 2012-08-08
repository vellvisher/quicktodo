package tests.storage;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.EventList;

import storage.FileDatabase;

public class FileDatabaseTests
{
	private FileDatabase newDB;

	@Before
	public void setUp() throws Exception {
		newDB = new FileDatabase();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testClearDatabase() {
		assertTrue(newDB.clearDatabase());
		assertEquals(0, newDB.getEventListNames().length);
	}
	
	@Test
	public void testReadWriteGetEventLists() {
		String[] testListNames = {"default", "List1", "List2", "List3"};
		EventList[] testLists = new EventList[4];
		
		testLists[0] = new EventList();
		assertTrue(newDB.writeEventList(testLists[0]));
		
		for (int i = 1; i < testListNames.length; i ++) {
			testLists[i] = new EventList(testListNames[i]);
			assertTrue(newDB.writeEventList(testLists[i]));
		}
		
		for (int i = 0; i < testListNames.length; i++) {
			EventList readList = newDB.readList(testListNames[i]);
			assertEquals(testLists[i], readList);
		}
		
		String[] namesOfLists = newDB.getEventListNames();

		Arrays.sort(testListNames);
		Arrays.sort(namesOfLists);

		assertArrayEquals(testListNames, namesOfLists);
	}
	
	@Test
	public void testExportDatabaseToText() {
		
	}
}
