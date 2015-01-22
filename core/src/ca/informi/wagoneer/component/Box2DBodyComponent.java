package ca.informi.wagoneer.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.utils.Array;

public class Box2DBodyComponent extends Component {

	public Body body;
	public Array<Joint> ownedJoints;

}