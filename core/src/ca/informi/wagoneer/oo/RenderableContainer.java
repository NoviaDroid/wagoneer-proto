package ca.informi.wagoneer.oo;

import ca.informi.wagoneer.oo.gameobject.Renderable;

public interface RenderableContainer {

	void clear();

	void dispose();

	void init();

	void put(int id, Renderable renderable);

	void remove(int id);

}
