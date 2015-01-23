package ca.informi.wagoneer;

import java.util.HashMap;
import java.util.Map;

import ca.informi.delegate.screen.Screen;
import ca.informi.gdx.ApplicationDelegate;
import ca.informi.gdx.Controller;
import ca.informi.wagoneer.oo.GameScreen;
import ca.informi.wagoneer.screen.TitleScreen;

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
					return null;
				}

				@Override
				public Screen<?> createInitialScreen() {
					return new GameScreen();
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
