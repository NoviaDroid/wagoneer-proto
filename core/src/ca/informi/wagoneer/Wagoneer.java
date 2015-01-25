package ca.informi.wagoneer;

import java.util.HashMap;
import java.util.Map;

import ca.informi.gdx.delegate.ApplicationDelegate;
import ca.informi.gdx.delegate.controller.Controller;
import ca.informi.gdx.delegate.screen.Screen;
import ca.informi.gdx.delegate.screen.TitleScreen;
import ca.informi.wagoneer.oo.Game;

import com.badlogic.gdx.utils.Array;

public class Wagoneer extends Controller {

	private interface EntryPoint {
		Array<ApplicationDelegate> createInitialDelegates();

		Screen<?> createInitialScreen();
	}

	private static final String DEFAULT = "default";

	private static Map<String, EntryPoint> entryPoints = new HashMap<String, EntryPoint>() {
		{
			put("game", new EntryPoint() {
				@Override
				public Array<ApplicationDelegate> createInitialDelegates() {
					return new Array<ApplicationDelegate>(new ApplicationDelegate[] { new Game() });
				}

				@Override
				public Screen<?> createInitialScreen() {
					return null;
				}
			});
			put(DEFAULT, new EntryPoint() {
				@Override
				public com.badlogic.gdx.utils.Array<ApplicationDelegate> createInitialDelegates() {
					return null;
				}

				@Override
				public Screen<?> createInitialScreen() {
					return new TitleScreen();
				}
			});
		}
	};

	EntryPoint ep;

	public Wagoneer() {
		final String entryPointKey = System.getProperty("entryPoint");
		ep = entryPoints.get(entryPointKey);
		if (ep == null) {
			ep = entryPoints.get(DEFAULT);
		}
	}

	@Override
	protected Array<ApplicationDelegate> createInitialDelegates() {
		return ep.createInitialDelegates();
	}

	@Override
	protected Screen<?> createInitialScreen() {
		return ep.createInitialScreen();
	}

	@Override
	protected String getTitle() {
		return "Wagoneer";
	}
}
