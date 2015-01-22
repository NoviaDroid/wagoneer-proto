package ca.informi.service.engine;

import ca.informi.service.Services;
import ca.informi.wagoneer.component.Box2DBodyComponent;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DFactory {

	public static void destroy(final Box2DBodyComponent bodyComponent) {
		final World world = Services.instance.get(World.class);
		final Body body = bodyComponent.body;
		for (final Joint joint : bodyComponent.ownedJoints) {
			world.destroyJoint(joint);
		}
		bodyComponent.ownedJoints.clear();
		world.destroyBody(body);
	}

}
