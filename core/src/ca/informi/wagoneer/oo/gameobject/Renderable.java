package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.oo.RenderOptions;

public interface Renderable {

	public abstract int getLayer();

	public abstract boolean isAlwaysVisible();

	public abstract void render(RenderOptions opts);

}
