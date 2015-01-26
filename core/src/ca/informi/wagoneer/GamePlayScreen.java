package ca.informi.wagoneer;

import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.Renderable;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GamePlayScreen implements Screen {

	private final OrthographicCamera camera = new OrthographicCamera();
	private final Wagoneer game;
	private final RenderOptions renderOptions = new RenderOptions() {
		{
			batch = new SpriteBatch();
		}
	};
	private final Array<Renderable> rq = new Array<Renderable>();
	private float scale = 0.5f;
	private final FitViewport viewport = new FitViewport(16.f / scale, 9.f / scale, camera);

	public GamePlayScreen() {
		this.game = Wagoneer.instance;
	}

	@Override
	public void dispose() {
		rq.clear();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(final float delta) {
		camera.position.set(game.player.object.getPosition(), 0.f);
		game.updateRQ(camera, rq);
		game.renderRQ(camera, rq, renderOptions);
	}

	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

}
