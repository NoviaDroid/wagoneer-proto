package ca.informi.wagoneer;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class DelegatingContactListener implements ContactListener {
	@Override
	public void beginContact(final Contact contact) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();
		final Object oA = fA.getBody()
							.getUserData();
		final Object oB = fB.getBody()
							.getUserData();
		if (oA instanceof ContactListener) {
			((ContactListener) oA).beginContact(contact);
		}
		if (oB instanceof ContactListener) {
			((ContactListener) oB).beginContact(contact);
		}
	}

	@Override
	public void endContact(final Contact contact) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();
		final Object oA = fA.getBody()
							.getUserData();
		final Object oB = fB.getBody()
							.getUserData();
		if (oA instanceof ContactListener) {
			((ContactListener) oA).endContact(contact);
		}
		if (oB instanceof ContactListener) {
			((ContactListener) oB).endContact(contact);
		}
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();
		final Object oA = fA.getBody()
							.getUserData();
		final Object oB = fB.getBody()
							.getUserData();
		if (oA instanceof ContactListener) {
			((ContactListener) oA).postSolve(contact, impulse);
		}
		if (oB instanceof ContactListener) {
			((ContactListener) oB).postSolve(contact, impulse);
		}
	}

	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();
		final Object oA = fA.getBody()
							.getUserData();
		final Object oB = fB.getBody()
							.getUserData();
		if (oA instanceof ContactListener) {
			((ContactListener) oA).preSolve(contact, oldManifold);
		}
		if (oB instanceof ContactListener) {
			((ContactListener) oB).preSolve(contact, oldManifold);
		}
	}
}