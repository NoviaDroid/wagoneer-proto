package ca.informi.wagoneer;

import ca.informi.gdx.ApplicationDelegate;
import ca.informi.service.IntervalTimer.Interval;
import ca.informi.service.Services;
import ca.informi.service.engine.Box2DDestroySystem;
import ca.informi.service.engine.EngineService;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

public class Game extends ApplicationDelegate {

	private World world;
	private EngineService engine;

	public void start() {
		Box2D.init();
		world = new World(new Vector2(), true);
		engine = new EngineService();

		final Box2DDestroySystem destroySystem = new Box2DDestroySystem();
		engine.addEntityListener(destroySystem.getInterestedFamily(), destroySystem);
		engine.addSystem(destroySystem);

		Services.instance.add(world);
		Services.instance.remove(engine);
	}

	public void stop() {
		Services.instance.remove(world);
		Services.instance.remove(engine);
	}

	@Override
	public void update(final Interval interval) {
		engine.update(interval);
		world.step(interval.dt, 8, 3);
	}

	@Override
	public boolean updateWhilePaused() {
		return false;
	}

}
