/**
 * Table Model which defines the datatype of the Event Table
 * @author Vaarnan Drolia
 */
package ui.table;

import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

import data.Event;

public class EventTableModel extends DefaultTableModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Class[] types = new Class[] { JToggleButton.class, Event.class,
			JToggleButton.class };

	public EventTableModel(String[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
