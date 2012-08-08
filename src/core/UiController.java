/**
 * Manipulates the UI to show the result of the action
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 * 
 */
package core;

import java.util.Hashtable;

import action.Action;
import action.Add;
import action.CheckFree;
import action.DataMiner;
import action.Delete;
import action.Edit;
import action.Find;
import action.FindExec;
import action.GoogleCalendarAction;
import action.Overdue;
import action.Schedule;
import action.Toggle;

import ui.View;
import data.Event;

public class UiController
{
	private Event[] result;
	/**
	 * @return the result
	 */

	private View view;
	private Hashtable<String, String> successMessages;
	private Thread resultBoxThread;
	String[][] commands = { { "add", "added" }, { "delete", "deleted" },
			{ "free?", "Yup! Timeslot is free..." }, { "edit", "edited" },
			{ "postpone", "postponed" }, { "schedule", "Free slots found!" },
			{ "undo", "undid" }, { "login", "Logged in to Google Calendar" },
			{ "update", "You are running the latest version :-)" } };

	/**
	 * Constructor for the UI Controller
	 */
	public UiController() {
		successMessages = new Hashtable<String, String>();
		for (int i = 0; i < commands.length; i++) {
			successMessages.put(commands[i][0], commands[i][1]);
		}
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * sets the view object
	 * 
	 * @param view
	 *            object
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * 
	 * @return the result as an event array
	 */
	public Event[] getResult() {
		return result;
	}

	/**
	 * sets the result
	 * 
	 * @param the
	 *            result to set
	 */
	public void setResult(Event[] result) {
		this.result = result;
	}

	/**
	 * Manipulates the result and displays the result in the result box
	 * 
	 * @param actionPerformer
	 */
	public void showResult(Action actionPerformer) {

		if (actionPerformer instanceof FindExec) {
			if (result == null) {
				view.showOnlyBox();
				return;
			}
			FindExec action = (FindExec) actionPerformer;
			if (action instanceof Edit) {
				handleEdit(action);
			} else if (action instanceof Delete) {
				handleDelete(action);
			}
		} else if (actionPerformer instanceof Schedule) {
			if (result != null && result.length == 0) {
				showResultBox("Sorry, no free slots found...");
				view.showOnlyBox();
			} else if (result != null) {
				showResultBox(successMessages.get(actionPerformer
						.getCommandName()));
				view.showEventsInList(result);
			}
		} else if (actionPerformer instanceof CheckFree) {
			if (result != null && result.length == 0) {
				showResultBox(successMessages.get(actionPerformer
						.getCommandName()));
				view.showOnlyBox();
			} else if (result != null) {
				showResultBox("Sorry, Clashing events found...");
				view.showEventsInList(result);
			}
		} else if (result == null) {
			view.showOnlyBox();
			showResultBox(actionPerformer.errorMessage());
		} else if (actionPerformer instanceof Add) {
			view.showOnlyBox();
			view.clearBoxText();
			showResultBox(formatEvents(result, actionPerformer.getCommandName()));
		} else if (actionPerformer instanceof GoogleCalendarAction) {
			String resultString = successMessages.get("login");
			showResultBox(resultString);
		} else if (actionPerformer instanceof Toggle) {
			view.updateMultipleEventsInList(result);
		} else if (actionPerformer instanceof DataMiner) {
			if (result.length >= 1) {
				view.showEventsInList(result);
			} else {
				view.showOnlyBox();
			}
		} else {
			if (result.length > 1) {
				view.showEventsInList(result);
			}
		}
	}

	/**
	 * formats the result String
	 * 
	 * @param resultEvents
	 * @param commandName
	 * @return formatted result String
	 */
	private String formatEvents(Event[] resultEvents, String commandName) {
		String eventName = resultEvents[0].getName();

		if (result.length > 1) {
			int i;
			for (i = 1; i < result.length - 1; i++) {
				eventName += ", " + result[i].getName();
			}
			eventName += " and " + result[i++].getName();
		}

		return String.format("%1$s %2$s successfully", eventName,
				successMessages.get(commandName));
	}

	/**
	 * gives user delete functionality with feedback and appropriate result
	 * 
	 * @param action
	 */
	private void handleDelete(FindExec action) {
		Delete deleteAction = (Delete) action;
		if (deleteAction.isExecuting()) {
			showResultBox(formatEvents(result, deleteAction.getCommandName()));
			Event[] emptyArray = new Event[result.length];
			view.updateMultipleEventsInList(emptyArray);
		} else if (result.length > 0) {
			view.showEventsInList(result);
		}
	}

	/**
	 * gives user edit functionality with feedback and appropriate result
	 * 
	 * @param action
	 */
	private void handleEdit(FindExec action) {
		Edit editAction = (Edit) action;
		if (editAction.isExecuting()) {
			showResultBox("You can start editing now...");
			String autoCompleteText = "edit" + " ";
			autoCompleteText += result[0].toString();
			view.setBoxText(autoCompleteText);
			return;
		} else if (editAction.isEdited()) {
			showResultBox("Edited successfully...");
			view.setBoxText("");
			view.updateMultipleEventsInList(result);
		} else if (result.length > 0) {
			view.showEventsInList(result);
		}
	}

	/**
	 * displays the result
	 * 
	 * @param resultString
	 */
	private void showResultBox(String resultString) {
		if (resultBoxThread != null && resultBoxThread.isAlive()) {

			resultBoxThread.interrupt();
		}

		resultBoxThread = new Thread(view.displayResult(resultString));
		resultBoxThread.setDaemon(true);
		resultBoxThread.start();
	}

	/**
	 * displays result for undo action
	 * 
	 * @param actionPerformer
	 */
	public void showUndoResult(Action actionPerformer) {
		if (result == null) {
			showResultBox("Could not undo.");
		} else {
			showResultBox(formatEvents(result, "undo"));
			view.showOnlyBox();
			view.clearBoxText();
		}

	}
}