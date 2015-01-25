package ca.informi.wagoneer.client;

import ca.informi.wagoneer.Wagoneer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class HtmlLauncher extends GwtApplication {

	@Override
	public ApplicationListener getApplicationListener() {
		return new Wagoneer();
	}

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(1280, 720);
	}
}