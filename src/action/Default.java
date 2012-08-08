/**
 * This event runs a default find because the user entered no keyword
 * @author Vaarnan Drolia
 */
package action;

import data.Event;
import java.lang.String;

import parser.Parser;

public class Default extends Find
{

	/**
	 * returns the result of the default action
	 */
	@Override
	public Event[] execute(String userCommand) {
		Event[] result = new Event[1];
		Event userEvent = Parser.parseEvent(userCommand);

		return super.execute("find " + userCommand);
	}

	/**
	 * undo is irrelevant here
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * undo is irrelevant here
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return super.errorMessage();
	}
}
