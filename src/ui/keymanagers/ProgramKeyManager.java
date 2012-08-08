/**
 * 
 * class which extends keyEventDispatcher which overwrites
 *  some of the keys of the program to provide extended control over the keys.
 *  @author Vaarnan Drolia
 */

package ui.keymanagers;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;

/**
 * 
 * @author Poornima Muthukumar
 * 
 */
public class ProgramKeyManager implements KeyEventDispatcher
{
	/**
	 * overwrites some of the keys so that the focus does not shift from the
	 * commandBox
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getKeyCode() == 16) {// && ((e.getKeyCode() != KeyEvent.VK_UP) &&
									// (e.getKeyCode() != KeyEvent.VK_UP))) {
			return true;
		}
		if (e.getKeyCode() == 17
				|| (e.getKeyCode() == 27 && !(e.getComponent() instanceof JDialog))) {
			return true;
		}
		return false;
	}

}
