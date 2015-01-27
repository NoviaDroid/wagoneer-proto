package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class HitchFixtureDef extends FixtureDef {

	public HitchFixtureDef(final int hitchIndex, final Vector2 size) {
		filter.categoryBits = FilterBits.HITCH_CATEGORY_BIT[hitchIndex];
		filter.maskBits = FilterBits.HITCH_MASK_BIT[hitchIndex];
		final CircleShape hitchShape = new CircleShape();
		hitchShape.setRadius(0.5f);
		hitchShape.setPosition(WagonConnections.getHitchOffset(hitchIndex, size));
		isSensor = true;
		shape = hitchShape;
	}

}
