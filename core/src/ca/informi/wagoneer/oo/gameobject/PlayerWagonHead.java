package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class PlayerWagonHead extends Box2DObject implements Renderable {

	private static BodyDef getBodyDef(final Vector2 position, final float angle) {
		final BodyDef def = new BodyDef();
		def.position.set(position);
		def.angle = angle;
		def.type = BodyType.DynamicBody;
		return def;
	}

	private static FixtureDef getFixtureDef() {
		final PolygonShape shape = new PolygonShape();
		shape.setAsBox(2.f, 2.f);

		final FixtureDef def = new FixtureDef();
		def.friction = 0.2f;
		def.restitution = 0.8f;
		def.shape = shape;

		return def;
	}

	private final Box2DSpriteRenderer renderer;

	public PlayerWagonHead(final Vector2 position, final float angle) {
		super(getBodyDef(position, angle), getFixtureDef());
		renderer = new Box2DSpriteRenderer(this, "ship_head", Layers.GAME_FOREGROUND);
	}

	@Override
	public int getLayer() {
		return renderer.getLayer();
	}

	@Override
	public void render(final RenderOptions opts) {
		renderer.render(opts);
	}

}
