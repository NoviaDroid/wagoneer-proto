package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class ParticleRenderer implements Updatable, Renderable, Disposable {

	private boolean orientedVelocityEnabled;
	private float orientedVelocityVariance;
	protected ParticleEffect effect;
	protected Vector2 offset;
	protected float orientedVelocity;
	protected final GameObject parent;
	protected Vector2 position = new Vector2();
	private float lastScale = 1.f;

	public ParticleRenderer(final GameObject parent, final Vector2 offset, final ParticleEffect effectProto, final float scale,
			final boolean physics) {
		this.parent = parent;
		this.offset = new Vector2(offset);
		this.effect = physics ? new ParticleEffectBox2D(Wagoneer.instance.getWorld(), effectProto) : new ParticleEffect(effectProto);
		setScale(scale);
	}

	@Override
	public void dispose() {
		effect.dispose();
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public boolean isAlwaysVisible() {
		return false;
	}

	@Override
	public void render(final RenderOptions opts) {
		position.set(offset)
				.rotate(parent.getAngleRadians() * MathUtils.radiansToDegrees)
				.add(parent.getPosition());
		effect.setPosition(position.x, position.y);
		effect.draw(opts.batch);
	}

	public void setEmitting(final boolean emitting) {
		if (emitting) {
			effect.start();
			setContinuous(true);
		} else {
			effect.setDuration(500);
			effect.allowCompletion();
		}
	}

	public void setFountainSpeedCone(final float f, final float varianceDegrees) {
		orientedVelocity = f;
		orientedVelocityVariance = varianceDegrees;
		orientedVelocityEnabled = true;
		updateOrientedVelocityAmount();
	}

	public void setFountain(final boolean enabled) {
		orientedVelocityEnabled = enabled;
	}

	public void setScale(final float scale) {
		final float effectiveScale = scale / lastScale;
		if (effectiveScale == 1.f) { return; }
		effect.scaleEffect(effectiveScale);
		lastScale = scale;
	}

	@Override
	public void update(final Interval interval) {
		if (orientedVelocityEnabled) {
			updateOrientedVelocityAngle();
		}
		effect.update(interval.dt);
	}

	private void setContinuous(final boolean continuous) {
		final Array<ParticleEmitter> emitters = effect.getEmitters();
		for (int i = 0; i < emitters.size; ++i) {
			final ParticleEmitter e = emitters.get(i);
			e.setContinuous(continuous);
		}
	}

	private void updateOrientedVelocityAmount() {
		final Array<ParticleEmitter> emitters = effect.getEmitters();
		for (int i = 0; i < emitters.size; ++i) {
			final ParticleEmitter e = emitters.get(i);
			e.getAngle()
				.setActive(true);
			e.getVelocity()
				.setActive(orientedVelocityEnabled);
			if (orientedVelocityEnabled) {
				e.getVelocity()
					.setHigh(orientedVelocity);
			}
		}
	}

	private void updateOrientedVelocityAngle() {
		final Array<ParticleEmitter> emitters = effect.getEmitters();
		final float halfVariance = orientedVelocityVariance * 0.5f;
		final float angle = parent.getAngleRadians() * MathUtils.radiansToDegrees + 90;
		final float low = angle - halfVariance, high = angle + halfVariance;
		for (int i = 0; i < emitters.size; ++i) {
			final ParticleEmitter e = emitters.get(i);
			final ScaledNumericValue angleValue = e.getAngle();
			angleValue.setLow(low);
			angleValue.setHigh(high);
		}
	}
}
