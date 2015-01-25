package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class SpriteRenderer implements Renderable {

	protected final Sprite sprite;

	public SpriteRenderer(final Sprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void render(final RenderOptions opts) {
		final Vector2 position = getPosition();
		final float angle = getAngleDegrees();
		sprite.setPosition(position.x, position.y);
		sprite.setRotation(angle);
		sprite.draw(opts.batch);
	}

	protected abstract float getAngleDegrees();

	protected abstract Vector2 getPosition();

}
