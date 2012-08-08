/**
 * class View
 * 
 * This class acts as an intermediate between controller and AppWindow. 
 * It initialises the window and passes the command received from the AppWindow to the controller.
 * 
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 * 
 */

package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import ui.keymanagers.GlobalHotKeyListener;
import ui.keymanagers.GlobalKeyboardGrabber;
import ui.popups.ResultBox;
import ui.table.EventButton;
import core.Controller;
import data.Event;

public class View implements PropertyChangeListener, Runnable, ActionListener
{
	private static Controller controller;
	private static AppWindow window;
	private static ComponentMover cm;
	private static ResultBox resultBox;

	/**
	 * 
	 * this function is used to respond to short key press.
	 */
	public View() {
		EventQueue.invokeLater(this);

		try {
			GlobalKeyboardGrabber.setup();

			GlobalHotKeyListener listener = new GlobalHotKeyListener() {
				public void onHotKey() {
					// if (window != null && window.isListAreaVisible()) {
					//
					// } window.setTableVisible(true);
					window.showBoxOnScreen();
				}
			};
			GlobalKeyboardGrabber.addHotKeyListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * sets the controller object
	 * 
	 * @param cont
	 */
	public void setController(Controller cont) {
		controller = cont;
	}

	/**
	 * This function is used to respond to property changes depending on the
	 * changed property.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String changedProperty = evt.getPropertyName();
		if ("userCommand".equals(changedProperty)) {
			String userCommand = (String) evt.getNewValue();
			if ("hide".equals(userCommand.trim())) {
				window.setBoxVisible(false);
				window.setTableVisible(false);
				clearBoxText();
			} else {
				controller.setCommand(userCommand);
				SwingUtilities.invokeLater(controller);
			}
		} else if ("clearView".equals(changedProperty)) {
			showOnlyBox();
		}
	}

	/**
	 * This function is used to call the function which will add the result to
	 * the table.
	 * 
	 * @param resultEvents
	 */
	public void showEventsInList(Event[] resultEvents) {
		addToList(resultEvents);
		window.setTableVisible(true);
	}

	/**
	 * This function is used to add the result to the table.
	 * 
	 * @param resultEvents
	 */
	public void addToList(Event[] resultEvents) {
		window.addEventsToList(resultEvents);
	}

	/**
	 * Initialise the window,resultBox,componentMover.
	 * 
	 * 
	 */
	public void run() {

		window = new AppWindow();
		window.setBoxVisible(true);
		window.setTableVisible(false);
		window.setCommandListener(this);
		window.setParent(this);
		window.setButtonListener(this);
		cm = new ComponentMover();
		cm.registerComponent(window.getParentComponent());
		executeControllerCommand("reminder ");
		resultBox = new ResultBox(window);
	}

	/**
	 * This function is used to execute the string entered by the user after he
	 * presses enter.
	 * 
	 * @param userCommand
	 */
	public void executeControllerCommand(String userCommand) {
		controller.setCommand(userCommand);
		SwingUtilities.invokeLater(controller);
	}

	/**
	 * Remove global shortcut key for program.
	 */
	public void unregisterProvider() {
		GlobalKeyboardGrabber.shutdown();
		window.getParentComponent().dispose();
	}

	/**
	 * This function is used to display the result in the resultBox.
	 * 
	 * @param result
	 * @return resultBox
	 */
	public ResultBox displayResult(String result) {
		resultBox.setText(result);
		return resultBox;
	}

	/**
	 * This function is used to set only the box to visible mode.
	 */
	public void showOnlyBox() {
		window.setOnlyBoxVisible();
	}

	/**
	 * This function is used to toggle the button icon .
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof EventButton) {
			EventButton selectedButton = (EventButton) e.getSource();
			Event event = selectedButton.getEvent();
			String command = "toggle "
					+ selectedButton.getType().name().toLowerCase() + " "
					+ event.getId();
			executeControllerCommand(command);
		}
	}

	/**
	 * 
	 * This function clears the commandBox text.
	 */
	public void clearBoxText() {
		setBoxText("");
	}

	/**
	 * updates the events displayed in the table after an action has been
	 * performed.
	 * 
	 * @param result
	 */
	public void updateMultipleEventsInList(Event[] result) {
		window.updateMultipleEventsInLists(result);
	}

	/**
	 * This Function is used to get the get entered by the user in the command
	 * box.
	 * 
	 * @return commandBox text .
	 */
	public String getBoxText() {
		return window.getBoxText();
	}

	/**
	 * This function is used to set the text to be displayed in the command Box.
	 * 
	 * @param text
	 */
	public void setBoxText(String text) {
		window.setBoxText(text);
	}

}