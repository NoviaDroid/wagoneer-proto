package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.WagonMessage.WagonMessageData;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class WagonObject extends Box2DObject implements Renderable, Updatable, ContactListener {

	private static class ContactData {
		boolean isLocalSensor;
		boolean isRemoteSensor;
		GameObject remote;
		short sensedBits;
		short sensingMask;
	}

	public static int FORE = 0, AFT = 1, LEFT = 2, RIGHT = 3;

	public WagonConnections connections = new WagonConnections(this);
	private final WagonMessageBus bus = new WagonMessageBus(this);
	private final ContactData contactData = new ContactData();
	private final WagonObject[] eligibleHitches = new WagonObject[4];
	private final Renderable[] hitches;
	private final Box2DSpriteRenderer renderer;

	private final Vector2 size = new Vector2();

	protected final WagonDelegates delegates = new WagonDelegates();

	public WagonObject(final Sprite sprite, final Vector2 size, final Vector2 position, final float angle) {
		super(size, position, angle);
		this.size.set(size);
		renderer = new Box2DSpriteRenderer(this, sprite, size);
		hitches = createHitchRenderers();
	}

	public void addDelegate(final WagonMessageType type, final WagonDelegate delegate) {
		delegates.add(type, delegate);
	}

	@Override
	public void beginContact(final Contact contact) {
		final ContactData data = getContactData(contact);
		if ((data.sensedBits & FilterBits.HITCH_FILTER_BITS) != 0) {
			setEligibleHitch(data, true);
		}
	}

	@Override
	public void endContact(final Contact contact) {
		final ContactData data = getContactData(contact);
		if ((data.sensedBits & FilterBits.HITCH_FILTER_BITS) != 0) {
			setEligibleHitch(data, false);
		}
	}

	@Override
	public int getLayer() {
		return renderer.getLayer();
	}

	public Vector2 getSize() {
		return size;
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
	}

	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
	}

	public void removeDelegate(final WagonDelegate delegate) {
		delegates.remove(delegate);
	}

	@Override
	public void render(final RenderOptions opts) {
		for (int i = 0; i < hitches.length; ++i) {
			if (hitches[i] == null) continue;
			hitches[i].render(opts);
		}
		renderer.render(opts);
	}

	public void sendMessge(final WagonMessageType type, final WagonMessageData<?> data) {
		receiveMessage(WagonMessage.acquire(type, data));
	}

	@Override
	public void update(final Interval interval) {
		bus.update(interval);
	}

	private Renderable[] createHitchRenderers() {
		final Renderable[] hitchRenderers = new Renderable[4];
		final Vector2 hitchSize = new Vector2(0.33f, 0.33f);
		for (int i = 0; i < 4; ++i) {
			if (willAcceptConnection(i)) {
				final Sprite sprite = Wagoneer.instance.resources.oryxAtlas.object.createSprite("wagon_hitch", i);
				hitchRenderers[i] = new OffsetSpriteRenderer(this, sprite, hitchSize, connections.getHitchOffset(i),
						renderer.getLayer() - 1);
			}
		}
		return hitchRenderers;
	}

	private ContactData getContactData(final Contact contact) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();
		contactData.isLocalSensor = fA.isSensor();
		contactData.isRemoteSensor = fB.isSensor();
		final GameObject oA = (GameObject) fA.getBody()
												.getUserData();
		final GameObject oB = (GameObject) fB.getBody()
												.getUserData();
		contactData.remote = (oA == this ? oB : oA);

		final Fixture sensing = (oA == this ? fA : fB);
		final Fixture sensed = (oA == this ? fB : fA);
		contactData.sensingMask = sensing.getFilterData().maskBits;
		final short sensedCat = sensed.getFilterData().categoryBits;
		contactData.sensedBits = (short) (contactData.sensingMask & sensedCat);
		return contactData;
	}

	private void setEligibleHitch(final ContactData cd, final boolean set) {
		short baseBits = (short) (cd.sensedBits >> FilterBits.HCB0);
		for (int i = 0; i < 4; ++i) {
			if ((baseBits & 0x1) != 0) {
				eligibleHitches[i] = (WagonObject) (set ? cd.remote : null);
			}
			baseBits >>= 1;
		}
	}

	protected void busWillUpdate() {
		// Clear any flags set by commands
	}

	protected FixtureDef createBodyFixture(final Vector2 size) {
		final FixtureDef bodyFixture = new FixtureDef();
		bodyFixture.friction = 0.2f;
		bodyFixture.restitution = 0.8f;
		bodyFixture.density = 1.0f;

		final PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(size.x / 2.f, size.y / 2.f);
		bodyFixture.shape = bodyShape;
		return bodyFixture;
	}

	@Override
	protected void disposeInner() {
		super.disposeInner();
		connections.dispose();
		bus.dispose();
		renderer.dispose();
		delegates.dispose();
	}

	@Override
	protected BodyDef getBodyDef(final Vector2 position, final float angle) {
		final BodyDef def = new BodyDef();
		def.position.set(position);
		def.angularDamping = 2.0f;
		def.angle = angle;
		def.type = BodyType.DynamicBody;
		return def;
	}

	@Override
	protected final Array<FixtureDef> getFixtureDefs(final Vector2 size) {
		final Array<FixtureDef> fds = new Array<FixtureDef>(5);

		fds.add(createBodyFixture(size));

		for (int i = 0; i < 4; ++i) {
			if (!willAcceptConnection(i)) continue;
			final FixtureDef hitchDef = new HitchFixtureDef(i, size);
			fds.add(hitchDef);
		}

		return fds;
	}

	protected void receiveMessage(final WagonMessage message) {
		if (!message.alreadyReceived(this)) {
			delegates.handle(message);
			bus.add(message);
			message.release();
		}
	}

	protected boolean willAcceptConnection(final int direction) {
		return true;
	}
}
