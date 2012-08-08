/**
 * HelpScreen
 * 
 * This class is used to create the helpScreen that will be displayed to the user when he types help -enter.
 * It displays an html page for help.
 * 
 *  @author Poornima Muthukumar
 */

package ui.popups;

import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import ui.UiConstants;

public class HelpScreen extends JPanel
{

	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(HelpScreen.class.getName());
	private static final long serialVersionUID = 1L;
	private JDialog dialog;
	private JEditorPane htmlPane;
	private JScrollPane scrollPane;

	public HelpScreen() {

		dialog = new JDialog();
		dialog.setSize(446, 322);
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		scrollPane = new JScrollPane(htmlPane);
		scrollPane.setSize(UiConstants.HELPSCREENSIZE);
		dialog.setContentPane(this);
		setLayout(null);
		this.add(scrollPane);
	}

	/**
	 * this function adds the html link to the editorPane
	 */
	public void displayHelp() {
		java.net.URL helpURL = HelpScreen.class
				.getResource("help/quicktodohelpguide.html");
		if (helpURL != null) {
			try {
				htmlPane.setPage(helpURL);
			} catch (IOException e) {
				logger.error("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			logger.error("Couldn't find file: help/quicktodohelpguide.html");
		}
		dialog.setVisible(true);
		scrollPane.setVisible(true);
		dialog.toFront();

	}

	/**
	 * this function is used to set the visibility of the helpScreen
	 * 
	 * @params a boolean value
	 */
	public void setVisible(boolean b) {
		displayHelp();
	}
}
