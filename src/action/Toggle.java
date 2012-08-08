/**
 * extends Action
 * This class decides which type of toggle action is expected- tick or star
 * This class then calls the toggleAction class which actually implements the toggle
 * @author Vaarnan Drolia
 */
package action;

import java.util.ArrayList;
import java.util.Arrays;

import action.ToggleAction.TOGGLE_TYPE;

import parser.Parser;
import storage.DataManager;
import core.Utility;
import data.Event;

public class Toggle extends Action
{
	ArrayList<ToggleAction> toggleActions;
	private static final String[] TOGGLE_COMMANDS = { "star", "tick" };

	/**
	 * @param userCommand
	 * @return Event[] of events that were toggled
	 */
	@Override
	public Event[] execute(String userCommand) {
		userCommand = userCommand.replace("toggle ", "");
		String command = Utility.getHead(userCommand);
		String[] idList = Parser.extractIds(userCommand);

		toggleActions = new ArrayList<ToggleAction>();
		TOGGLE_TYPE type = TOGGLE_TYPE.STAR;

		if (command.equals("tick")) {
			type = TOGGLE_TYPE.TICK;
		}

		ArrayList<Event> result = new ArrayList<Event>();

		for (String id : idList) {
			ToggleAction toggleAction = new ToggleAction(type);
			toggleActions.add(toggleAction);
			result.add(toggleAction.execute(DataManager.getEventById(id))[0]);
		}
		return (Event[]) result.toArray(new Event[result.size()]);
	}

	/**
	 * @return Event[] of undone events In this case, starred will be unstarred
	 *         and vice versa Similarly, ticked will be unticked and vice- versa
	 */
	@Override
	public Event[] undo() {
		ArrayList<Event> undoResult = new ArrayList<Event>();
		for (ToggleAction toggleAction : toggleActions) {
			undoResult.add(toggleAction.undo()[0]);
		}
		return (Event[]) undoResult.toArray(new Event[undoResult.size()]);
	}

	/**
	 * @return boolean always true. All toggle functions are undoable
	 */
	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String errorMessage() {
		return null;
	}

	// FIXME will be take out and validated with other commands right?
	public static boolean isToggleCommand(String command) {
		command = command.replace("toggle ", "");
		String action = Utility.getHead(command);
		if (isToggleAction(action)) {
			String toggleEvent = Parser.extractId(command.replace(action + " ",
					""));
			return toggleEvent != null;
		}
		return false;
	}

	private static boolean isToggleAction(String command) {
		if (Arrays.binarySearch(TOGGLE_COMMANDS, command) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return string containing the command being carried out In this case,
	 *         either star or tick
	 */
	@Override
	public String getCommandName() {
		return toggleActions.get(0).getCommandName();
	}
}