package storage;

import data.Event;

public interface UpdateListener
{
	public void updated(Event oldEvent, Event newEvent);
}
