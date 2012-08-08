/**
 * class EventButton
 * 
 * this class sets the button in the table depending on the event Detail.
 * if it is starred it creates a a yellow star otherwise it creates a blank star.
 * Similarly it creates a green tick and blank tick to display if  it is a done event or not.
 * 
 * @author Vaarnan Drolia
 */

package ui.table;

import javax.swing.Icon;
import javax.swing.JToggleButton;
import data.Event;

public class EventButton extends JToggleButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TYPE {
		STAR, TICK;
	}

	private TYPE type;
	private Event event;

	/**
	 * Sets the visibility of the button and its type
	 * 
	 * @param selected
	 * @param unSelected
	 * @param state
	 * @param type
	 * @param e
	 */
	public EventButton(Icon selected, Icon unSelected, boolean state,
			TYPE type, Event e) {
		super();
		this.setSelectedIcon(selected);
		this.setIcon(unSelected);
		this.setSelected(state);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		this.type = type;
		this.event = e;
	}

	/**
	 * This function is used to get the event .
	 * 
	 * @return event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * this function returns the type of the button whether it ia a tick or a
	 * star.
	 * 
	 * @return type
	 */
	public TYPE getType() {
		return type;
	}
}
