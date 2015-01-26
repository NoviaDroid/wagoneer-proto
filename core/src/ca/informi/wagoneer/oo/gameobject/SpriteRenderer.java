package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class SpriteRenderer implements Renderable, Disposable {

	protected final Vector2 pos = new Vector2();
	protected final Sprite sprite;
	protected final Vector2 spriteSize;

	public SpriteRenderer(final Sprite sprite, final Vector2 drawSize) {
		this.sprite = sprite;
		spriteSize = new Vector2(this.sprite.getWidth(), this.sprite.getHeight());
		this.sprite.setScale(drawSize.x / spriteSize.x, drawSize.y / spriteSize.y);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render(final RenderOptions opts) {
		final float angle = getAngleDegrees();
		final Vector2 pos = getPosition();
		sprite.setRotation(angle);
		sprite.setCenter(pos.x, pos.y);
		sprite.draw(opts.batch);
	}

	protected abstract float getAngleDegrees();

	protected abstract Vector2 getPosition();

}
