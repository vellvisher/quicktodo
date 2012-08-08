/**
 * extends action
 * short for FIND and EXECUTE.
 * Our code narrows events down as they are searched, in a google like search method
 * As a result we have to make sure only when the user actually intends to select
 * an action is it actually executed
 * This class ensures that only when an eventId is passed, the action is actually
 * carried out depending on what the user had entered
 * 
 * @author Vaarnan Drolia
 */
package action;

import java.util.ArrayList;
import java.util.Collections;

import parser.Parser;
import storage.DataManager;
import data.Event;

public class FindExec extends Action implements DataMiner
{
	protected String commandName;
	protected boolean isExecuting = false;

	/**
	 * 
	 * @param userCommand
	 * @return Event[] containing the events as a result of execution by classes
	 *         that inherit from this class
	 */
	@Override
	public Event[] execute(String userCommand) {

		String params = userCommand;
		if (userCommand.toLowerCase().startsWith(this.commandName + " ")) {
			params = userCommand.substring(this.commandName.length() + 1);
		}

		String[] eventIds = Parser.extractIds(params);
		Event findEvent = Parser.parseEvent(params);
		ArrayList<FindExec> findExecObjects = new ArrayList<FindExec>();
		if (eventIds != null) {
			ArrayList<Event> resultEvents = new ArrayList<Event>();
			for (int i = 0; i < eventIds.length; i++) {
				Event e = DataManager.getEventById(eventIds[i]);
				isExecuting = true; // FIXME not quite sure what this is
									// supposed to do
				Event[] result = execute(e);
				if (result == null) {
					return null;
				}
				Collections.addAll(resultEvents, result);
			}
			Event[] result = (Event[]) resultEvents
					.toArray(new Event[resultEvents.size()]);
			return result;
		} else {
			Find f = new Find();
			Event[] foundEvents = f.find(findEvent);

			if (foundEvents == null || foundEvents.length == 0) {
				return null;
			}
			return foundEvents;
		}

	}

	/**
	 * @return Event[] with events undone by class that actually executed the
	 *         action
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * overwritten in all classes
	 * 
	 * @return depends on actual object. false by default
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return null;
	}

	/**
	 * 
	 * @return boolean - true if function is executing, false otherwise
	 */
	public boolean isExecuting() {
		return isExecuting;
	}

	/**
	 * @return string containing the action currently executing in this case,
	 *         findexec
	 */
	@Override
	public String getCommandName() {
		return "findExec";
	}
}
