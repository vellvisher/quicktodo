/**
 * extends Action
 * This class finds all overdue functions and passes them out as an array
 * 
 * @author Vaarnan Drolia
 */
package action;

import parser.Parser;
import core.Utility;
import data.Event;

public class Archive extends Action implements DataMiner
{

	/**
	 * @param userCommand
	 * @return Event[] containing all done events in database
	 */
	@Override
	public Event[] execute(String userCommand) {
		String params = userCommand.replace(Utility.getHead(userCommand), "");
		return archive(Parser.parseEvent(params));
	}
	
	/**
	 * Look for an event matching the archived one in the entire database
	 * @param archivedEvent
	 * @return
	 */
	public Event[] archive(Event archivedEvent) {
		archivedEvent.setTicked(true);		
		return new Find().find(archivedEvent);
	}

	/**
	 * Look for an event matching the archived one in the searched events
	 * @param archivedEvent
	 * @return
	 */
	public Event[] archive(Event archivedEvent, Event[] searchEvents) {
		archivedEvent.setTicked(true);
		return new Find().find(archivedEvent, searchEvents);
	}
	
	/**
	 * Undo for this class is irrelevant
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * Undo for this class is irrelevant
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "You have not done anything :-\\";
	}

	/**
	 * @return string containing current action being executed in this case,
	 *         archive
	 */
	@Override
	public String getCommandName() {
		return "archive";
	}
}
