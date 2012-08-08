package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ControllerTests.class, 
		GoogleCalendarTests.class, HttpClientTests.class,
		OverdueTests.class, UtilityTests.class,
		tests.storage.DataManagerTests.class,
		tests.storage.FileDatabaseTests.class,
		tests.storage.FileHandlerTests.class})
public class AllTests
{

}
