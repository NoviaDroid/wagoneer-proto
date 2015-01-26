package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class WagonDelegates implements Disposable {

	private final ObjectMap<WagonMessageType, Array<WagonDelegate>> delegateForType = new ObjectMap<WagonMessageType, Array<WagonDelegate>>();
	private final ObjectMap<WagonDelegate, WagonMessageType> typeForDelegate = new ObjectMap<WagonDelegate, WagonMessageType>();

	public <T extends WagonMessage> void add(final WagonMessageType type, final WagonDelegate delegate) {
		typeForDelegate.put(delegate, type);
		Array<WagonDelegate> array = delegateForType.get(type);
		if (array == null) {
			array = new Array<WagonDelegate>();
			delegateForType.put(type, array);
		}
		array.add(delegate);
	}

	@Override
	public void dispose() {
		delegateForType.clear();
		typeForDelegate.clear();
	}

	public boolean handle(final WagonMessage message) {
		final WagonMessageType type = message.type;
		final Array<WagonDelegate> array = delegateForType.get(type);
		boolean handled = false;
		if (array != null) {
			for (int i = 0; i < array.size; ++i) {
				final WagonDelegate delegate = array.get(i);
				delegate.handle(message);
				handled = true;
			}
		}
		final Array<WagonDelegate> arrayAll = delegateForType.get(WagonMessageType.ALL);
		if (arrayAll != null) {
			for (int i = 0; i < arrayAll.size; ++i) {
				final WagonDelegate delegate = array.get(i);
				delegate.handle(message);
				handled = true;
			}
		}
		return handled;
	}

	public void remove(final WagonDelegate delegate) {
		final WagonMessageType type = typeForDelegate.get(delegate);
		final Array<WagonDelegate> array = delegateForType.get(type);
		array.removeValue(delegate, true);
		if (array.size == 0) {
			delegateForType.remove(type);
		}
	}

}
