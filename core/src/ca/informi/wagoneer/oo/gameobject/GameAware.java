package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.Wagoneer;

public class GameAware {

	public final Wagoneer game;

	public GameAware() {
		game = Wagoneer.instance;
	}

}
