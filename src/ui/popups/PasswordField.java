/**
 * PasswordField 
 * 
 * This class is used to create the dialogbox which will create the login screen for the user when he presses login-enter.
 * It will have a username field and password field.
 * 
 * @author Poornima Muthukumar
 */

package ui.popups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import ui.UiConstants;

public class PasswordField extends JPanel implements ActionListener,
		FocusListener, KeyListener

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String OK = "Ok";
	private static String CANCEL = "Cancel";

	private JDialog passwordDialog;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton ok;
	private JButton cancel;
	private JLabel userName;
	private JLabel password;

	/**
	 * initialization
	 */
	public PasswordField() {
		passwordDialog = new JDialog();
		passwordDialog.setAlwaysOnTop(true);
		passwordDialog.setContentPane(this);
		passwordDialog.setSize(360, 211);
		passwordDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		userName = renderLabel(userName, " Username");
		userName.setBounds(33, 20, 89, 33);
		add(userName);
		password = renderLabel(password, " Password");
		password.setBounds(31, 72, 93, 33);
		add(password);
		usernameField = new JTextField();
		usernameField.setFont(UiConstants.TEXT_FONT);
		usernameField.setBorder(UiConstants.LIGHT_BLUE_BORDER);
		usernameField.setBounds(148, 18, 184, 32);
		add(usernameField);
		usernameField.setColumns(10);
		passwordField = new JPasswordField();
		passwordField.setFont(UiConstants.TEXT_FONT);
		passwordField.setBorder(UiConstants.LIGHT_BLUE_BORDER);
		passwordField.setBounds(148, 74, 183, 30);
		add(passwordField);
		ok = renderButton(ok, OK);
		ok.setBounds(81, 131, 65, 23);
		add(ok);
		cancel = renderButton(cancel, CANCEL);
		cancel.setBounds(189, 130, 81, 23);
		add(cancel);
		usernameField.addFocusListener(this);

		passwordField.addFocusListener(this);
		passwordField.addKeyListener(this);
	}

	/**
	 * 
	 * @param button
	 * @param buttonName
	 * @return created button
	 */
	private JButton renderButton(JButton button, String buttonName) {
		button = new JButton(buttonName);
		button.setBorder(UiConstants.LABEL_BORDER);
		button.addActionListener(this);
		return button;
	}

	/**
	 * 
	 * @param label
	 * @param labelName
	 * @return created label
	 */
	private JLabel renderLabel(JLabel label, String labelName) {

		label = new JLabel(labelName);
		label.setFont(UiConstants.FONT);
		label.setBorder(UiConstants.LABEL_BORDER);
		label.setOpaque(true);
		label.setBackground(UiConstants.COLOR);
		return label;
	}

	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
		passwordDialog.setVisible(value);
		passwordDialog.requestFocus();
		passwordDialog.setLocation(100, 100);

		if (value) {
			passwordDialog.toFront();
		}
	}

	/**
	 * This function supports actionListener It tracks ButtonPress and updates
	 * the view accordingly.
	 * 
	 * @param ActionEvent
	 *            Object
	 */

	public void actionPerformed(ActionEvent arg0) {
		if (OK.equals(arg0.getActionCommand())) {
			String username = usernameField.getText();
			char[] password = passwordField.getPassword();
			if (username != null && password != null) {
				firePropertyChange("usernamePassword", null, username + " "
						+ new String(password));
				password = null;
				passwordDialog.dispose();
			}
		} else if (CANCEL.equals(arg0.getActionCommand())) {
			passwordDialog.dispose();
		}
	}

	/**
	 * This function sets the border of the usernamefield depending on the focus
	 * 
	 * @param
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		((JComponent) arg0.getComponent()).setBorder(UiConstants.LABEL_BORDER);
	}

	/**
	 * This function sets the border of the passwordfield depending on the focus
	 */
	@Override
	public void focusLost(FocusEvent arg0) {
		((JComponent) arg0.getComponent())
				.setBorder(UiConstants.LIGHT_BLUE_BORDER);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			ok.doClick();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
