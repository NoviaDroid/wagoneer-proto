package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Box2DSpriteRenderer extends SpriteRenderer {

	private int layer;
	private final Box2DObject parent;

	public Box2DSpriteRenderer(final Box2DObject parent, final Sprite sprite, final Vector2 size) {
		super(sprite, size);
		this.parent = parent;
		this.layer = Layers.GAME_FOREGROUND;
	}

	@Override
	public void dispose() {
		super.dispose();
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
		return parent.getAngleRadians() * MathUtils.radiansToDegrees;
	}

	@Override
	protected Vector2 getPosition() {
		return parent.getPosition();
	}

}
