package ca.informi.gdx;

import java.util.ArrayList;
import java.util.List;

import ca.informi.service.IntervalTimer;
import ca.informi.service.IntervalTimer.Interval;
import ca.informi.service.ResourceService;
import ca.informi.service.Services;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public abstract class ApplicationController {

	public static class ApplicationDelegateInfo {
		boolean added;
		public ApplicationDelegate delegate;
		boolean ready;

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

	private final Listener applicationListener = new Listener(this);

	private List<ApplicationDelegateInfo> delegates;
	private IntervalTimer intervalTimer;
	private boolean paused;

	protected ApplicationController() {
	}

	public ApplicationDelegate add(final ApplicationDelegate delegate) {
		delegates.add(new ApplicationDelegateInfo(delegate));
		return delegate;
	}

	public void create() {
		Gdx.graphics.setTitle(getTitle());
		delegates = new ArrayList<ApplicationDelegateInfo>();
		intervalTimer = new IntervalTimer();
		Services.instance.add(intervalTimer);
		Services.instance.add(new ResourceService());
		Services.instance.add(new SpriteBatch());

		addServices(Services.instance);
		for (final Object service : Services.instance) {
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
		Services.instance.dispose();
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
		Gdx.gl.glClearColor(0.f, 0.f, 0.f, 0.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				final ApplicationDelegateInfo withInfo = new ApplicationDelegateInfo(with);
				final int index = findDelegateInfoIndexForDelegate(delegate);
				if (index == -1) {
					Gdx.app.log("ApplicationController", "Delegate to remove " + delegate + " was not present");
					delegates.add(withInfo);
				} else {
					delegates.set(index, withInfo);
				}
				delegate.removed();
				with.setController(delegate.controller);

				delegate.dispose();
				with.added();
			}
		});
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

	private int findDelegateInfoIndexForDelegate(final ApplicationDelegate delegate) {
		for (int i = 0; i < delegates.size(); ++i) {
			final ApplicationDelegateInfo info = delegates.get(i);
			if (info.delegate.equals(delegate)) return i;
		}
		return -1;
	}

	protected abstract void addInitialDelegates();

	protected abstract void addServices(Services services);

	protected abstract String getTitle();

}
