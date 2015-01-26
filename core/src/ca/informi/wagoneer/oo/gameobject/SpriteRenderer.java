package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class SpriteRenderer implements Renderable {

	protected final Vector2 centerOffset;
	protected final Vector2 pos = new Vector2();
	protected final Sprite sprite;
	protected final Vector2 spriteSize;

	public SpriteRenderer(final Sprite sprite, final Vector2 drawSize) {
		this.sprite = sprite;
		spriteSize = new Vector2(this.sprite.getWidth(), this.sprite.getHeight());
		centerOffset = new Vector2(spriteSize).scl(-0.5f);
		this.sprite.setScale(drawSize.x / spriteSize.x, drawSize.y / spriteSize.y);
	}

	@Override
	public void render(final RenderOptions opts) {
		pos.set(getPosition()).add(centerOffset);
		final float angle = getAngleDegrees();
		sprite.setRotation(angle);
		sprite.setPosition(pos.x, pos.y);
		sprite.draw(opts.batch);
	}

	protected abstract float getAngleDegrees();

	protected abstract Vector2 getPosition();

}
