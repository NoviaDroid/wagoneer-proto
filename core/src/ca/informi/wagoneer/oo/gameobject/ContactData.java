package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ContactData {
	public static class ContactObject {
		public Object bodyObject;
		public Object fixtureObject;
		public Body body;
		public Fixture fixture;

		public void populate(final Fixture fixture) {
			this.fixture = fixture;
			fixtureObject = fixture.getUserData();
			body = fixture.getBody();
			bodyObject = body.getUserData();
		}
	}

	private final ContactObject a = new ContactObject();

	private final ContactObject b = new ContactObject();
	public ContactObject remote;
	public ContactObject local;
	private final GameObject localObj;
	public short sensedBits;

	public short sensingMask;

	public ContactData(final GameObject local) {
		this.localObj = local;
	}

	public void apply(final Contact contact) {
		a.populate(contact.getFixtureA());
		b.populate(contact.getFixtureB());
		if (a.bodyObject == localObj) {
			local = a;
			remote = b;
		} else if (b.bodyObject == localObj) {
			local = b;
			remote = a;
		} else {
			local = remote = null;
			return;
		}
		sensingMask = local.fixture.getFilterData().maskBits;
		final short sensedCat = remote.fixture.getFilterData().categoryBits;
		sensedBits = (short) (sensingMask & sensedCat);
	}

}