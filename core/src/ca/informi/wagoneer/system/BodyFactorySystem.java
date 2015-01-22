package ca.informi.wagoneer.system;

import ca.informi.service.Services;
import ca.informi.service.engine.EngineService;
import ca.informi.wagoneer.component.Box2DBodyComponent;
import ca.informi.wagoneer.component.PhysicsComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Bits;

public class BodyFactorySystem extends IteratingSystem {

	ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

	public BodyFactorySystem() {
		super(Family.getFor(ComponentType.getBitsFor(PhysicsComponent.class), new Bits(),
				ComponentType.getBitsFor(Box2DBodyComponent.class)));
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final World world = Services.instance.get(World.class);
		final EngineService engine = Services.instance.get(EngineService.class);
		final PhysicsComponent physicsComponent = physicsMapper.get(entity);
		final Box2DBodyComponent bodyComponent = engine.createComponent(Box2DBodyComponent.class);
		final BodyDef bodyDef = physicsComponent.bodyDef;
		bodyComponent.body = world.createBody(bodyDef);
		for (final FixtureDef fixture : physicsComponent.fixtures) {
			bodyComponent.body.createFixture(fixture);
		}
	}
}
