package tests.action;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import action.Action;
import action.Default;
import action.Find;

public class ActionTests
{
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetActionObject() {
		assertTrue(Action.getActionObject("something random should find") instanceof Default);
	}

}
