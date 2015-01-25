package ca.informi.gdx.delegate.controller;

import ca.informi.gdx.delegate.ApplicationDelegate;
import ca.informi.gdx.delegate.IntervalTimer;
import ca.informi.gdx.delegate.LocaleService;
import ca.informi.gdx.delegate.ResourceService;
import ca.informi.gdx.delegate.SpriteBatchDelegate;
import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.gdx.delegate.screen.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class Controller {

	private static class Listener implements ApplicationListener, Disposable {
		private final Controller controller;

		private Listener(final Controller controller) {
			this.controller = controller;
		}

		@Override
		public void create() {
			controller.create();
		}

		@Override
		public void dispose() {
			controller.dispose();
		}

		@Override
		public void pause() {
			controller.suspend();
		}

		@Override
		public void render() {
			controller.render();
		}

		@Override
		public void resize(final int width, final int height) {
			controller.resize(width, height);
		}

		@Override
		public void resume() {
			controller.resume();
		}
	}

	public static Controller instance;
	private final Listener applicationListener = new Listener(this);
	private final DelegateContainer delegates = new DelegateContainer();
	private IntervalTimer intervalTimer;
	private boolean paused;

	protected Controller() {
		if (instance != null) throw new GdxRuntimeException("Single ApplicationController required");
		instance = this;
	}

	public ApplicationDelegate add(final ApplicationDelegate delegate) {
		return delegates.add(delegate);
	}

	public void create() {
		Gdx.graphics.setTitle(getTitle());
		intervalTimer = new IntervalTimer();
		delegates.add(intervalTimer);
		delegates.add(new LocaleService());
		delegates.add(new ResourceService());
		delegates.add(new SpriteBatchDelegate());
		final Array<ApplicationDelegate> initialDelegates = createInitialDelegates();
		if (initialDelegates != null) {
			for (final ApplicationDelegate delegate : initialDelegates) {
				delegates.add(delegate);
			}
		}
		final Screen<?> screen = createInitialScreen();
		if (screen != null) screen.add();
	}

	public void dispose() {
		delegates.dispose();
	}

	public <T extends ApplicationDelegate> T get(final Class<T> type) {
		return delegates.get(type);
	}

	public Listener getApplicationListener() {
		return applicationListener;
	}

	public <T extends ApplicationDelegate> T getDelegate(final Class<T> type) {
		return delegates.get(type);
	}

	public void pause() {
		paused = true;
		delegates.pause();
	}

	public void remove(final ApplicationDelegate delegate) {
		final ApplicationDelegate result = delegates.remove(delegate);
		if (result != null) {
			result.dispose();
		}
	}

	public void render() {
		final Interval interval = intervalTimer.getInterval();
		Gdx.gl.glClearColor(0.f, 0.f, 0.f, 0.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		delegates.preUpdate();
		delegates.update(interval, paused);
		delegates.render(paused);
	}

	public void replace(final ApplicationDelegate delegate, final ApplicationDelegate with) {
		if (with instanceof Screen) {
			if (!with.isReady()) { throw new GdxRuntimeException("Use the replacement constructor of Screen to replace a screen"); }
		}

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				final ApplicationDelegate result = delegates.replace(delegate, with);
				if (result != null) {
					delegate.dispose();
				}
			}
		});
	}

	public void resize(final int width, final int height) {
		delegates.resize(width, height);
	}

	public void resume() {
		delegates.resume();
	}

	public void suspend() {
		delegates.suspend();
	}

	public void unpause() {
		paused = false;
		delegates.unpause();
	}

	protected abstract Array<ApplicationDelegate> createInitialDelegates();

	protected abstract Screen<?> createInitialScreen();

	protected abstract String getTitle();

}
