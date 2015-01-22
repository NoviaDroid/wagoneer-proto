package ca.informi.service.engine;

import ca.informi.wagoneer.component.Box2DBodyComponent;
import ca.informi.wagoneer.component.PhysicsComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;

public class Box2DDestroySystem extends IteratingSystem implements EntityFamilyListener {

	ComponentMapper<Box2DBodyComponent> bodyMapper = ComponentMapper.getFor(Box2DBodyComponent.class);

	/**
	 * If an entity is removed that has a {@link Box2DBodyComponent}, destroy
	 * the component.
	 */
	@SuppressWarnings("unchecked")
	static Family interestedFamilyRemove = Family.getFor(ComponentType.getBitsFor(Box2DBodyComponent.class), new Bits(), new Bits());

	/**
	 * If an entity exists that has a {@link Box2DBodyComponent} but no
	 * {@link PhysicsComponent}, destroy the {@link Box2DBodyComponent}.
	 */
	@SuppressWarnings("unchecked")
	static Family interestedFamilyProcess = Family.getFor(ComponentType.getBitsFor(Box2DBodyComponent.class), new Bits(),
			ComponentType.getBitsFor(PhysicsComponent.class));

	public Box2DDestroySystem() {
		super(interestedFamilyRemove);
	}

	@Override
	public void entityAdded(final Entity entity) {
	}

	@Override
	public void entityRemoved(final Entity entity) {
		final Box2DBodyComponent bodyComponent = bodyMapper.get(entity);
		Box2DFactory.destroy(bodyComponent);
	}

	@Override
	public Family getInterestedFamily() {
		return interestedFamilyRemove;
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		Box2DFactory.destroy((Box2DBodyComponent) entity.remove(Box2DBodyComponent.class));
	}

}
