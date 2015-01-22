package ca.informi.service;

import ca.informi.gdx.ApplicationDelegate;
import ca.informi.service.IntervalTimer.Interval;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

public class EngineService extends ApplicationDelegate implements Service {

	PooledEngine engine = new PooledEngine(100, 10000, 100, 10000);

	public void addEntityListener(EntityListener listener) {
		engine.addEntityListener(listener);
	}

	public void addEntityListener(Family family, EntityListener listener) {
		engine.addEntityListener(family, listener);
	}

	@Override
	public void adding(ApplicationDelegate delegate) {
		// Take this opportunity to do some cleanup
		engine.clearPools();

		if (delegate instanceof EntitySystemProvider) {
			Array<EntitySystem> systems = ((EntitySystemProvider) delegate).getSystems();
			for (EntitySystem system : systems) {
				engine.addSystem(system);
			}
		}
	}

	public void addSystem(EntitySystem system) {
		engine.addSystem(system);
	}

	public <T extends PoolableComponent> T createComponent(Class<T> klass) {
		return engine.createComponent(klass);
	}

	public Entity createEntity() {
		return engine.createEntity();
	}

	public ImmutableArray<Entity> getEntitiesFor(Family family) {
		return engine.getEntitiesFor(family);
	}

	public <T extends EntitySystem> T getSystem(Class<T> systemType) {
		return engine.getSystem(systemType);
	}

	public ImmutableArray<EntitySystem> getSystems() {
		return engine.getSystems();
	}

	public void removeAllEntities() {
		engine.removeAllEntities();
	}

	public void removeEntity(Entity entity) {
		engine.removeEntity(entity);
	}

	public void removeEntityListener(EntityListener listener) {
		engine.removeEntityListener(listener);
	}

	public void removeSystem(EntitySystem system) {
		engine.removeSystem(system);
	}

	@Override
	public void removing(ApplicationDelegate delegate) {
		if (delegate instanceof EntitySystemProvider) {
			Array<EntitySystem> systems = ((EntitySystemProvider) delegate).getSystems();
			for (EntitySystem system : systems) {
				engine.removeSystem(system);
			}
		}

		// Take this opportunity to do some cleanup
		engine.clearPools();
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public String toString() {
		return engine.toString();
	}

	@Override
	public void update(Interval interval) {
		engine.update(interval.dt);
	}
}
