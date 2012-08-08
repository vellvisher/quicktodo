/**
 * This class is used to add a mouse listener to the table 
 * so that the buttons respond to mouse click and change accordingly.
 * 
 * @author Poornima Muthukumar
 */

package ui.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class TableButtonListener extends MouseAdapter
{
	private final JTable table;

	/**
	 * 
	 * @param table
	 */
	public TableButtonListener(JTable table) {
		this.table = table;
	}

	/**
	 * tracks mouse click and changes button icon accordingly.
	 * 
	 * @param
	 */
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		int column = table.getColumnModel().getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();
		if (row < table.getRowCount() && row >= 0
				&& column < table.getColumnCount() && column >= 0) {
			Object value = table.getValueAt(row, column);
			if (value instanceof EventButton) {
				EventButton button = (EventButton) value;
				button.doClick();
			}
		}
	}
}