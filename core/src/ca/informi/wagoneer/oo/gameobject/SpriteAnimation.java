package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpriteAnimation implements Updatable {

	private static class SpriteAnimationFrame {
		Vector2 offset;
		Sprite sprite;

		public SpriteAnimationFrame(final Sprite sprite) {
			super();
			this.sprite = sprite;
			this.offset = new Vector2(-sprite.getWidth() / 2.f, -sprite.getHeight() / 2.f);
		}

		public SpriteAnimationFrame(final Sprite sprite, final Vector2 offset) {
			super();
			this.sprite = sprite;
			this.offset = offset;
		}

	}

	private final Array<SpriteAnimationFrame> animation;
	private int frame;
	private float frameTime;
	private final Vector2 position = new Vector2();

	private float rotation;
	private float scale;
	private float timeAccum;

	public SpriteAnimation(final Array<Sprite> animation) {
		this(animation, 1.f / 24.f);
	}

	public SpriteAnimation(final Array<Sprite> animation, final Array<Vector2> frameOffsets, final float frameTime) {
		this.frameTime = frameTime;
		this.animation = new Array<SpriteAnimationFrame>(animation.size);
		for (int i = 0; i < animation.size; ++i) {
			this.animation.add(new SpriteAnimationFrame(animation.get(i), frameOffsets.get(i)));
		}
	}

	public SpriteAnimation(final Array<Sprite> animation, final float frameTime) {
		this.frameTime = frameTime;
		this.animation = new Array<SpriteAnimationFrame>(animation.size);
		for (int i = 0; i < animation.size; ++i) {
			this.animation.add(new SpriteAnimationFrame(animation.get(i)));
		}
	}

	public void advanceFrame() {
		frame = (frame + 1) % animation.size;
	}

	public int getFrame() {
		return frame;
	}

	public int getFrameCount() {
		return animation.size;
	}

	public Sprite getFrameSprite() {
		final SpriteAnimationFrame frameObj = animation.get(frame);
		final Sprite sprite = frameObj.sprite;
		sprite.setScale(scale);
		sprite.setRotation(rotation);
		sprite.setPosition(position.x + frameObj.offset.x, position.y + frameObj.offset.y);
		return sprite;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public Vector2 getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setFrame(final int frameIndex) {
		frame = frameIndex;
	}

	public void setFrameTime(final float frameTime) {
		this.frameTime = frameTime;
	}

	public void setPosition(final float x, final float y) {
		position.set(x, y);
	}

	public void setPosition(final Vector2 v) {
		position.set(v);
	}

	public void setRotation(final float v) {
		this.rotation = v;
	}

	public void setScale(final float f) {
		scale = f;
	}

	@Override
	public void update(final Interval interval) {
		timeAccum += interval.dt;
		while (timeAccum > frameTime) {
			timeAccum -= frameTime;
			advanceFrame();
		}
	}

}
