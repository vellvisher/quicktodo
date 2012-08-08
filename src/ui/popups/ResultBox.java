/**
 * 
 * class ResultBox
 * This class is used to create the pop up result box on top.
 * 
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 */

package ui.popups;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ui.AppWindow;
import ui.UiConstants;

public class ResultBox extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension panelSize;

	private AppWindow window;
	private JLabel resultLabel;
	private JDialog dialog;
	private Point frameLocation;

	/**
	 * initialize the components of the resultBox
	 * 
	 * @param window
	 */
	public ResultBox(AppWindow window) {
		dialog = new JDialog(window.getParentComponent(), false);
		Dimension boxSize = window.getBoxSize();
		panelSize = new Dimension(boxSize.width, boxSize.height - 18);
		this.resultLabel = new JLabel();
		formatLabel();
		this.resultLabel.setFont(UiConstants.FONT);
		add(this.resultLabel);
		dialog.setContentPane(this);
		setSize(panelSize);
		setBackground(Color.WHITE);
		setBorder(UiConstants.LINE_BORDER);
		dialog.setSize(panelSize);
		dialog.setUndecorated(true);
		this.window = window;
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * sets text in the resultBox
	 * 
	 * @param window
	 * @param result
	 */
	public ResultBox(AppWindow window, String result) {
		this(window);
		resultLabel.setText(result);
	}

	/**
	 * this function is used to format the label.
	 * 
	 */
	private void formatLabel() {
		resultLabel.setFont(UiConstants.FONT);
		resultLabel.setBackground(Color.WHITE);
		resultLabel.setForeground(Color.black);
		resultLabel.setSize(panelSize);
	}

	/**
	 * Displays resultBox on screen.
	 */
	@Override
	public void run() {
		resultLabel.setVisible(true);
		setVisible(true);
		dialog.setVisible(true);
		frameLocation = new Point(window.getParentComponent()
				.getLocationOnScreen());
		dialog.setLocation(frameLocation.x, frameLocation.y - panelSize.height);
		window.setBoxFocus();
		dialog.transferFocus();
		try {
			Thread.sleep(2000);
			dialog.setVisible(false);
		} catch (InterruptedException e) {

		} finally {
			dialog.setVisible(false);
		}
	}

	/**
	 * Sets the text in the resultbox
	 * 
	 * @param result
	 */

	public void setText(String result) {
		resultLabel.setText(result);
	}

}
