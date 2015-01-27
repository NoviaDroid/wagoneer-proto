package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class HitchFixtureDef extends FixtureDef {

	public static float HITCH_SIZE = 0.5f;
	public static final Vector2 HITCH_SIZE_VECTOR = new Vector2(HITCH_SIZE, HITCH_SIZE);

	public HitchFixtureDef(final int hitchIndex, final Vector2 size, final Vector2 position) {
		filter.categoryBits = FilterBits.HITCH_BIT;
		filter.maskBits = FilterBits.HITCH_BIT;
		final CircleShape hitchShape = new CircleShape();
		hitchShape.setRadius(HITCH_SIZE);
		hitchShape.setPosition(position);
		isSensor = true;
		shape = hitchShape;
	}

}
