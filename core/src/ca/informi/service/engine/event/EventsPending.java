package ca.informi.service.engine.event;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class EventsPending {

	public ObjectMap<Class<? extends Event>, Array<? extends Event>> pending;

	@SuppressWarnings("unchecked")
	public <T extends Event> T add(final T event, final Entity entity) {
		final Array<T> array = (Array<T>) getTypeArray(event.getClass());
		event.setEntity(entity);
		array.add(event);
		return event;
	}

	public void clear(final EventPool pool) {
		final Entries<Class<? extends Event>, Array<? extends Event>> entries = pending.entries();
		for (final Entry<Class<? extends Event>, Array<? extends Event>> entry : entries) {
			final Array<? extends Event> array = entry.value;
			pool.freeAll(array);
			array.clear();
		}
	}

	public <T extends Event> Array<T> get(final Class<T> eventType) {
		return getTypeArray(eventType);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Event> Array<T> getTypeArray(final Class<T> eventType) {
		Array array = pending.get(eventType);
		if (array == null) {
			array = new Array<T>(1);
			pending.put(eventType, array);
		}
		return array;
	}

}
