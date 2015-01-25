package ca.informi.wagoneer.oo;

import com.badlogic.gdx.utils.Disposable;

public class Handle<T> implements Disposable {

	public T object;

	public Handle(final T obj) {
		this.object = obj;
	}

	@Override
	public void dispose() {
		object = null;
	}

}
