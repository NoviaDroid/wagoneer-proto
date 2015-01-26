package ca.informi.wagoneer;

import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.DebrisEmitter;
import ca.informi.wagoneer.oo.gameobject.Renderable;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GamePlayScreen implements Screen {

	// private final class MyResourcePackage extends ResPackage {
	// final ResHandle<Texture> flotsamTexture = add("images/flotsam.png",
	// Texture.class);
	//
	// public MyResourcePackage() {
	// super("gameplay");
	// }
	// }
	//
	private final OrthographicCamera camera = new OrthographicCamera();
	private final DebrisEmitter debrisEmitter = new DebrisEmitter();
	private final Wagoneer game;
	private final RenderOptions renderOptions = new RenderOptions(new SpriteBatch(), camera);
	// private final MyResourcePackage resources;
	private final Array<Renderable> rq = new Array<Renderable>();
	private final float scale = 0.5f;
	private final FitViewport viewport = new FitViewport(16.f / scale, 9.f / scale, camera);

	public GamePlayScreen() {
		final GamePlayScreen capture = this;
		// resources = new MyResourcePackage();
		// resources.load()
		// .onReady(new Runnable() {
		// @Override
		// public void run() {
		Wagoneer.instance.setScreen(capture);
		// }
		// });
		this.game = Wagoneer.instance;
	}

	@Override
	public void dispose() {
		rq.clear();
		debrisEmitter.dispose();
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
		game.updateRQ(renderOptions, rq);
		game.renderRQ(renderOptions, rq);
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
