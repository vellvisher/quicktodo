/**
 * extends Action
 * This class allows the user to sync with Google Calendar
 * @author Vaarnan Drolia
 */
package action;

import googlecalendar.GoogleCalendar;
import parser.Parser;
import storage.DataManager;
import core.Utility;
import data.Event;

public class GoogleCalendarAction extends Action
{

	/**
	 * @param userCommand
	 * @return null always
	 */
	@Override
	public Event[] execute(String userCommand) {

		if (userCommand.trim().equals("logout")) {
			if (DataManager.getGoogleCalendar().logout()) {
				return new Event[1];
			}
			return null;
		}

		String params = userCommand.replace("login ", "");
		String username = Utility.getHead(params);

		params = params.replace(username + " ", "");
		String password = Utility.getHead(params);

		params = params.replace(password, "");

		if ((!Parser.validGoogleUsername(username))
				|| (!Parser.validGooglePassword(password))) {
			return null;
		}

		if (!"".equals(params.trim())) {
			return null;
		}

		GoogleCalendar gcalObj = new GoogleCalendar();
		gcalObj.login(username, password);

		if (gcalObj.isLoggedIn()) {
			DataManager.setGoogleCalendar(gcalObj);
			DataManager.addUpdateListener(gcalObj);
			return (new Event[0]);
		} else {
			return null;
		}
	}

	/**
	 * undo is irrelevant to this class
	 */
	@Override
	public Event[] undo() {
		return null;
	}

	/**
	 * undo is irrelevant to this class
	 */
	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String errorMessage() {
		return "Could not login to Google Calendar Service";
	}

	/**
	 * @return string containing the action being executed in this case,
	 *         googleCalendar
	 */
	@Override
	public String getCommandName() {
		return "googleCalendar";
	}

}
