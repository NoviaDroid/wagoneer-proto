package ca.informi.wagoneer.oo.gameobject;

import ca.informi.wagoneer.Wagoneer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Disposable;

public class WagonConnections implements Disposable {

	public static class WagonConnection {

		public Joint joint;
		public final int offsetX;
		public final int offsetY;
		public WagonObject wagon;

		public WagonConnection(final int offsetX, final int offsetY) {
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}
	}

	public static int FORE = 0, AFT = 1, LEFT = 2, RIGHT = 3;
	private static final float GAP_SCALAR = 0.5f;
	private static final Vector2 GAP_VECTOR = new Vector2(GAP_SCALAR, GAP_SCALAR);
	private static Vector2[] offsets = { new Vector2(0, 1), new Vector2(0, -1), new Vector2(-1, 0), new Vector2(1, 0) };
	private static int[] Z_COMPLEMENT = { AFT, FORE, RIGHT, LEFT };

	public static Vector2 getHitchOffset(final int i, final Vector2 size) {
		return new Vector2(size).scl(GAP_VECTOR)
								.scl(offsets[i]);
	}

	public WagonConnection[] connections = new WagonConnection[] { //
	new WagonConnection(0, 1), // fore
			new WagonConnection(0, -1), // aft
			new WagonConnection(-1, 0), // left,
			new WagonConnection(1, 0) // right
	};

	private final WagonObject owner;

	public WagonConnections(final WagonObject owner) {
		this.owner = owner;
	}

	public boolean attach(final int myIndex, final WagonObject other) {
		final int remoteIndex = Z_COMPLEMENT[myIndex];
		if (!(owner.willAcceptConnection(myIndex) && other.willAcceptConnection(remoteIndex))) return false;
		if (connections[myIndex].wagon != null) {
			detach(myIndex);
		}

		if (other.connections.connections[remoteIndex].wagon != null) {
			other.connections.detach(remoteIndex);
		}
		connections[myIndex].wagon = other;
		other.connections.connections[remoteIndex].wagon = owner;

		final Joint joint = (myIndex >= LEFT) ? createFixedJoint(owner, other, myIndex) : createRevoluteJoint(owner, other, myIndex);
		connections[myIndex].joint = joint;
		other.connections.connections[remoteIndex].joint = joint;
		return true;
	}

	public void detach(final int myIndex) {
		final int remoteIndex = Z_COMPLEMENT[myIndex];
		final WagonObject other = connections[myIndex].wagon;
		if (other == null) return;
		connections[myIndex].wagon = null;
		other.connections.connections[remoteIndex].wagon = null;
		// remote should be same object
		Wagoneer.instance.getWorld()
							.destroyJoint(connections[myIndex].joint);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < 4; ++i)
			detach(i);
	}

	public Vector2 getHitchOffset(final int i) {
		return new Vector2(owner.getSize()).scl(GAP_VECTOR)
											.scl(offsets[i]);
	}

	public boolean hasConnection(final int dir) {
		return connections[dir].wagon != null;
	}

	private Vector2 calculateSizedOffset(final WagonObject objA, final WagonObject objB, final int aToBOffsetIndex) {
		return new Vector2(objA.getSize()).add(objB.getSize())
											.add(GAP_VECTOR)
											.scl(offsets[aToBOffsetIndex]);
	}

	private Joint createFixedJoint(final WagonObject objA, final WagonObject objB, final int aToBOffsetIndex) {
		// Math to line up centers looks pretty simple
		final Vector2 offset = calculateSizedOffset(objA, objB, aToBOffsetIndex);
		offset.rotateRad(objA.getAngleRadians());
		objB.setPosition(offset);

		objB.setAngleRadians(objA.getAngleRadians());

		final WeldJointDef def = new WeldJointDef();
		def.bodyA = objA.body;
		def.bodyB = objB.body;
		def.localAnchorA.set(WagonConnections.getHitchOffset(aToBOffsetIndex, objA.getSize()));
		def.localAnchorB.set(WagonConnections.getHitchOffset(Z_COMPLEMENT[aToBOffsetIndex], objB.getSize()));
		def.collideConnected = false;
		final Joint joint = Wagoneer.instance.getWorld()
												.createJoint(def);
		return joint;
	}

	private Joint createRevoluteJoint(final WagonObject objA, final WagonObject objB, final int aToBOffsetIndex) {
		// current offset
		final Vector2 v = new Vector2(objA.getPosition()).sub(objB.getPosition());

		// size(a) + size(b) + gap for correct dimension
		final Vector2 targetOffset = calculateSizedOffset(objA, objB, aToBOffsetIndex);

		// Set current offset length to that of target offset
		v.scl(targetOffset.len() / v.len());

		// Calculate position B based on position A
		v.add(objA.getPosition());

		// Move B into place
		objB.setPosition(v);

		final RevoluteJointDef def = new RevoluteJointDef();
		def.bodyA = objA.body;
		def.bodyB = objB.body;
		def.lowerAngle = 0.5f * MathUtils.PI;
		def.upperAngle = (3.f / 2.f) * MathUtils.PI;
		def.localAnchorA.set(WagonConnections.getHitchOffset(aToBOffsetIndex, objA.getSize()));
		def.localAnchorB.set(WagonConnections.getHitchOffset(Z_COMPLEMENT[aToBOffsetIndex], objB.getSize()));
		def.collideConnected = true; // Prevent from rotating through each other
		final Joint joint = Wagoneer.instance.getWorld()
												.createJoint(def);

		return joint;
	}
}
