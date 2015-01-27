package ca.informi.wagoneer.oo.gameobject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

public class ParticleEffectBox2D extends ParticleEffect {

	final World world;

	public ParticleEffectBox2D(final World world) {
		super();
		this.world = world;
	}

	public ParticleEffectBox2D(final World world, final ParticleEffect effect) {
		super();
		this.world = world;
		final Array<ParticleEmitter> emitters = getEmitters();
		final Array<ParticleEmitter> emitterSource = effect.getEmitters();
		for (int i = 0, n = emitterSource.size; i < n; i++) {
			emitters.add(new ParticleEmitterBox2D(world, emitterSource.get(i)));
		}
	}

	@Override
	public void loadEmitters(final FileHandle effectFile) {
		final Array<ParticleEmitter> emitters = getEmitters();
		final InputStream input = effectFile.read();
		emitters.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input), 512);
			while (true) {
				final ParticleEmitter emitter = new ParticleEmitterBox2D(world, reader);
				emitters.add(emitter);
				if (reader.readLine() == null) break;
				if (reader.readLine() == null) break;
			}
		} catch (final IOException ex) {
			throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}
}
