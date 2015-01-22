package ca.informi.gdx;

import java.util.Locale;

import ca.informi.service.IntervalTimer;
import ca.informi.service.IntervalTimer.Interval;

import com.badlogic.gdx.ApplicationListener;

public abstract class ApplicationDelegate implements ApplicationListener {

	protected ApplicationController controller;
	protected Locale locale;

	public void added() {
	}

	@Override
	public void create() {
	}

	@Override
	public void dispose() {
	}

	public ApplicationController getController() {
		return controller;
	}

	public boolean isReady() {
		return true;
	}

	@Override
	public void pause() {

	}

	public void preUpdate() {

	}

	public final void remove() {
		controller.remove(this);
		removeInternal();
	}

	public void removed() {
	}

	protected void removeInternal() {
	}

	@Override
	public void render() {
	}

	public boolean renderWhilePaused() {
		return true;
	}

	@Override
	public void resize(final int width, final int height) {
	}

	@Override
	public void resume() {

	}

	public final void setController(final ApplicationController controller) {
		this.controller = controller;
	}

	public void setLocale(final Locale currentLocale) {
		this.locale = currentLocale;
	}

	public void suspend() {

	}

	public void update(final Interval interval) {
	}

	public boolean updateWhilePaused() {
		return false;
	}

}
