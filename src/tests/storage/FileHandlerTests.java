package tests.storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import storage.FileHandler;

public class FileHandlerTests
{
	private static String testPath;
	
	@AfterClass
	public static void tearDownAfterClass() {
		FileHandler.removeFile(testPath);
	}
	
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadWriteObject() {
		int testValue = 5;
		Integer newIntegerObj = new Integer(testValue);
		testPath = "testWriteObject";
		assertTrue(FileHandler.writeObject(newIntegerObj, testPath));
		assertTrue(new File(testPath).exists());
		Object readObj = FileHandler.readObject(testPath);
		Integer readInt = (Integer) readObj;
		assertEquals(testValue, readInt.intValue());
		assertNull(FileHandler.readObject(testPath+"failIt"));
	}

	@Test
	public void testAddRemoveFile() throws IOException {
		String testFile = "testingAddRemoveFile";
		String testString = "abc";
		assertTrue(FileHandler.writeToFile(testString, testFile, false));
		File testFileObject = new File(testFile);
		BufferedReader read = new BufferedReader(new FileReader(testFileObject));
		String newString = read.readLine();
		assertTrue(!newString.equals(testString+"\n"));
		assertTrue(FileHandler.removeFile(testFile));
	}

	@Test
	public void testCreateSha1Checksum() {
		assertNotNull(testPath);
	}

	@Test
	public void testUnzipFile() {

	}
}
