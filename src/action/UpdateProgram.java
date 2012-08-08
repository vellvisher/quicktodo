/**
 * This class is built to automatically update the users version of QuickToDo
 * @author Vaarnan Drolia
 */
package action;

import java.io.File;

import core.HttpClient;
import core.Utility;
import data.Event;

public class UpdateProgram extends Action
{

	/**
	 * this function updates the users version of the product
	 * 
	 * @param userCommand
	 * @return always null because no events are being affected
	 */
	@Override
	public Event[] execute(String userCommand) {
		String version = UpdateProgram.class.getPackage()
				.getImplementationVersion();
		if (!HttpClient.isUpdateRequired(version)) {
			return new Event[0];
		}
		String updatedFilePath = HttpClient.downloadUpdatedFile();
		if (updatedFilePath != null) {
			String checksum = HttpClient.getUpdateFileChecksum();
			if (Utility.verifyChecksum(checksum, updatedFilePath)) {
				if (Utility.replaceWithUpdatedFile(updatedFilePath)) {
					if ((new File(updatedFilePath)).delete()) {
						return new Event[1];
					}
				}
			}
		}
		return null;
	}

	/**
	 * Undo is irrelevant for this class
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * Undo is irrelevant for this class
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "Could not update program";
	}

	/**
	 * @return string containing the current action being executed in this case,
	 *         update
	 */
	@Override
	public String getCommandName() {
		return "update";
	}

	public static boolean isCorrectInput(String input) {
		return input.trim().equalsIgnoreCase("update");
	}

}
