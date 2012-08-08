package tests.storage;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import storage.DataManager;

public class DataManagerTests
{

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExportToTxt() {
		DataManager.loadLists();
		DataManager.exportToTxt();
	}

	@Test
	public void testFindInStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testReplace() {
		fail("Not yet implemented");
	}

}
