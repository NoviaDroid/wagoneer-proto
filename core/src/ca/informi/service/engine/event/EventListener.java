package ca.informi.service.engine.event;

import com.badlogic.gdx.utils.Array;

public interface EventListener<T extends Event> {

	public Class<T> getEventClass();

	public void handleEvents(Array<T> events);

}
