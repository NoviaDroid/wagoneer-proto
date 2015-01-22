package ca.informi.wagoneer;

import ca.informi.gdx.ApplicationController;
import ca.informi.service.Services;
import ca.informi.wagoneer.screen.GameScreen;

public class Wagoneer extends ApplicationController {

	@Override
	protected void addInitialDelegates() {
		add(new GameScreen());
	}

	@Override
	protected void addServices(final Services services) {

	}

	@Override
	protected String getTitle() {
		return "Wagoneer";
	}

}
