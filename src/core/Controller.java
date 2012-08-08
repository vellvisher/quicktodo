/**
 * This class runs the entire program. 
 * It communicates with UI and Logic and Storage to co-ordinate the running of the application
 * @author Vaarnan Drolia
 */
package core;

import java.util.Stack;

import org.apache.log4j.Logger;

import ui.View;

import storage.DataManager;
import action.Action;

public class Controller implements Runnable
{
	private Stack<Action> undoStack;
	private UiController ui;
	private String command;
	private Logger logger = Logger.getLogger(Controller.class.getName());
	/**
	 * controller constructor. Builds the controller object
	 */
	public Controller() {
		ui = new UiController();
		DataManager.loadLists();
		undoStack = new Stack<Action>();
	}

	/**
	 * Takes the command from the user and implements functionality via logic
	 * Gives UI directions to display results of implementation
	 * 
	 * @param commandFromUser
	 */
	public void executeCommand(String commandFromUser) {
		Action actionPerformer = null;
		if (command == null || command.equals("")) {
			return;
		} else if (command.trim().equals("exit")) {
			exit();
		} else if (commandFromUser.trim().equals("undo") && !undoStack.empty()) {
			Action undoAction = undoStack.pop();
			ui.setResult(undoAction.undo());
			ui.showUndoResult(actionPerformer);
		} else {
			actionPerformer = Action.getActionObject(commandFromUser);
			ui.setResult(actionPerformer.execute(commandFromUser));
			ui.showResult(actionPerformer);
			if (actionPerformer.isUndoable()) {
				undoStack.push(actionPerformer);
			}
		}
		if (commandFromUser.toLowerCase().equals("update")) {
			if (ui.getResult().length == 1) {
				exit();
			}
		}
	}

	// public String partialCommand(String command) {
	// return null;
	// }

	/**
	 * 
	 * @return String[] of names of all lists in storage
	 */
	public String[] namesOfAllLists() {
		return DataManager.getListNames();
	}

	/**
	 * Sets the view to the specified value
	 * 
	 * @param view
	 */
	public void setView(View view) {
		this.ui.setView(view);
	}

	/**
	 * Sets the command to the command specified
	 * 
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * executes the command and starts the chain of events
	 */
	@Override
	public void run() {
		executeCommand(command);
	}

	/**
	 * exits the code and closes the UI window
	 */
	public void exit() {
		ui.getView().unregisterProvider();
	}

	/**
	 * 
	 * @return command entered
	 */
	public String getCommand() {
		return command;
	}
}
