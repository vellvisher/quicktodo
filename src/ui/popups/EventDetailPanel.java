/**
 * class EventDetailPanel
 * 
 * This class is used to create the dialog box 
 * which will display complete event details when
 *  the user presses enter after selecting an event.
 *  The details are displayed in different font style and font colour to support clarity.
 * 
 * 
 * @author Poornima Muthukumar
 */

package ui.popups;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.apache.log4j.Logger;

import ui.UiConstants;

import core.Utility;

import data.Event;
import data.EventDateTime;

public class EventDetailPanel extends JPanel implements KeyListener
{
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(EventDetailPanel.class.getName());
	private static final long serialVersionUID = 1L;
	private JTextPane noteBox;
	private JScrollPane scrollPane;
	private JDialog dialog;

	public EventDetailPanel(Frame window) {
		this.setSize(UiConstants.NOTE_SIZE);
		noteBox = new JTextPane();
		noteBox.setSize(UiConstants.NOTE_SIZE);
		noteBox.setEditable(false);
		scrollPane = new JScrollPane(noteBox);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(UiConstants.LINE_BORDER);
		dialog = new JDialog(window);
		dialog.setResizable(false);
		dialog.setContentPane(this);
		dialog.addKeyListener(this);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setUndecorated(true);
		dialog.setSize(UiConstants.NOTE_SIZE);
		setLayout(null);
		scrollPane.setSize(UiConstants.NOTE_SIZE);
		this.add(scrollPane);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * function tracks user key press and triggers and event accordingly.
	 * 
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			dialog.requestFocus();
			dialog.setVisible(false);
		}
	}

	/**
	 * function to set the visibility of the dialog box.
	 * 
	 * @param value
	 */
	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
		dialog.setVisible(value);
		dialog.requestFocus();
		scrollPane.setVisible(value);
		if (value) {
			dialog.toFront();
		}
	}

	/**
	 * sets the location of the dialog box
	 * 
	 * @param x
	 *            coordinate, y coordinate
	 */
	@Override
	public void setLocation(int x, int y) {
		dialog.setLocation(x, y);
	}

	/**
	 * this function is used to call the function which generates the formatted
	 * event String for every event.
	 * 
	 * @param e
	 */
	public void setMessage(Event event) {

		ArrayList<String> resultFormat = event.toStringArrayList();
		noteBox.setText("");
		createStyle(resultFormat);
	}

	/**
	 * Generates the formated String .
	 * 
	 * @param resultFormat
	 */

	private void createStyle(ArrayList<String> resultFormat) {
		StyledDocument doc = noteBox.getStyledDocument();
		addStylesToDocument(doc);

		String[] initStyles = { "bold", "regular" };

		try {
			for (int i = 0; i < resultFormat.size(); i++) {
				doc.insertString(doc.getLength(), resultFormat.get(i),
						doc.getStyle(initStyles[i % 2]));
			}
		} catch (BadLocationException ble) {
			logger.error("Couldn't insert initial text into text pane.");
		}
	}

	/**
	 * This function is used to define the styles for the text.
	 * 
	 * @param doc
	 */
	protected void addStylesToDocument(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, UiConstants.FONT_TYPE);
		StyleConstants.setForeground(def, Color.RED);
		StyleConstants.setFontSize(def, 14);

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setForeground(s, Color.black);

		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);
	}

}
