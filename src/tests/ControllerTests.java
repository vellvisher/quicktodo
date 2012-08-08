package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Controller;

public class ControllerTests
{

	private Controller controllerObj;

	@Before
	public void setUp() throws Exception {
		controllerObj = new Controller();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteCommand() throws Exception {
		// assertNull(controllerObj.executeCommand(null));
		// assertNull(controllerObj.executeCommand(""));
	}

}
