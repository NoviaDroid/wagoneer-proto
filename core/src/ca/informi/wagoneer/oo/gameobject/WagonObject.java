package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.WagonMessage.WagonMessageData;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class WagonObject extends Box2DObject implements Renderable, Updatable {

	public static int FORE = 0, AFT = 1, LEFT = 2, RIGHT = 3;

	public WagonConnections connections = new WagonConnections(this);

	private final WagonMessageBus bus = new WagonMessageBus(this);

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
	public int getLayer() {
		return renderer.getLayer();
	}

	public Vector2 getSize() {
		return size;
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

	protected void busWillUpdate() {
		// Clear any flags set by commands
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
	protected FixtureDef[] getFixtureDefs(final Vector2 size) {
		final FixtureDef body = new FixtureDef();
		body.friction = 0.2f;
		body.restitution = 0.8f;
		body.density = 1.0f;
		final PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(size.x / 2.f, size.y / 2.f);
		body.shape = bodyShape;

		return new FixtureDef[] { body };
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
