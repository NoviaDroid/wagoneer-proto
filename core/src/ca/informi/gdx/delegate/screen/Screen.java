package ca.informi.gdx.delegate.screen;

import ca.informi.gdx.delegate.ApplicationDelegate;
import ca.informi.gdx.delegate.ResPackage;
import ca.informi.gdx.delegate.controller.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class Screen<T extends ResPackage> extends ApplicationDelegate {

	private boolean beingReplaced;
	private boolean disposed;
	private final T hiddenResources;
	private Runnable onAddedRunnable;
	private boolean ready = false;
	protected T resources;

	protected Screen(final T rp) {
		hiddenResources = rp;
	}

	public Screen<T> add() {
		add(null);
		return this;
	}

	@Override
	public final void added() {
		if (!ready) { throw new GdxRuntimeException("Added when not ready"); }
		final int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		super.added();
		addedInternal();
		resize(width, height);
		if (onAddedRunnable != null) onAddedRunnable.run();
	}

	@Override
	public final void dispose() {
		if (disposed) {
			final GdxRuntimeException t = new GdxRuntimeException("Already disposed");
			t.printStackTrace();
			throw t;
		}
		disposed = true;
		if (ready) {
			resources.dispose();
			resources = null;
		}
		disposeInternal();
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	public boolean isReplacing() {
		return beingReplaced;
	}

	public Screen<T> onAdded(final Runnable runnable) {
		onAddedRunnable = runnable;
		return this;
	}

	public final Screen<T> replace(final Screen<?> replaces) {
		if (replaces.beingReplaced) { throw new GdxRuntimeException("Screen is already being replaced"); }
		replaces.beingReplaced = true;
		add(replaces);
		return this;
	}

	private void add(final Screen<?> replaces) {
		final Screen<T> capture = this;
		if (hiddenResources == null) {
			Controller.instance.add(this);
		} else {
			hiddenResources.load(this)
							.onReady(new Runnable() {
								@Override
								public void run() {
									if (!disposed) {
										capture.ready = true;
										capture.resources = hiddenResources;
										if (replaces == null) {
											Controller.instance.add(capture);
										} else {
											Controller.instance.replace(replaces, capture);
										}
									} else {
										capture.ready = false;
									}
								}
							});
		}
	}

	protected abstract void addedInternal();

	protected abstract void disposeInternal();

}
