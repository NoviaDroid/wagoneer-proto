package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class DebrisEmitter extends GameObject implements Updatable, Renderable {

	private static final int DEBRIS_COUNT = 100;
	private static final int MAX_REPOSITION_COUNT = Math.max(DEBRIS_COUNT / 20, 1);
	private static final float REPOSITION_PERIMETER = 1.05f;
	private static final float VIEWPORT_MARGIN = 1.25f;

	private final Rectangle bbxInner = new Rectangle();
	private final Rectangle bbxOuter = new Rectangle();

	private final SpriteAnimation[] debris = new SpriteAnimation[DEBRIS_COUNT];
	private final Array<Array<Sprite>> prototypes = new Array<Array<Sprite>>();
	private final Vector2 zero = new Vector2();

	public DebrisEmitter() {
		prototypes.add(Wagoneer.instance.resources.oryxAtlas.object.createSprites("asteroid_small"));
		prototypes.add(Wagoneer.instance.resources.oryxAtlas.object.createSprites("asteroid_medium"));
	}

	@Override
	public float getAngleRadians() {
		return 0;
	}

	@Override
	public int getLayer() {
		return Layers.GAME_FOREGROUND + 100;
	}

	@Override
	public Vector2 getPosition() {
		return zero;
	}

	@Override
	public boolean isAlwaysVisible() {
		return true;
	}

	@Override
	public void render(final RenderOptions opts) {
		bbxInner.setSize(opts.camera.viewportWidth, opts.camera.viewportHeight);
		bbxInner.setCenter(opts.camera.position.x, opts.camera.position.y);
		bbxOuter.setSize(opts.camera.viewportWidth * VIEWPORT_MARGIN, opts.camera.viewportHeight * VIEWPORT_MARGIN);
		bbxOuter.setCenter(opts.camera.position.x, opts.camera.position.y);

		int repositionCount = 0;
		for (int i = 0; i < debris.length; ++i) {
			SpriteAnimation anim = debris[i];
			// Create new if not present
			if (anim == null) {
				anim = new SpriteAnimation(prototypes.get(MathUtils.random(prototypes.size - 1)));
				anim.setScale(0.04f);
				anim.setFrame(MathUtils.random(anim.getFrameCount() - 1));
				position(anim);
				debris[i] = anim;
			}
			// Cull escaped
			final Rectangle bounds = anim.getFrameSprite()
											.getBoundingRectangle();
			if (!bbxOuter.contains(bounds) && repositionCount < MAX_REPOSITION_COUNT) {
				reposition(anim);
				++repositionCount;
			}
			anim.getFrameSprite()
				.draw(opts.batch);
		}
	}

	@Override
	public void setAngleRadians(final float angle) {
		throw new GdxRuntimeException("Not supported");
	}

	@Override
	public void setPosition(final Vector2 position) {
		throw new GdxRuntimeException("Not supported");
	}

	@Override
	public void update(final Interval interval) {
		for (final SpriteAnimation a : debris) {
			if (a == null) continue;
			a.update(interval);
		}
	}

	private void position(final SpriteAnimation animation) {
		animation.setPosition(MathUtils.random(bbxOuter.x, bbxOuter.x + bbxOuter.width),
				MathUtils.random(bbxOuter.y, bbxOuter.y + bbxOuter.height));
	}

	private void reposition(final SpriteAnimation anim) {
		// Pick a point along the perimeter of bbxInner
		final float width = bbxInner.width * REPOSITION_PERIMETER, height = bbxInner.height * REPOSITION_PERIMETER;
		final float halfWidth = width / 2.f, halfHeight = height / 2.f;
		final float cx = bbxInner.x + halfWidth;
		final float cy = bbxInner.y + halfHeight;
		final float left = cx - halfWidth, right = left + width;
		final float bottom = cy - halfHeight, top = bottom + height;
		final int edge = MathUtils.random(3);

		float x, y;
		if (edge % 2 == 0) {
			// left or right
			x = (edge < 2) ? left : right;
			y = bottom + MathUtils.random(height);
		} else {
			// bottom or top
			x = left + MathUtils.random(width);
			y = (edge < 2) ? bottom : top;
		}

		anim.setPosition(x, y);
	}

	@Override
	protected void disposeInner() {
	}
}
