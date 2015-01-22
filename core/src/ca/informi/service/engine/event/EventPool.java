package ca.informi.service.engine.event;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

public class EventPool {
	private final ObjectMap<Class<? extends Event>, ReflectionPool<? extends Event>> pools;
	private final int initialSize;
	private final int maxSize;

	public EventPool(final int initialSize, final int maxSize) {
		this.pools = new ObjectMap<Class<? extends Event>, ReflectionPool<? extends Event>>();
		this.initialSize = 0;
		this.maxSize = 0;
	}

	public void clear() {
		for (final Pool<? extends Event> pool : pools.values()) {
			pool.clear();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void free(final Event object) {
		if (object == null) { throw new IllegalArgumentException("object cannot be null."); }

		final ReflectionPool pool = pools.get(object.getClass());

		if (pool == null) { return; // Ignore freeing an object that was never
									// retained.
		}

		pool.free(object);
	}

	public void freeAll(final Array<? extends Event> objects) {
		if (objects == null) throw new IllegalArgumentException("objects cannot be null.");

		for (int i = 0, n = objects.size; i < n; i++) {
			final Event object = objects.get(i);
			if (object == null) continue;
			free(object);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Event> T obtain(final Class<T> type) {
		ReflectionPool pool = pools.get(type);

		if (pool == null) {
			pool = new ReflectionPool(type, initialSize, maxSize);
			pools.put(type, pool);
		}

		return (T) pool.obtain();
	}
}
