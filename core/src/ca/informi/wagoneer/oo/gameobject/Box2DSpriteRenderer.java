package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.Wagoneer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Box2DSpriteRenderer extends SpriteRenderer {

	private int layer;
	private final Box2DObject parent;

	public Box2DSpriteRenderer(final Wagoneer game, final Box2DObject parent, final String textureName) {
		super(game.resources.oryxAtlas.o.createSprite("hauler_white"));
		this.parent = parent;
		this.layer = Layers.GAME_FOREGROUND;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public boolean isAlwaysVisible() {
		return parent.isAlwaysVisible();
	}

	public void setLayer(final int layer) {
		this.layer = layer;
	}

	@Override
	protected float getAngleDegrees() {
		return parent.getAngle() * MathUtils.radiansToDegrees;
	}

	@Override
	protected Vector2 getPosition() {
		return parent.getPosition();
	}

}
