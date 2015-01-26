package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class PlayerWagonHead extends Box2DObject implements Renderable {

	private static float aHitchRadius = 0.2f;
	private static Vector2 hitchOffset = new Vector2(0.f, -1.f - aHitchRadius);
	private static Vector2 size = new Vector2(2.f, 2.f);

	private static BodyDef getBodyDef(final Vector2 position, final float angle) {
		final BodyDef def = new BodyDef();
		def.position.set(position);
		def.angle = angle;
		def.type = BodyType.DynamicBody;
		return def;
	}

	private static FixtureDef[] getFixtureDefs() {

		final FixtureDef body = new FixtureDef();
		body.friction = 0.2f;
		body.restitution = 0.8f;
		body.density = 1.0f;
		final PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(size.x / 2.f, size.y / 2.f);
		body.shape = bodyShape;

		final FixtureDef hitch = new FixtureDef();
		body.friction = 0.6f;
		body.restitution = 0.4f;
		body.density = 1.0f;
		final CircleShape hitchShape = new CircleShape();
		hitchShape.setPosition(hitchOffset);
		hitchShape.setRadius(aHitchRadius);
		hitch.shape = hitchShape;

		return new FixtureDef[] { body, hitch };
	}

	private final Box2DSpriteRenderer renderer;

	public PlayerWagonHead(final Vector2 position, final float angle) {
		super(getBodyDef(position, angle), getFixtureDefs());
		renderer = new Box2DSpriteRenderer(game, this, game.resources.oryxAtlas.o.createSprite("fighter_green"), size);
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
