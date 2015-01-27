package ca.informi.wagoneer.oo.gameobject;

import java.util.Comparator;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class WagonHitch implements Updatable, Renderable, Disposable {
	public static enum WagonHitchType {
		WELD_HITCH,
		REVOLUTE_HITCH;
	}

	private static final class WagonHitchDistanceComparator implements Comparator<WagonHitch> {
		private final WagonObject owner;
		private final Vector2 temp = new Vector2();

		public WagonHitchDistanceComparator(final WagonObject owner) {
			this.owner = owner;
		}

		@Override
		public int compare(final WagonHitch o1, final WagonHitch o2) {
			final float d1 = temp.set(owner.getPosition())
									.sub(o1.owner.getPosition())
									.len2();
			final float d2 = temp.set(owner.getPosition())
									.scl(o2.owner.getPosition())
									.len2();
			return d1 > d2 ? 1 : -1; // not correct, but stable.
		}

	}

	public static final int FORE = 0, AFT = 1, LEFT = 2, RIGHT = 3;

	private static int[][] offsets = {	{	0,
											1 },
										{	0,
											-1 },
										{	-1,
											0 },
										{	1,
											0 } };

	private static float HITCH_OFFSET[] = { 0.5f,
											0.5f,
											0.f,
											0.f };

	public static int[] HITCH_COMPLEMENT = { AFT,
											FORE,
											RIGHT,
											LEFT };

	private static final float ATTACH_DISTANCE_TOLERANCE = 0.08f;
	private static final float ATTACH_DISTANCE_TOLERANCE_SQ = ATTACH_DISTANCE_TOLERANCE * ATTACH_DISTANCE_TOLERANCE;
	private static final float ATTACH_REVOLUTE_ANGLE_TOLERANCE = 90.f * MathUtils.degreesToRadians;
	private static final float ATTACH_WELD_ANGLE_TOLERANCE = 5.f * MathUtils.degreesToRadians;;

	public final WagonObject owner;
	public final WagonHitchType type;
	public final Fixture fixture;
	public final Vector2 offset;
	public final int index;
	public final int offsetX;
	public final int offsetY;

	public WagonHitch connected;
	private Joint joint;

	private final float attractStrength;

	private final WagonHitchDistanceComparator distanceComparator;
	private final Renderable renderer;

	private final Array<WagonHitch> sensorContacts = new Array<WagonHitch>(3);

	public WagonHitch(final WagonObject owner, final int index) {
		this.distanceComparator = new WagonHitchDistanceComparator(owner);
		this.index = index;
		this.owner = owner;
		this.attractStrength = 1.0f;
		this.offsetX = offsets[index][0];
		this.offsetY = offsets[index][1];
		type = (index == FORE || index == AFT) ? WagonHitchType.REVOLUTE_HITCH : WagonHitchType.WELD_HITCH;
		final Vector2 ownerSize = owner.getSize();
		offset = new Vector2(ownerSize).scl(0.5f)
										.add(HITCH_OFFSET[index], HITCH_OFFSET[index])
										.scl(offsetX, offsetY);
		final Vector2 visualOffset = new Vector2(offset).sub(HITCH_OFFSET[index] * offsetX * 0.5f, HITCH_OFFSET[index] * offsetY
				* 0.5f);

		final FixtureDef fdef = new HitchFixtureDef(index, ownerSize, offset);
		fixture = owner.body.createFixture(fdef);
		fdef.shape.dispose();
		fixture.setUserData(this);

		final Sprite sprite = Wagoneer.instance.resources.oryxAtlas.object.createSprite("wagon_hitch", index);
		renderer = new OffsetSpriteRenderer(owner, sprite, HitchFixtureDef.HITCH_SIZE_VECTOR, visualOffset, owner.getLayer() - 1);
	}

	/**
	 * Attach the owner via hitch myIndex to the complementary hitch on the
	 * other object. Breaks existing attachments using the involved hitches
	 * (maintaining existing joints would have physics consequences).
	 *
	 * @param myIndex
	 * @param other
	 * @return
	 */
	public boolean attach(final WagonHitch other) {
		if (!(owner.willAcceptConnection(index) && other.owner.willAcceptConnection(other.index))) { return false; }

		if (connected != null) {
			detach();
		}
		if (other.connected != null) {
			other.detach();
		}

		final Joint joint = type == WagonHitchType.WELD_HITCH ? tryCreateFixedJoint(other) : tryCreateRevoluteJoint(other);
		if (joint == null) { return false; }

		this.connected = other;
		this.joint = joint;

		other.connected = this;
		other.joint = joint;

		return true;
	}

	public void beginContact(final WagonHitch other) {
		if (connected != null) { return; }

		sensorContacts.add(other);
	}

	public boolean detach() {
		if (connected == null) { return false; }

		connected.joint = null;
		connected.connected = null;

		Wagoneer.instance.getWorld()
							.destroyJoint(joint);
		joint = null;
		connected = null;
		return true;
	}

	@Override
	public void dispose() {
		detach();
		if (owner.body != null) {
			owner.body.getFixtureList()
						.removeValue(this.fixture, true);
		}
		renderer.dispose();
	}

	public void endContact(final WagonHitch other) {
		if (connected != null) { return; }

		sensorContacts.removeValue(other, true);
	}

	@Override
	public int getLayer() {
		return renderer.getLayer();
	}

	@Override
	public boolean isAlwaysVisible() {
		return renderer.isAlwaysVisible();
	}

	public boolean isConnected() {
		return connected != null;
	}

	@Override
	public void render(final RenderOptions opts) {
		renderer.render(opts);
	}

	@Override
	public void update(final Interval interval) {
		if (connected == null) {
			sensorContacts.sort(distanceComparator);
			for (int i = 0, n = sensorContacts.size; i < n; ++i) {
				applyMagnet(sensorContacts.get(i));
			}
		}
	}

	private void applyMagnet(final WagonHitch other) {
		magnet(other, attracts(other) ? attractStrength : -attractStrength);
	}

	private boolean attracts(final WagonHitch other) {
		return this.type == other.type && this.index != other.index;
	}

	private void magnet(final WagonHitch other, final float amount) {
		// Do it both ways. This will get called twice per attracting pair, but
		// they can have different
		// attract strengths.
		final Vector2 temp = new Vector2();
		final Vector2 localAttractLocus = owner.body.getWorldPoint(this.offset);
		final Vector2 remoteAttractLocus = other.owner.body.getWorldPoint(other.offset);

		if (amount > 0.f && attach(other)) { return; }

		final Vector2 forceVector = temp.set(remoteAttractLocus)
										.sub(localAttractLocus);

		// Use inverse-cubed plus minimum to avoid force amplification at near
		// distances.
		final float inv = 0.05f + Math.abs(forceVector.x * forceVector.x * forceVector.x)
				+ Math.abs(forceVector.y * forceVector.y * forceVector.y);
		forceVector.scl(amount / inv);
		// Gdx.app.debug(getClass().getSimpleName(), "Attract local between " +
		// localAttractLocus + " and " + remoteAttractLocus
		// + " is force:" + forceVector);
		owner.body.applyForce(forceVector, localAttractLocus, true);

		forceVector.scl(-1.f);
		// Gdx.app.debug(getClass().getSimpleName(), "Attract force between " +
		// remoteAttractLocus + " and " + localAttractLocus
		// + " is force:" + forceVector);
		other.owner.body.applyForce(forceVector, remoteAttractLocus, true);

	}

	/**
	 * Tries to create a weld joint between objA and objB.
	 *
	 * @param objA
	 * @param objB
	 * @param aToBOffsetIndex
	 * @return
	 */
	private Joint tryCreateFixedJoint(final WagonHitch other) {
		if (!tryPrepareForJoint(other, ATTACH_WELD_ANGLE_TOLERANCE, true)) { return null; }

		final WeldJointDef def = new WeldJointDef();
		def.bodyA = this.owner.body;
		def.bodyB = other.owner.body;
		def.localAnchorA.set(this.offset);
		def.localAnchorB.set(other.offset);
		def.collideConnected = false;
		final Joint joint = Wagoneer.instance.getWorld()
												.createJoint(def);
		return joint;
	}

	/**
	 * Tries to create a revolute joint between objA and objB.
	 *
	 * @param objA
	 * @param objB
	 * @param aToBOffsetIndex
	 * @return
	 */
	private Joint tryCreateRevoluteJoint(final WagonHitch other) {
		if (!tryPrepareForJoint(other, ATTACH_REVOLUTE_ANGLE_TOLERANCE, false)) { return null; }
		final RevoluteJointDef def = new RevoluteJointDef();
		def.bodyA = this.owner.body;
		def.bodyB = other.owner.body;
		def.lowerAngle = ATTACH_REVOLUTE_ANGLE_TOLERANCE;
		def.upperAngle = MathUtils.PI2 - ATTACH_REVOLUTE_ANGLE_TOLERANCE;
		def.localAnchorA.set(offset);
		def.localAnchorB.set(other.offset);
		def.collideConnected = true; // Prevent from rotating through each other
		final Joint joint = Wagoneer.instance.getWorld()
												.createJoint(def);

		return joint;
	}

	private boolean tryPrepareForJoint(final WagonHitch other, final float angleTolerance, final boolean alignAngles) {
		final WagonObject objA = this.owner;
		final WagonObject objB = other.owner;
		final float angleA = objA.getAngleRadians();
		final float angleB = objB.getAngleRadians();
		final float angleDifference = AngleUtils.absAngleDifferenceRadians(angleA, angleB);
		if (angleDifference > angleTolerance) { return false; }

		final Vector2 error = new Vector2(other.owner.body.getWorldPoint(other.offset)).sub(owner.body.getWorldPoint(offset));
		if (error.len2() > ATTACH_DISTANCE_TOLERANCE_SQ) { return false; }

		// Adjust object positions by mass
		final float massA = objA.body.getMass(), massB = objB.body.getMass(), massTotal = massA + massB;
		final Vector2 objANewPosition = new Vector2(error).scl(-massB / massTotal)
															.add(objA.getPosition());
		final Vector2 objBNewPosition = new Vector2(error).scl(massA / massTotal)
															.add(objB.getPosition());
		objA.setPosition(objANewPosition);
		objB.setPosition(objBNewPosition);

		if (alignAngles) {
			objB.setAngleRadians(angleA);
		}
		return true;
	}

}
