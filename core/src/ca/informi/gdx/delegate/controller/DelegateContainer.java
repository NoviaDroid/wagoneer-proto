package ca.informi.gdx.delegate.controller;

import ca.informi.gdx.delegate.ApplicationDelegate;
import ca.informi.gdx.delegate.IntervalTimer.Interval;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

class DelegateContainer implements Disposable {
	private final Array<ApplicationDelegateInfo> delegates = new Array<ApplicationDelegateInfo>();
	private final ObjectMap<Class<? extends ApplicationDelegate>, ApplicationDelegate> typeMap = new ObjectMap<Class<? extends ApplicationDelegate>, ApplicationDelegate>();

	public ApplicationDelegate add(final ApplicationDelegate delegate) {
		delegates.add(new ApplicationDelegateInfo(delegate));
		onAdded(delegate);
		return delegate;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			delegateInfo.delegate.removed();
			delegateInfo.delegate.dispose();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends ApplicationDelegate> T get(final Class<T> type) {
		return (T) typeMap.get(type);
	}

	public void pause() {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			delegateInfo.delegate.pause();
		}
	}

	public void preUpdate() {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			delegateInfo.delegate.preUpdate();
		}
	}

	public ApplicationDelegate remove(final ApplicationDelegate delegate) {
		final int index = delegates.indexOf(new ApplicationDelegateInfo(delegate), false);
		if (index == -1) {
			Gdx.app.debug("DelegateContainer", "Delegate to remove is missing: " + delegate);
			return null;
		}
		return remove(index);
	}

	public void render(final boolean paused) {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			if (delegateInfo.ready && (!paused || delegateInfo.delegate.renderWhilePaused())) {
				delegateInfo.delegate.render();
			}
		}
	}

	public ApplicationDelegate replace(final ApplicationDelegate old, final ApplicationDelegate with) {
		final int index = delegates.indexOf(new ApplicationDelegateInfo(old), false);
		if (index == -1) {
			Gdx.app.debug("DelegateContainer", "Delegate to replace is missing: " + old);
			return add(with);
		}

		final ApplicationDelegateInfo info = delegates.get(index);
		onRemoved(info.delegate);
		info.delegate = with;
		onAdded(info.delegate);
		return with;
	}

	public void resize(final int width, final int height) {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			delegateInfo.delegate.resize(width, height);
		}
	}

	public void resume() {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);

			delegateInfo.delegate.resume();
		}
	}

	public void suspend() {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			delegateInfo.delegate.suspend();
		}
	}

	public void unpause() {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			delegateInfo.delegate.unpause();
		}
	}

	public void update(final Interval interval, final boolean paused) {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo delegateInfo = delegates.get(i);
			if (!delegateInfo.ready) {
				delegateInfo.ready = delegateInfo.delegate.isReady();
			}
			if (delegateInfo.ready && (!paused || delegateInfo.delegate.updateWhilePaused())) {
				delegateInfo.delegate.update(interval);
			}
		}
	}

	// private Iterable<ApplicationDelegateInfo> delegateIterator() {
	// new Throwable().printStackTrace();
	// return delegates;
	// }
	//

	private void onAdded(final ApplicationDelegate delegate) {
		typeMap.put(delegate.getClass(), delegate);
		delegate.added();
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo info = delegates.get(i);
			info.delegate.addedSibling(delegate);
		}
	}

	private void onRemoved(final ApplicationDelegate delegate) {
		for (int i = 0; i < delegates.size; ++i) {
			final ApplicationDelegateInfo info = delegates.get(i);
			info.delegate.removingSibling(delegate);
		}
		delegate.removed();
		typeMap.remove(delegate.getClass());
	}

	private ApplicationDelegate remove(final int index) {
		final ApplicationDelegateInfo info = delegates.get(index);
		delegates.removeIndex(index);
		onRemoved(info.delegate);
		return info.delegate;
	}
}