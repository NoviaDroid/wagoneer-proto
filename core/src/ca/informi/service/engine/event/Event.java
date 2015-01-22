package ca.informi.service.engine.event;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class Event implements Poolable {

	public Entity entity;

	public Entity getEntity() {
		return entity;
	}

	@Override
	public void reset() {
		entity = null;
	}

	void setEntity(final Entity entity) {
		this.entity = entity;
	}

}
