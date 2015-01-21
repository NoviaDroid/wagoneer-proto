package ca.informi.wagoneer;

import ca.informi.ApplicationController;
import ca.informi.Services;
import ca.informi.wagoneer.screen.TitleScreen;

public class Wagoneer extends ApplicationController {

	@Override
	protected void addInitialDelegates() {
		add(new TitleScreen());
	}

	@Override
	protected void addServices(final Services services) {

	}

	@Override
	protected String getTitle() {
		return "Wagoneer";
	}

}
