package ca.informi.service.engine;

import ca.informi.gdx.ApplicationDelegate;
import ca.informi.service.EntitySystemProvider;
import ca.informi.service.IntervalTimer.Interval;
import ca.informi.service.Service;
import ca.informi.service.engine.event.Event;
import ca.informi.service.engine.event.EventListener;
import ca.informi.service.engine.event.EventPool;
import ca.informi.service.engine.event.EventsPending;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

public class EngineService implements Service {

	PooledEngine engine;
	EventPool eventPool;
	EventsPending eventsPending;
	Array<EventListener<?>> eventListeners;
	Array<EntityListener> entityListeners;

	public void addEntityListener(final EntityListener listener) {
		entityListeners.add(listener);
		engine.addEntityListener(listener);
	}

	public void addEntityListener(final Family family, final EntityListener listener) {
		entityListeners.add(listener);
		engine.addEntityListener(family, listener);
	}

	@Override
	public void adding(final ApplicationDelegate delegate) {
		// Take this opportunity to do some cleanup
		engine.clearPools();

		if (delegate instanceof EntitySystemProvider) {
			final Array<EntitySystem> systems = ((EntitySystemProvider) delegate).getSystems();
			for (final EntitySystem system : systems) {
				engine.addSystem(system);
			}
		}
	}

	public void addSystem(final EntitySystem system) {
		engine.addSystem(system);
	}

	public <T extends Component> T createComponent(final Class<T> klass) {
		return engine.createComponent(klass);
	}

	public Entity createEntity() {
		return engine.createEntity();
	}

	public <T extends Event> T fireEvent(final Class<T> klass, final Entity entity) {
		final T event = eventPool.obtain(klass);
		return eventsPending.add(event, entity);
	}

	public ImmutableArray<Entity> getEntitiesFor(final Family family) {
		return engine.getEntitiesFor(family);
	}

	public <T extends EntitySystem> T getSystem(final Class<T> systemType) {
		return engine.getSystem(systemType);
	}

	public ImmutableArray<EntitySystem> getSystems() {
		return engine.getSystems();
	}

	public void removeAllEntities() {
		engine.removeAllEntities();
	}

	public void removeEntity(final Entity entity) {
		engine.removeEntity(entity);
	}

	public void removeEntityListener(final EntityListener listener) {
		engine.removeEntityListener(listener);
	}

	public void removeSystem(final EntitySystem system) {
		engine.removeSystem(system);
	}

	@Override
	public void removing(final ApplicationDelegate delegate) {
		if (delegate instanceof EntitySystemProvider) {
			final Array<EntitySystem> systems = ((EntitySystemProvider) delegate).getSystems();
			for (final EntitySystem system : systems) {
				engine.removeSystem(system);
			}
		}

		// Take this opportunity to do some cleanup
		engine.clearPools();
	}

	@Override
	public void start() {
		engine = new PooledEngine(100, 10000, 100, 10000);
		eventPool = new EventPool(10, 1000);
		eventsPending = new EventsPending();
		eventListeners = new Array<EventListener<?>>();
	}

	@Override
	public void stop() {
		eventsPending.clear(eventPool);
		eventPool.clear();
		engine.removeAllEntities();
		engine.clearPools();
		engine = null;
	}

	@Override
	public String toString() {
		return engine.toString();
	}

	public void update(final Interval interval) {
		engine.update(interval.dt);
		dispatchPendingEvents();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dispatchPendingEvents() {
		for (final EventListener listener : eventListeners) {
			final Class klass = listener.getEventClass();
			final Array events = eventsPending.get(klass);
			if (events.size == 0) continue;
			listener.handleEvents(events);
		}
		eventsPending.clear(eventPool);
	}
}
