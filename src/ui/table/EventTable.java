/**
 * Table of all events which are available
 * @author Poornima Muthukumar
 * @author Vaarnan Drolia
 */
package ui.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import ui.AppWindow;
import ui.UiConstants;
import ui.table.EventButton.TYPE;
import data.Event;

public class EventTable
{
	private static final String imagePathTick = "images/tick.png";
	private static final String imagePathUnTick = "images/untick.png";
	private static final String imagePathStar = "images/star.png";
	private static final String imagePathUnStar = "images/unstar.png";

	private static final ImageIcon imageStar = AppWindow
			.createImageIcon(imagePathStar);
	private static final ImageIcon imageTick = AppWindow
			.createImageIcon(imagePathTick);
	private static final ImageIcon imageUnStar = AppWindow
			.createImageIcon(imagePathUnStar);
	private static final ImageIcon imageUnTick = AppWindow
			.createImageIcon(imagePathUnTick);

	private JTable table;
	private DefaultTableModel tableModel;
	private TableCellRenderer tableRenderer;
	private ActionListener buttonListener;

	public EventTable() {
		tableRenderer = new EventCellRenderer();
		tableModel = new EventTableModel(
				new String[] { "Star", "Event", "Tick" }, 0);
		table = new JTable();
		table.setModel(tableModel);
		table.setDefaultRenderer(Event.class, tableRenderer);
		table.setDefaultRenderer(EventButton.class, tableRenderer);
		table.setTableHeader(null);
		TableButtonListener tableListener = new TableButtonListener(table);
		table.addMouseListener(tableListener);
		table.setSelectionBackground(Color.black);
		table.setSelectionForeground(Color.black);
		table.setColumnSelectionAllowed(false);
		table.setRowHeight(UiConstants.ROW_HEIGHT);
		table.setShowGrid(false);
		TableColumn starColumn = table.getColumnModel().getColumn(0);
		starColumn.setPreferredWidth(UiConstants.BUTTON_COLUMN_WIDTH);
		starColumn.setCellRenderer(tableRenderer);

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getColumnModel().getColumn(1)
				.setPreferredWidth(UiConstants.EVENT_COLUMN_WIDTH);
		table.getColumnModel().getColumn(2)
				.setPreferredWidth(UiConstants.BUTTON_COLUMN_WIDTH);
		table.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
		table.setVisible(true);

		setVisibleRowCount();
	}

	public JTable getComponent() {
		return table;
	}

	public void setVisibleRowCount() {
		Dimension size = table.getPreferredScrollableViewportSize();
		table.setPreferredScrollableViewportSize(new Dimension(Math.min(
				table.getPreferredSize().width, size.width), size.height));

	}

	private void clearTable() {
		int size = table.getRowCount();
		for (int i = 0; i < size; i++) {
			tableModel.removeRow(0);
		}
	}

	public void addElement(Event e) {
		if (e != null) {
			EventButton starButton = new EventButton(imageStar, imageUnStar,
					e.getStarred(), TYPE.STAR, e);
			EventButton tickButton = new EventButton(imageTick, imageUnTick,
					e.getTicked(), TYPE.TICK, e);
			tableModel.addRow(new Object[] { starButton, e, tickButton });
			starButton.addActionListener(buttonListener);
			tickButton.addActionListener(buttonListener);
		}
	}

	public void addEventsToList(Event[] resultEvents) {
		clearTable();

		for (Event e : resultEvents) {
			addElement(e);
		}
	}

	public void setSelectedIndexDown() {

		if (table.getRowCount() == 0) {
			return;
		}
		if (getSelectedIndex() < table.getRowCount() - 1) {
			table.setRowSelectionInterval(getSelectedIndex() + 1,
					getSelectedIndex() + 1);
		} else {
			table.setRowSelectionInterval(0, 0);
		}
		table.scrollRectToVisible(table
				.getCellRect(getSelectedIndex(), 0, true));

	}

	public void setSelectedIndexUp() {

		if (table.getRowCount() == 0) {
			return;
		}

		if (getSelectedIndex() > 0) {
			table.setRowSelectionInterval(getSelectedIndex() - 1,
					getSelectedIndex() - 1);
		} else {
			table.setRowSelectionInterval(table.getRowCount() - 1,
					table.getRowCount() - 1);
		}
		table.scrollRectToVisible(table
				.getCellRect(getSelectedIndex(), 0, true));

	}

	public void setSelectedMultipleIndexDown() {

		int[] selectedRows = table.getSelectedRows();
		if (selectedRows.length == 0) {
			setSelectedIndexDown();
			return;
		}

		int lastSelectedIndex = selectedRows[selectedRows.length - 1];

		if (lastSelectedIndex < table.getRowCount() - 1) {
			table.setRowSelectionInterval(selectedRows[0],
					lastSelectedIndex + 1);
			table.scrollRectToVisible(table.getCellRect(lastSelectedIndex + 1,
					0, true));
		}
	}

	public void setSelectedMultipleIndexUp() {

		int[] selectedRows = table.getSelectedRows();
		if (selectedRows.length == 0) {
			setSelectedIndexUp();
			return;
		}
		int firstSelectedIndex = selectedRows[0];
		if (firstSelectedIndex > 0) {
			table.setRowSelectionInterval(selectedRows[0] - 1,
					selectedRows[selectedRows.length - 1]);
			table.scrollRectToVisible(table.getCellRect(selectedRows[0] - 1, 0,
					true));
		}
	}

	public int getSelectedIndex() {
		return table.getSelectedRow();
	}

	public int[] getSelectedIndices() {
		return table.getSelectedRows();
	}

	public int getSelectedColumn() {
		return table.getSelectedColumn();
	}

	public Object getValueAtIndex(int row, int column) {
		return tableModel.getValueAt(row, column);
	}

	public void setValueAtRow(int row, Event event) {
		if (event != null) {
			EventButton starButton = new EventButton(imageStar, imageUnStar,
					event.getStarred(), TYPE.STAR, event);
			EventButton tickButton = new EventButton(imageTick, imageUnTick,
					event.getTicked(), TYPE.TICK, event);
			tableModel.setValueAt(starButton, row, 0);
			tableModel.setValueAt(event, row, 1);
			tableModel.setValueAt(tickButton, row, 2);
			starButton.addActionListener(buttonListener);
			tickButton.addActionListener(buttonListener);
		}
	}

	public void removeValueAtRow(int row) {
		tableModel.removeRow(row);
	}

	public void setButtonListener(ActionListener listener) {
		buttonListener = listener;
	}
}
