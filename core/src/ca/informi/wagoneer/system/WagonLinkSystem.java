package ca.informi.wagoneer.system;

import ca.informi.wagoneer.component.Wagon;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class WagonLinkSystem extends IteratingSystem {

	private final ComponentMapper<Wagon> wagonMapper = ComponentMapper.getFor(Wagon.class);

	public WagonLinkSystem() {
		super(Family.getFor(Wagon.class), 100); // after WagonBodyFactorySystem
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final Wagon wagon = wagonMapper.get(entity);
		if (wagon.requestLinkEntity == wagon.linkEntity) return;

	}

}
