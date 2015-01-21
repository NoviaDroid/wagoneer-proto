package ca.informi;

import java.util.ArrayList;
import java.util.List;

import ca.informi.IntervalTimer.Interval;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public abstract class ApplicationController {

	public static class ApplicationDelegateInfo {
		public ApplicationDelegate delegate;
		boolean ready;
		boolean added;

		public ApplicationDelegateInfo(final ApplicationDelegate delegate) {
			super();
			this.delegate = delegate;
		}
	}

	private static class Listener implements ApplicationListener, Disposable {
		private final ApplicationController controller;

		private Listener(final ApplicationController controller) {
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

	public Services services;

	private List<ApplicationDelegateInfo> delegates;
	private IntervalTimer intervalTimer;
	private boolean paused;

	private final Listener applicationListener = new Listener(this);

	protected ApplicationController() {
	}

	public ApplicationDelegate add(final ApplicationDelegate delegate) {
		delegates.add(new ApplicationDelegateInfo(delegate));
		return delegate;
	}

	public void create() {
		Gdx.graphics.setTitle(getTitle());
		delegates = new ArrayList<ApplicationDelegateInfo>();
		services = new Services();
		intervalTimer = new IntervalTimer();
		services.add(intervalTimer);
		services.add(new ResourceService());
		services.add(new SpriteBatch());

		addServices(services);
		for (final Object service : services) {
			if (service instanceof ApplicationDelegate) {
				delegates.add(new ApplicationDelegateInfo((ApplicationDelegate) service));
			}
		}
		addInitialDelegates();

		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.setController(this);
			delegateInfo.delegate.create();
		}
	}

	public void dispose() {
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.dispose();
		}
		delegates.clear();
		services.dispose();
	}

	public Listener getApplicationListener() {
		return applicationListener;
	}

	public void pause() {
		paused = true;
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.pause();
		}
	}

	public ApplicationDelegate remove(final ApplicationDelegate delegate) {
		return delegates.remove(delegate) ? delegate : null;
	}

	public void render() {
		final Interval interval = intervalTimer.getInterval();
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.preUpdate();
		}
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			if (!delegateInfo.ready) {
				delegateInfo.ready = delegateInfo.delegate.isReady();
			}
			if (delegateInfo.ready && (!paused || delegateInfo.delegate.updateWhilePaused())) {
				delegateInfo.delegate.update(interval);
			}
		}
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			if (delegateInfo.ready && (!paused || delegateInfo.delegate.renderWhilePaused())) {
				delegateInfo.delegate.render();
			}
		}
	}

	public ApplicationDelegate replace(final ApplicationDelegate delegate, final ApplicationDelegate with) {
		final int index = delegates.indexOf(delegate);
		if (index == -1) return add(with);
		delegate.removed();
		delegate.dispose();
		delegates.set(index, new ApplicationDelegateInfo(with));
		with.setController(this);
		with.added();
		return with;
	}

	public void resize(final int width, final int height) {
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.resize(width, height);
		}
	}

	public void resume() {
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.resume();
		}
	}

	public void suspend() {
		for (final ApplicationDelegateInfo delegateInfo : delegates) {
			delegateInfo.delegate.suspend();
		}
	}

	protected abstract void addInitialDelegates();

	protected abstract void addServices(Services services);

	protected abstract String getTitle();

}
