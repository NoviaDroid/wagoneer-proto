package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class WagonMessage implements Poolable {

	public static abstract class WagonMessageData<T> {
		public abstract T clone();
	}

	private static class MessagePool extends Pool<WagonMessage> {
		@Override
		protected WagonMessage newObject() {
			return new WagonMessage();
		}
	}

	private static MessagePool pool = new MessagePool();

	public static WagonMessage acquire(final WagonMessageType type, final WagonMessageData<?> data) {
		final WagonMessage message = pool.obtain();
		message.set(type, data);
		return message;
	}

	public static void releaseAll(final Iterable<WagonMessage> messages) {
		for (final WagonMessage message : messages) {
			message.release();
		}
	}

	public WagonMessageData<?> data;
	public int originX, originY;
	public WagonMessageType type;

	private final IntSet receivedBy = new IntSet();

	private WagonMessage() {
	}

	public final boolean alreadyReceived(final WagonObject recipient) {
		if (!receivedBy.add(recipient.id)) return true;
		return false;
	}

	public WagonMessage clone(final int offsetX, final int offsetY) {
		final WagonMessage message = WagonMessage.acquire(type, data == null ? null : (WagonMessageData<?>) data.clone());
		message.originX -= offsetX;
		message.originY -= offsetY;
		return message;
	}

	public final void release() {
		pool.free(this);
	}

	@Override
	public void reset() {
		type = WagonMessageType.__INVALID;
		data = null;
		receivedBy.clear();
	}

	private void set(final WagonMessageType type, final WagonMessageData<?> data) {
		this.type = type;
		this.data = data;
	}

}
