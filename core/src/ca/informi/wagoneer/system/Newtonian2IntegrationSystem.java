package ca.informi.wagoneer.system;

import ca.informi.wagoneer.component.Newtonian2;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class Newtonian2IntegrationSystem extends IteratingSystem {

	private final ComponentMapper<Newtonian2> newtonianMapper = ComponentMapper.getFor(Newtonian2.class);
	private Vector2 temp;

	public Newtonian2IntegrationSystem() {
		super(Family.getFor(Newtonian2.class));
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final Newtonian2 n = newtonianMapper.get(entity);
		n.position.add(temp.set(n.velocity)
							.scl(deltaTime));

		n.rotation += n.torque * deltaTime;
	}

}
