package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class WagonMessage implements Poolable {

	public static abstract class WagonMessageData<T> implements ca.informi.Cloneable<T> {
		@Override
		public abstract T clone();
	}

	private static class MessagePool extends Pool<WagonMessage> {
		@Override
		protected WagonMessage newObject() {
			return new WagonMessage();
		}
	}

	public static WagonMessage obtain(final WagonMessage message, final int offsetX, final int offsetY) {
		final WagonMessage child = pool.obtain();
		child.set(message.type, message.data);
		child.originX = message.originX - offsetX;
		child.originY = message.originY - offsetY;
		child.receivedBy.addAll(message.receivedBy);

		return child;
	}

	public static WagonMessage obtain(final WagonMessageType type, final WagonMessageData<?> data) {
		final WagonMessage message = pool.obtain();
		message.set(type, data);
		return message;
	}

	public static void releaseAll(final Iterable<WagonMessage> messages) {
		for (final WagonMessage message : messages) {
			message.release();
		}
	}

	private static MessagePool pool = new MessagePool();

	public WagonMessageData<?> data;
	public int originX, originY;
	public WagonMessageType type = WagonMessageType.__INVALID;
	private WagonObject consumedBy;

	private final IntSet receivedBy = new IntSet();

	private WagonMessage() {
	}

	public final boolean receive(final WagonObject recipient) {
		if (type == WagonMessageType.__INVALID) { throw new GdxRuntimeException("Message was reset"); }
		if (consumedBy != null) { throw new GdxRuntimeException("Message already consumed by " + consumedBy); }
		consumedBy = recipient;
		return receivedBy.add(recipient.id);
	}

	public final void release() {
		pool.free(this);
	}

	@Override
	public void reset() {
		type = WagonMessageType.__INVALID;
		data = null;
		receivedBy.clear();
		originX = originY = 0;
		consumedBy = null;
	}

	@Override
	public String toString() {
		return super.toString() + "[WagonMessage " + type.toString() + ":" + data + " @" + originX + "," + originY + "]";
	}

	private void set(final WagonMessageType type, final WagonMessageData<?> data) {
		this.type = type;
		this.data = data;
	}

}
