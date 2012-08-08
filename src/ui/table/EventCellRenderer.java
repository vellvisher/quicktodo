/**
 * This class renders the label with event details to be added to the table.
 * 
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 * 
 */

package ui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;
import ui.UiConstants;
import data.Event;

public class EventCellRenderer implements TableCellRenderer
{

	/**
	 * This function returns a rendered component depending on whether the cell
	 * is a label or a button
	 * 
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Event) {
			return renderEventLabel((Event) value, isSelected);
		} else if (value instanceof EventButton) {
			return (JToggleButton) value;
		}
		return null;
	}

	/**
	 * This function is used to set the text and the format of the label.
	 * 
	 * @param value
	 * @param isSelected
	 * @return eventLabel
	 */
	private Component renderEventLabel(Event value, boolean isSelected) {

		Event event = (Event) value;
		String result = event.toString();

		JLabel eventLabel = new JLabel();
		eventLabel.setAutoscrolls(true);
		eventLabel.setText(result);
		eventLabel.setOpaque(true);
		eventLabel.setFont(UiConstants.FONT);
		if (isSelected) {
			eventLabel.setBackground(UiConstants.COLOR);
			eventLabel.setForeground(Color.black);
		} else {
			eventLabel.setBackground(Color.white);
			eventLabel.setForeground(Color.black);
		}

		return eventLabel;
	}
}
