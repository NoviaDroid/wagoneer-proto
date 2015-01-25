package ca.informi.gdx.delegate;

import java.util.Locale;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.gdx.delegate.controller.Controller;

import com.badlogic.gdx.utils.Disposable;

public abstract class ApplicationDelegate implements Disposable {

	protected final Controller controller = Controller.instance;
	protected Locale locale;

	public void added() {
	}

	public void addedSibling(final ApplicationDelegate delegate) {

	}

	public Controller getController() {
		return controller;
	}

	public boolean isReady() {
		return true;
	}

	public void pause() {

	}

	public void preUpdate() {

	}

	public final void remove() {
		controller.remove(this);
	}

	public void removed() {
	}

	public void removingSibling(final ApplicationDelegate delegate) {

	}

	public void render() {
	}

	public boolean renderWhilePaused() {
		return true;
	}

	public void resize(final int width, final int height) {
	}

	public void resume() {
	}

	public void setLocale(final Locale currentLocale) {
		this.locale = currentLocale;
	}

	public void suspend() {
	}

	public void unpause() {
	}

	public void update(final Interval interval) {
	}

	public boolean updateWhilePaused() {
		return true;
	}

}
