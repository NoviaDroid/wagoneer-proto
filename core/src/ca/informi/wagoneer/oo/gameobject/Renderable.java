package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.utils.Disposable;

public interface Renderable extends Disposable {

	public abstract void dispose();

	public abstract int getLayer();

	public abstract boolean isAlwaysVisible();

	public abstract void render(RenderOptions opts);

}
