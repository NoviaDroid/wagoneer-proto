package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class OffsetSpriteRenderer extends SpriteRenderer {

	private final int layer;
	private final Vector2 offset;
	private final GameObject parent;
	private final Vector2 position;

	public OffsetSpriteRenderer(final GameObject parent, final Sprite sprite, final Vector2 size, final Vector2 offset, final int layer) {
		super(sprite, size);
		this.offset = new Vector2(offset);
		this.parent = parent;
		this.layer = layer;
		position = new Vector2();
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public boolean isAlwaysVisible() {
		return parent.isAlwaysVisible();
	}

	@Override
	protected float getAngleDegrees() {
		return parent.getAngleRadians() * MathUtils.radiansToDegrees;
	}

	@Override
	protected Vector2 getPosition() {
		return position.set(offset)
						.rotate(parent.getAngleRadians() * MathUtils.radiansToDegrees)
						.add(parent.getPosition());
	}

}
