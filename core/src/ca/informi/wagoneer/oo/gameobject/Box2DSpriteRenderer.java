package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.controller.Controller;
import ca.informi.wagoneer.oo.Game;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Box2DSpriteRenderer implements Renderable {

	private int layer;
	private final Box2DObject parent;
	private final Sprite sprite;

	public Box2DSpriteRenderer(final Box2DObject parent, final String textureName, final int layer) {
		this.parent = parent;
		final Game game = Controller.instance.get(Game.class);
		sprite = game.resources.oryxAtlas.o.createSprite("hauler_white");
		this.layer = layer;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public boolean isAlwaysVisible() {
		return parent.isAlwaysVisible();
	}

	@Override
	public void render(final RenderOptions opts) {
		final Vector2 position = parent.getPosition();
		final float angle = parent.getAngle();
		sprite.setPosition(position.x, position.y);
		sprite.setRotation(angle * MathUtils.radiansToDegrees);
	}

	public void setLayer(final int layer) {
		this.layer = layer;
	}

}
