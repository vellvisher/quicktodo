/**
 * 
 * 
 * class CommandBox
 * 
 * The commandBox gets the text entered by the user 
 * and notifies the AppWindow or the View class to carry out the necessary task. 
 * It tracks keyPress and  documentChanges and responds accordingly.  
 * 
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 */

package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import core.Utility;

public class CommandBox extends JTextField implements DocumentListener,
		KeyListener, Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static enum Mode {
		AUTOCOMPLETED, COMPLETION, ENTERED, ACTION
	}

	private Mode mode = Mode.AUTOCOMPLETED;
	private JTextComponent editor;

	private final String[] SUPPORTED_COMMANDS = { "add", "archive", "delete",
			"done", "edit", "exit", "find", "free?", "full", "hide", "help",
			"login", "overdue", "reminder", "schedule", "search", "show",
			"tutorial", "undo" };

	private final String[] LIST_POPDOWN_COMMANDS = { "archive", "done",
			"delete", "edit", "find", "free?", "overdue", "postpone",
			"reminder", "schedule", "search" };
	private static final String[] EVENT_DETAIL_COMMANDS = { "archive", "done",
			"find", "overdue", "reminder", "search" };
	private static final String[] HIDE_TABLE = { "add", "login", "help" };

	private static final String SHOW_PASSWORD_WINDOW = "showPasswordDialog";
	private static final String DELETE_EVENT = "deleteCommand";
	private static final String INCREASE_TABLE_SIZE = "increaseSize";
	private static final String AUTOCOMPLETE_EVENT = "autoCompleteEvent";
	private static final String SHOW_EVENT_DETAIL = "showDialog";
	private static final String SHOW_HELP = "showHelp";
	private static final String USER_COMMAND = "userCommand";
	private static final String CLEAR_VIEW = "clearView";
	private static final String HIDE_TABLE_VIEW = "hideTable";

	private int autoCompletePos;
	private String autoCompleteString;
	private String userCommand;
	private boolean editPostponeMode = false;

	/**
	 * 
	 * initialises the editor
	 */
	public CommandBox() {

		this.setEditable(true);
		editor = this;
		editor.getDocument().addDocumentListener(this);
		editor.addKeyListener(this);
		editor.setFocusTraversalKeysEnabled(false);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	/**
	 * This function is notified everytime the user enters a text or deletes a
	 * text.
	 * 
	 */
	@Override
	public void insertUpdate(DocumentEvent ev) {
		if (ev.getLength() != 1) {
			return;
		}

		int changeLocation = ev.getOffset();
		String userCommand = null;
		userCommand = editor.getText();

		// Return if not beginning of the string
		if (userCommand.indexOf(" ") != -1) {
			return;
		}
		String prefix = userCommand.toLowerCase();

		for (String command : SUPPORTED_COMMANDS) {
			if (command.startsWith(prefix)) {
				this.autoCompleteString = command.substring(prefix.length());
				this.autoCompletePos = changeLocation + 1;
				SwingUtilities.invokeLater(this);
				break;
			}
		}
	}

	/**
	 * Inserts text at index pos to autocomplete.
	 * 
	 * @param editor
	 * @param textToInsert
	 * @param pos
	 */
	private void insertText(JTextComponent editor, String textToInsert, int pos) {
		String currText = editor.getText();
		if (textToInsert == null || pos < 0) {
			return;
		}
		editor.setText(currText.substring(0, pos) + textToInsert
				+ currText.substring(pos, currText.length()));
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}

	/**
	 * This function tracks user key press and depending on the key pressed
	 * fires a corresponding property to execute the action.
	 * 
	 * @param
	 */
	public void keyPressed(KeyEvent userKeyPressed) {

		if (userKeyPressed.getKeyCode() == KeyEvent.VK_ENTER) {
			if (mode == Mode.COMPLETION && !(editor.getSelectedText() == null)) {
				int pos = editor.getSelectionEnd();
				insertText(editor, " ", pos);
				editor.setCaretPosition(pos + 1);
				mode = Mode.AUTOCOMPLETED;
			} else {
				userCommand = editor.getText();
				// mode = Mode.AUTOCOMPLETED;
				firePropertyFunctions(userCommand);
			}
		} else if (userKeyPressed.getKeyCode() == KeyEvent.VK_DELETE) {
			firePropertyChange(DELETE_EVENT, null, "delete");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * 
	 * This function tracks user key release and depending on the key released
	 * fires a corresponding property to execute the action.
	 * 
	 * @param
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		userCommand = editor.getText();

		if (e.getKeyCode() == KeyEvent.VK_DOWN
				|| e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_TAB
				|| (((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) && (e
						.getKeyCode() == KeyEvent.VK_S
						|| e.getKeyCode() == KeyEvent.VK_T
						|| e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_E))) {
			e.consume();
			return;
		}

		for (String command : LIST_POPDOWN_COMMANDS) {
			if (userCommand.equals(command)) {
				firePropertyChange(CLEAR_VIEW, null, userCommand);
			} else if (Utility.getHead(userCommand).equals(command)) {
				if (!editPostponeMode) {
					firePropertyChange(USER_COMMAND, null, userCommand);
				}
			}
		}
	}

	/**
	 * set the mode if the user command is edit and postpone to support
	 * autocomplete.
	 * 
	 */
	public void enableEditingMode() {
		editPostponeMode = true;
	}

	/**
	 * calls autocomplete function
	 */
	@Override
	public void run() {
		autoComplete();
	}

	/**
	 * 
	 * This function is user to autocomplete userString if it is one of the
	 * supported commands
	 */
	private void autoComplete() {
		String completeString = this.autoCompleteString;
		int completePos = this.autoCompletePos;
		insertText(editor, completeString, completePos);
		editor.setCaretPosition(completePos + completeString.length());
		editor.moveCaretPosition(completePos);
		mode = Mode.COMPLETION;
	}

	/**
	 * This function is used to get userCommand
	 * 
	 * @return
	 */
	public String getUserCommand() {
		return userCommand;
	}

	/**
	 * This function is used to fireProperty changes depending on the
	 * userCommand.
	 * 
	 * @param userString
	 */
	public void firePropertyFunctions(String userString) {
		String userCommandHead = Utility.getHead(userCommand);
		if (Arrays.binarySearch(HIDE_TABLE, userCommandHead) >= 0) {
			firePropertyChange(HIDE_TABLE_VIEW, null, userCommand);
		}

		if (userCommand.startsWith("login")) {
			firePropertyChange(SHOW_PASSWORD_WINDOW, null, userCommand);
		} else if (userCommand.startsWith("schedule")) {
			firePropertyChange(AUTOCOMPLETE_EVENT, null, userCommand);
		} else if (userCommand.startsWith("help")) {
			firePropertyChange(SHOW_HELP, null, userCommand);
		} else if (userCommand.startsWith("delete")) {
			firePropertyChange(DELETE_EVENT, userCommandHead, userCommand);
		} else if ((userCommand.startsWith("edit "))
				&& editPostponeMode == false) {
			firePropertyChange(AUTOCOMPLETE_EVENT, null, userCommand);
			mode = Mode.AUTOCOMPLETED;
			editPostponeMode = true;
		} else if (Arrays.binarySearch(EVENT_DETAIL_COMMANDS, userCommandHead) >= 0) {
			firePropertyChange(SHOW_EVENT_DETAIL, null, userCommand);
		} else if (userCommand.startsWith("show ")) {
			firePropertyChange(INCREASE_TABLE_SIZE, null, userCommand);
		} else {
			firePropertyChange(USER_COMMAND, null, userCommand);
			if (userCommand.startsWith("edit")
					|| userCommand.startsWith("postpone ")) {
				editPostponeMode = false;
			}
		}
	}
}
