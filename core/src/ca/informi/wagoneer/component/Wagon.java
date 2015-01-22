package ca.informi.wagoneer.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;

public class Wagon extends PoolableComponent {

	public static enum LinkageType {
		NONE(null),
		LINEAR(PrismaticJoint.class),
		PIVOTING(WheelJoint.class);

		public final Class<? extends Joint> jointClass;

		LinkageType(final Class<? extends Joint> jointClass) {
			this.jointClass = jointClass;
		}
	}

	public Entity linkedAft;
	public LinkageType linkageTypeAft;

	@Override
	public void reset() {
		linkedAft = null;
		linkageTypeAft = LinkageType.NONE;
	}
}
