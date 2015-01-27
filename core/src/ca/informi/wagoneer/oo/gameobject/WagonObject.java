package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.WagonMessage.WagonMessageData;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class WagonObject extends Box2DObject implements Renderable, Updatable, ContactListener {

	public final WagonMessageBus messageBus = new WagonMessageBus(this);
	private final ContactData contactData = new ContactData(this);
	protected final WagonHitches hitches;
	private final Box2DSpriteRenderer renderer;

	private final Vector2 size = new Vector2();

	protected final WagonDelegates delegates = new WagonDelegates();

	public WagonObject(final Sprite sprite, final Vector2 size, final Vector2 position, final float angle) {
		super(size, position, angle);
		this.size.set(size);
		renderer = new Box2DSpriteRenderer(this, sprite, size);
		// needs size from owner, layer from renderer
		hitches = new WagonHitches(this);
	}

	public void addDelegate(final WagonMessageType type, final WagonDelegate delegate) {
		delegates.add(type, delegate);
	}

	@Override
	public void beginContact(final Contact contact) {
		contactData.apply(contact);
		if ((contactData.sensedBits & FilterBits.HITCH_BIT) != 0) {
			final WagonHitch hitch = (WagonHitch) contactData.local.fixtureObject;
			hitch.beginContact((WagonHitch) contactData.remote.fixtureObject);
		}
	}

	@Override
	public void endContact(final Contact contact) {
		contactData.apply(contact);
		if ((contactData.sensedBits & FilterBits.HITCH_BIT) != 0) {
			final WagonHitch hitch = (WagonHitch) contactData.local.fixtureObject;
			hitch.endContact((WagonHitch) contactData.remote.fixtureObject);
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
		hitches.render(opts);
		renderer.render(opts);
	}

	public void sendMessage(final WagonMessageType type, final WagonMessageData<?> data) {
		messageBus.receive(WagonMessage.obtain(type, data));
	}

	@Override
	public void update(final Interval interval) {
		hitches.update(interval);
		messageBus.update(interval);
	}

	protected void busWillUpdate() {
		// Clear any flags set by commands
	}

	protected void createBodyFixtures(final Array<FixtureDef> defs, final Vector2 size) {
		final FixtureDef bodyFixture = new FixtureDef();
		bodyFixture.friction = 0.2f;
		bodyFixture.restitution = 0.8f;
		bodyFixture.density = 1.0f;

		final PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(size.x / 2.f, size.y / 2.f);
		bodyFixture.shape = bodyShape;

		defs.add(bodyFixture);
	}

	@Override
	protected void disposeInner() {
		hitches.dispose();
		messageBus.dispose();
		renderer.dispose();
		delegates.dispose();
		super.disposeInner();
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
		final Array<FixtureDef> fds = new Array<FixtureDef>();
		createBodyFixtures(fds, size);
		return fds;
	}

	protected boolean willAcceptConnection(final int direction) {
		return true;
	}
}
