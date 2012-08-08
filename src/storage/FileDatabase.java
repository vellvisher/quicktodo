/**
 * class FileDatabase
 * This class serves as an abstraction to separate the DataManager from the FileHandler
 * It serves as a link between the live storage and the permanent storage
 * All updates to permanent storage go through this
 * All access to permanent storage must be called from here
 * 
 * @author Ankit Ganla
 * @author Vaarnan Drolia
 * 
 */

package storage;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.Event;
import data.EventList;

public class FileDatabase
{
	private static String nameOfDirectory = "userData";
	private static String nameOfTxtFile = "EventsInDatabase.txt";
	private Logger logger = Logger.getLogger(FileDatabase.class.getName());
	public FileDatabase() {
		int count = 0;
		final int MAX_TRIES = 3;
		while (!databaseExists() && !createDirectory(nameOfDirectory) && count < MAX_TRIES) {
			count ++;
		}
		if (count == MAX_TRIES) {
			logger.error("Could not create file database");
		}
	}

	private boolean databaseExists() {
		return new File(nameOfDirectory).isDirectory();
	}

	/**
	 * 
	 * @param listName
	 * @return EventList object
	 */
	public EventList readList(String listName) {
		logger.debug(listName);
		String destination = nameOfDirectory + File.separator + listName;
		EventList eventList = (EventList) FileHandler.readObject(destination);
		return eventList;
	}
	
	/**
	 * 
	 * @param eventList
	 * @return boolean - true if written, false if not written
	 */
	public boolean writeEventList(EventList eventList) {
		assert(eventList != null);
		
		if (logger.isDebugEnabled()) {
			logger.debug(eventList.getName());
		}
		
		String destination = nameOfDirectory + File.separator
				+ eventList.getName();
		if (!FileHandler.writeObject(eventList, destination)) {
			logger.warn("EventList was not written to storage");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param directoryName
	 * @return boolean - true if directory is created, false if not created
	 */
	private boolean createDirectory(String directoryName) {
		logger.debug(directoryName);
		File dir = new File(directoryName);
		if (dir.isDirectory()) {
			logger.warn("directory not created because it already exsists");
			return false;
		}
		if (dir.exists()) {
			logger.debug("file with same name exists");
		}
		boolean created =  dir.mkdir();
		if (!created) {
			logger.error("could not create directory");
		}
		return created;
	}

	/**
	 * 
	 * @return String array with names of all lists
	 */
	public String[] getEventListNames() {
		String[] namesOfLists;
		File dir = new File(nameOfDirectory);
		namesOfLists = dir.list();
		return namesOfLists;
	}

	/**
	 * 
	 * @return boolean- true if database is cleared and recreated, false if not
	 */
	public boolean clearDatabase() {
		File dir = new File(nameOfDirectory);
		boolean cleared = true;
		File[] dataFiles = dir.listFiles();
		for (File file : dataFiles) {
			if (!FileHandler.removeFile(file.getPath())) {
				cleared = false;
			}
		}
		if (!FileHandler.removeFile(dir.getPath())) {
			logger.error("Directory was not delete");			
			return false;
		}
		
		if (!createDirectory(nameOfDirectory)) {
			logger.error("Directory was not recreated");
			return false;
		}
		return cleared;
	}

	/**
	 * Sends string to FileHandler to be written to a file
	 * 
	 * @param toBeWritten
	 */
	public boolean exportDatabaseToText(EventList[] eventLists) {
		logger.debug(eventLists);
		boolean written = true;
		for (EventList eventList : eventLists) {
			ArrayList<Event> listOfEvents = eventList.getEvents();
			StringBuffer lines = new StringBuffer("");
			for (int i = 0; i < listOfEvents.size(); i++) {
				lines.append(listOfEvents.get(i)).append("\n");
			}
			String writeToFile = eventList.getName()+".txt";
			if (eventList.getName().equals("default")) {
				writeToFile = nameOfTxtFile;
			}
			if (!FileHandler.writeToFile(lines.toString(), writeToFile, false)) {
				logger.error("Could not write to file, list - " + eventList.getName());
				written = false;
			}
			logger.debug("wrote to text list - " + eventList.getName());
		}
		return written;
	}
}