/**
 * Abstract class
 * The action class decides which action the user intends
 * It then creates an object of that action
 * Execute function overwritten in each class that inherits from this
 * program dynamically runs the execute function of the action object actually chosen
 * @author Vaarnan Drolia
 * @author Poornima Muthukumar
 */
package action;

import org.apache.log4j.Logger;

import core.Utility;
import data.Event;

public abstract class Action
{
	/**
	 * 
	 * @param commandFromUser
	 * @return Action object depending on which action is being called
	 */
	private static Logger logger = Logger.getLogger(Action.class.getName());
	protected String userCommand;
	public static Action getActionObject(String commandFromUser) {
		if (!commandFromUser.contains("login")) {
			logger.info(commandFromUser); //Prevent logging of user credentials
		}
		String intendedAction;
		intendedAction = Utility.getHead(commandFromUser).toLowerCase();
		Action obj;
		// TODO validation to be implemented for all cases

		if (intendedAction.equals("toggle")
				&& Toggle.isToggleCommand(commandFromUser)) {
			return new Toggle();
		} else if (intendedAction.equals("add")
				&& Add.isCorrectInput(commandFromUser)) {
			obj = new Add();
		} else if (intendedAction.equals("delete")) {
			obj = new Delete();
		} else if (intendedAction.equals("find")
				|| intendedAction.equals("search")) {
			obj = new Find();
		} else if (intendedAction.equals("login")
				|| intendedAction.equals("logout")) {
			obj = new GoogleCalendarAction();
		} else if (intendedAction.equals("edit")) {
			obj = new Edit();
			// } else if (intendedAction.equals("postpone")) {
			// obj = new Postpone();
		} else if (intendedAction.equals("free?")) {
			obj = new CheckFree();
		} else if (intendedAction.equals("archive")
				|| intendedAction.equals("done")) {
			obj = new Archive();
		} else if (intendedAction.equals("schedule")) {
			obj = new Schedule();
		} else if (intendedAction.equals("overdue")) {
			obj = new Overdue();
		} else if (intendedAction.equals("reminder")) {
			obj = new Reminder();
		} else if (intendedAction.equals("update") && UpdateProgram.isCorrectInput(commandFromUser)) {
			obj = new UpdateProgram();
		} else {
			obj = new Default();
		}
		return obj;
	}

	// TODO to be implemented
	public static boolean isCorrectInput(String input) {
		return true;
	}

	/**
	 * 
	 * @param userCommand
	 * @return Event[] returned by whichever action is actually executed
	 */
	public abstract Event[] execute(String userCommand);

	protected Event[] execute(Event e) {
		return null;
	}

	/**
	 * 
	 * @return an event[] with the undone events from the action executed
	 */
	public abstract Event[] undo();

	/**
	 * 
	 * @return boolean - true if action should be added to the undoable stack
	 */
	public abstract boolean isUndoable();

	public abstract String errorMessage();

	public abstract String getCommandName();

}
