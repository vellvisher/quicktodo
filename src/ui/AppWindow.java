/**
 * Main AppWindow visibile to the user
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 */

package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import data.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import core.Utility;

import ui.keymanagers.ProgramKeyManager;
import ui.popups.EventDetailPanel;
import ui.popups.HelpScreen;
import ui.popups.PasswordField;
import ui.table.EventTable;
import java.awt.event.KeyEvent;

public class AppWindow implements PropertyChangeListener
{
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private static Logger logger = Logger.getLogger(AppWindow.class.getName());
	private AbstractAction listDownAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
				eventList.setSelectedMultipleIndexDown();
			} else {
				eventList.setSelectedIndexDown();
			}
		}
	};
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction listUpAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
				eventList.setSelectedMultipleIndexUp();
			} else {
				eventList.setSelectedIndexUp();
			}
		}
	};
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction listTabShiftUpAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			eventList.setSelectedIndexUp();
		}
	};

	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction tickAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			multipleEventsAction("toggle tick");
		}
	};
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction starAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			multipleEventsAction("toggle star");
		}
	};
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction deleteAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			multipleEventsAction("delete");
		}
	};
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction editAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			((CommandBox) commandBox).enableEditingMode();
			editEvent();
		}
	};
	/**
	 * function for the action to be performed for the respective inputMap keys
	 */
	private AbstractAction showEventDetails = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int row = eventList.getSelectedIndex();
			if (row != -1) {
				Event event = (Event) eventList.getValueAtIndex(row, 1);
				showEventDetails(event);
			}
		}
	};

	private JFrame frame;
	private JTextField commandBox;
	private EventTable eventList;
	private View view;
	private Event[] resultEvents;
	private JPanel panelBox;
	private JScrollPane tableScrollPane;
	private static final Image taskbarIcon = createImage(UiConstants.TASKBAR_IMAGE);
	private static final ImageIcon leftButtonIcon = createImageIcon(UiConstants.COMMANDBOX_IMAGE);
	private JPanel detailEventDescPane;
	private JPanel passwordPane;
	private JPanel helpScreen;
	private JLabel todoIcon;
	private static final String LISTDOWN_ACTION = "listDownAction";
	private static final String LISTUP_ACTION = "listUpAction";
	private static final String LIST_TABSHIFTUP_ACTION = "listTabShiftUpAction";
	private static final String STAR_ACTION = "starAction";
	private static final String TICK_ACTION = "tickAction";
	private static final String DELETE_ACTION = "deleteAction";
	private static final String EDIT_ACTION = "editAction";
	private static final String SHOW_EVENT_DETAILS = "showEventDetails";

	/**
	 * @wbp.nonvisual location=21,139
	 */

	public AppWindow() {
		initialize();
	}

	/**
	 * this function creates the image
	 * 
	 * @param path
	 * @return image
	 */
	private static BufferedImage createImage(String path) {
		java.net.URL imgURL = AppWindow.class.getResource(path);
		if (imgURL != null) {
			try {
				return ImageIO.read(imgURL);
			} catch (IOException e) {
				logger.error("Image not found");
				e.printStackTrace();
			}
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
		return null;
	}

	/**
	 * initializes the frame and its components
	 */
	private void initialize() {
		initializeFrame();
		initializeCommandBox();
		intializeToDoIcon();
		eventList = new EventTable();
		intializePanelBoxForIconAndCommandBox();
		intializeDetailEventPane();
		((JComponent) frame.getContentPane()).setBorder(UiConstants.COMPOUND);
		intializeScrollPane();
		setupInputMaps();
		frame.setVisible(true);
	}

	/**
	 * intializes the detailEventPane
	 */
	private void intializeDetailEventPane() {
		detailEventDescPane = new EventDetailPanel(frame);
		detailEventDescPane.setBorder(UiConstants.LINE_BORDER);
		detailEventDescPane.setBackground(Color.white);
		detailEventDescPane.setForeground(Color.BLUE);
	}

	/**
	 * intializes panelbox for icon and commandbox
	 */
	private void intializePanelBoxForIconAndCommandBox() {
		panelBox = new JPanel();
		panelBox.setBackground(Color.white);
		panelBox.setBorder(UiConstants.COMPOUND);
		panelBox.setBackground(Color.white);

		panelBox.setLocation(UiConstants.PANELBOXLOCATION);
		panelBox.setSize(UiConstants.PANELBOXSIZE);
		panelBox.setLayout(null);
		panelBox.add(commandBox);
		panelBox.add(todoIcon);
		frame.getContentPane().add(panelBox);
	}

	/**
	 * initalizes the todo icon
	 */
	private void intializeToDoIcon() {
		todoIcon = new JLabel(leftButtonIcon);
		todoIcon.setBorder(UiConstants.LINE_BORDER);
		todoIcon.setBounds(UiConstants.TODOICON_SIZE);
	}

	/**
	 * initializes the scrollPane
	 */
	private void intializeScrollPane() {
		tableScrollPane = new JScrollPane(eventList.getComponent());
		tableScrollPane.setBorder(UiConstants.LINE_BORDER);
		;
		tableScrollPane.setVisible(false);
		tableScrollPane.setFont(UiConstants.COMMANDBOX_FONT);
		tableScrollPane.setBackground(Color.WHITE);
		tableScrollPane.setSize(UiConstants.PANELLISTSIZE);
		tableScrollPane.setLocation(UiConstants.PANELLISTLOCATION);
		frame.getContentPane().add(tableScrollPane);

	}

	/**
	 * initializes the command box
	 */
	private void initializeCommandBox() {
		commandBox = new CommandBox();
		commandBox.setBounds(UiConstants.COMMANDBOXBOUNDS);
		commandBox.setFont(UiConstants.COMMANDBOX_FONT);
		commandBox.setBackground(Color.white);
		commandBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		commandBox.setBorder(UiConstants.LINE_BORDER);
		commandBox.addPropertyChangeListener(this);
	}

	/**
	 * initializes the frame
	 */
	private void initializeFrame() {
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.getContentPane().setBackground(Color.white);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(UiConstants.FRAMESIZEBOX);
		frame.setLocation(UiConstants.FRAMELOCATION);
		frame.setIconImage(taskbarIcon);
		frame.setAlwaysOnTop(true);
	}

	/**
	 * Redefine the short cut key for the program to provide extended
	 * functionality such as tab, s tarring, ticking, etc to different
	 * components
	 */
	private void setupInputMaps() {
		KeyEventDispatcher dispatcher = new ProgramKeyManager();
		panelBox.setFocusTraversalKeysEnabled(false);
		frame.getContentPane().setFocusTraversalKeysEnabled(false);
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(dispatcher);

		setupInputMap(commandBox
				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
		setupInputMap(eventList.getComponent().getInputMap());

		setupActionMaps(commandBox.getActionMap());
		setupActionMaps(eventList.getComponent().getActionMap());

		commandBox.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_MASK),
				LISTDOWN_ACTION);
		commandBox.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_MASK),
				LISTUP_ACTION);

		eventList.getComponent().getActionMap()
				.put(SHOW_EVENT_DETAILS, showEventDetails);
		eventList
				.getComponent()
				.getInputMap()
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						SHOW_EVENT_DETAILS);
	}

	/**
	 * redefine the action map to include mappings between the action events and
	 * their keys
	 * 
	 * @param actionMap
	 */
	private void setupActionMaps(ActionMap actionMap) {
		actionMap.put(LISTDOWN_ACTION, listDownAction);
		actionMap.put(LISTUP_ACTION, listUpAction);
		actionMap.put(LIST_TABSHIFTUP_ACTION, listTabShiftUpAction);
		actionMap.put(STAR_ACTION, starAction);
		actionMap.put(TICK_ACTION, tickAction);
		actionMap.put(DELETE_ACTION, deleteAction);
		actionMap.put(EDIT_ACTION, editAction);
	}

	/**
	 * mapping from the keyboard input keys to the action map key
	 * 
	 * @param inputMap
	 */
	private void setupInputMap(InputMap inputMap) {
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
				STAR_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK),
				TICK_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
				LISTDOWN_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0),
				LISTDOWN_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), LISTUP_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
				KeyEvent.SHIFT_MASK, false), LIST_TABSHIFTUP_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
				DELETE_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK),
				DELETE_ACTION);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK),
				EDIT_ACTION);
	}

	/**
	 * sets the visibility of the box on screen
	 */
	public void showBoxOnScreen() {
		panelBox.setVisible(false);
		panelBox.setVisible(true);
		frame.setVisible(false);
		frame.setVisible(true);
		setBoxFocus();
	}

	/**
	 * creates the image
	 * 
	 * @param path
	 * @return image
	 */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = AppWindow.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * sets the box either visible or invisible
	 * 
	 * @param b
	 */
	public void setBoxVisible(boolean b) {
		panelBox.setVisible(b);
		frame.setVisible(b);
	}

	/**
	 * sets the table either visible or invisible
	 * 
	 * @param b
	 */
	public void setTableVisible(boolean b) {
		tableScrollPane.setVisible(b);
		if (b) {
			frame.setSize(UiConstants.FRAMESIZEFULL);
		} else {
			frame.setSize(UiConstants.FRAMESIZEBOX);
		}
		commandBox.requestFocus();
	}

	/**
	 * shows the details of the event in the eventDetailBox
	 * 
	 * @param e
	 */
	public void showEventDetails(Event e) {
		((EventDetailPanel) detailEventDescPane).setMessage(e);
		detailEventDescPane.setVisible(true);
		detailEventDescPane.setLocation(frame.getLocationOnScreen().x,
				frame.getLocationOnScreen().y + 210);
	}

	/**
	 * sets password pane visible
	 * 
	 */
	public void setPasswordpaneVisible() {
		passwordPane = new PasswordField();
		passwordPane.setVisible(true);
		passwordPane.setLocation(100, 200);
		passwordPane.addPropertyChangeListener(this);
	}

	/**
	 * sets the help screen to visible mode
	 * 
	 */
	private void setHelpScreenVisible() {

		helpScreen = new HelpScreen();
		helpScreen.setVisible(true);
	}

	/**
	 * 
	 * @return boolean true if panelBox is visible false otherwise
	 */

	public boolean isBoxVisible() {
		return panelBox.isVisible();
	}

	/**
	 * adds propertyChangeListener to the command box.
	 * 
	 * @param listener
	 */
	public void setCommandListener(PropertyChangeListener listener) {
		commandBox.addPropertyChangeListener(listener);
	}

	/**
	 * adds events to listTable
	 * 
	 * @param resultEvents
	 */
	public void addEventsToList(Event[] resultEvents) {
		this.resultEvents = resultEvents;
		eventList.addEventsToList(resultEvents);
	}

	/**
	 * returns parent component
	 * 
	 * @return frame
	 */
	public JFrame getParentComponent() {
		return frame;
	}

	/**
	 * 
	 * handles the property changes thrown by the command box.
	 * 
	 * 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String changedProperty = evt.getPropertyName();
		if ("usernamePassword".equals(changedProperty)) {
			view.executeControllerCommand("login " + (String) evt.getNewValue());
		}
		if ("deleteCommand".equals(changedProperty)) {
			int index = eventList.getSelectedIndex();
			if (index != -1) {
				multipleEventsAction("delete");
			}
		}
		if ("autoCompleteEvent".equals(changedProperty)) {
			String userCommand = (String) evt.getNewValue();
			String keyword = Utility.getHead(userCommand);
			if (keyword.equals("edit") && (eventList.getSelectedIndex() != -1)) {
				editEvent();
			} else if (keyword.equals("schedule")
					&& (eventList.getSelectedIndex() != -1)) {
				scheduleEvent();
			} else {
				singleEventCommand((String) evt.getNewValue());
			}
		}
		if ("showPasswordDialog".equals(changedProperty)) {
			setPasswordpaneVisible();
		}
		if ("increaseSize".equals(changedProperty)) {
			String command = (String) evt.getNewValue();
			String n = Utility.removeFirstWord(command);
			try {
				changeSize(Integer.parseInt(n));
				clearBoxText();
			} catch (NumberFormatException e) {
				view.executeControllerCommand(command);
			}
		}
		if ("showDialog".equals(changedProperty)) {
			int row = eventList.getSelectedIndex();
			if (row != -1 && tableScrollPane.isVisible()) {
				String userCommand = (String) evt.getNewValue();
				commandBox.setText(userCommand);
				Event e = (Event) eventList.getValueAtIndex(row, 1);
				showEventDetails(e);
			}
		}
		if ("showHelp".equals(changedProperty)) {
			setHelpScreenVisible();
		}

		if ("refreshList".equals(changedProperty)) {
			// view.executeControllerCommand((String)evt.getNewValue());
		}
		if ("hideTable".equals(changedProperty)) {
			setOnlyBoxVisible();
		}
	}

	/**
	 * used to change size of the table dynamically.
	 * 
	 * @param n
	 */
	private void changeSize(int n) {
		if (n >= 5 && n <= 10) {
			UiConstants.PANELLISTSIZE.setSize(420, n * 40);

			tableScrollPane.setSize(UiConstants.PANELLISTSIZE.width,
					UiConstants.PANELLISTSIZE.height);
			tableScrollPane
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			UiConstants.FRAMESIZEFULL.setSize(UiConstants.PANELBOXSIZE.width,
					UiConstants.PANELBOXSIZE.height
							+ UiConstants.PANELLISTSIZE.height);
		}
	}

	/**
	 * handles multiple selection in the table
	 * 
	 * @param type
	 */
	private void multipleEventsAction(String type) {
		int[] rows = eventList.getSelectedIndices();

		if (rows.length == 0) {
			return;
		}

		StringBuilder userCommand = new StringBuilder(type);
		for (int row : rows) {
			Event event = (Event) eventList.getValueAtIndex(row, 1);
			userCommand.append(" " + event.getId());
		}
		view.executeControllerCommand(userCommand.toString());
	}

	/**
	 * this function sends the id of the event to be deleted to the edit class
	 * 
	 * @param type
	 */
	public void scheduleEvent() {
		int row = eventList.getSelectedIndex();
		if (row != -1) {
			Event e = (Event) eventList.getValueAtIndex(row, 1);
			String autoCompleteText = "add "
					+ e.toString().replace("free slot at ", "");
			commandBox.setText(autoCompleteText);
		}
	}

	/**
	 * this function sends the id of the event to be deleted to the edit class
	 * 
	 * @param type
	 */
	public void editEvent() {
		int row = eventList.getSelectedIndex();
		if (row != -1) {
			Event e = (Event) eventList.getValueAtIndex(row, 1);
			String commandExecute = "edit" + " " + e.getId();
			view.executeControllerCommand(commandExecute);
		}
	}

	/**
	 * this function updates the events in the listTable
	 * 
	 * @param events
	 */
	public void updateMultipleEventsInLists(Event[] events) {
		int[] rows = eventList.getSelectedIndices();
		Event event;
		for (int i = 0; i < rows.length; i++) {

			event = events[i];
			if (event == null) {
				eventList.removeValueAtRow(rows[i] - i);
			} else {
				eventList.setValueAtRow(rows[i], event);
			}
		}
	}

	/**
	 * Checks if the result is a valid event when the result is just one and
	 * executes it.
	 * 
	 * @param command
	 * 
	 */

	private void singleEventCommand(String command) {
		String partialCommand = Utility.removeFirstWord(command);
		String eventId = partialCommandCheck(partialCommand);
		if (eventId != null) {
			String commandExecute = Utility.getHead(command) + " " + eventId;
			view.executeControllerCommand(commandExecute);
		}
	}

	/**
	 * 
	 * @param partialCommand
	 * @return event id;
	 */
	private String partialCommandCheck(String partialCommand) {

		if (resultEvents != null && resultEvents.length == 1) {
			return resultEvents[0].getId();
		}
		return null;
	}

	/**
	 * 
	 * @param userCommand
	 * @return the commandTypeString;
	 */
	public static String getHead(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	/**
	 * 
	 * @param view
	 */
	public void setParent(View view) {
		this.view = view;
	}

	/**
	 * 
	 * @return selected index in the table
	 */
	public int getIndex() {
		return eventList.getSelectedIndex();
	}

	/**
	 * 
	 * @return panelBox
	 */
	public Component getBox() {
		return panelBox;
	}

	/**
	 * sets the focus to commandBox
	 * 
	 */
	public void setBoxFocus() {
		commandBox.requestFocusInWindow();
		commandBox.requestFocus();
	}

	/**
	 * sets the box visible
	 * 
	 */
	public void setOnlyBoxVisible() {
		setBoxVisible(true);
		setTableVisible(false);
		frame.setSize(UiConstants.FRAMESIZEBOX);
	}

	/**
	 * 
	 * @return panelBoxSize
	 */
	public Dimension getBoxSize() {
		return UiConstants.PANELBOXSIZE;
	}

	/**
	 * 
	 * @return frame location
	 */
	public Point getFrameLocation() {
		return frame.getLocation();
	}

	/**
	 * attaches a buttonListener to the table.
	 * 
	 * @param listener
	 */
	public void setButtonListener(ActionListener listener) {
		eventList.setButtonListener(listener);
	}

	/**
	 * 
	 * sets commandBox text to ""
	 */
	public void clearBoxText() {
		commandBox.setText("");
	}

	/**
	 * 
	 * @return CommandBox text
	 */
	public String getBoxText() {
		return commandBox.getText();
	}

	/**
	 * 
	 * @param text
	 */
	public void setBoxText(String text) {
		commandBox.setText(text);
	}
}
