package ca.informi.gdx.delegate;
//package ca.informi.service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.IdentityHashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.utils.Disposable;
//
//public class Services implements Collection<Object>, Disposable {
//
//	public static final Services instance = new Services();
//	public Map<Class<?>, Object> Objects = new IdentityHashMap<Class<?>, Object>();
//
//	private Services() {
//	}
//
//	@Override
//	public boolean add(final Object e) {
//		final Object old = Objects.put(e.getClass(), e);
//		if (e.equals(old)) {
//			Gdx.app.debug("Services", "Service was already present: " + e);
//			return false;
//		}
//
//		if (old != null) {
//			Gdx.app.debug("Services", "Replacing old instance " + old);
//			endObject(old);
//		}
//
//		if (e instanceof Service) {
//			((Service) e).start();
//		}
//
//		return true;
//	}
//
//	@Override
//	public boolean addAll(final Collection<? extends Object> c) {
//		for (final Object s : c) {
//			add(s);
//		}
//		return true;
//	}
//
//	@Override
//	public void clear() {
//		for (final Object s : Objects.values()) {
//			endObject(s);
//		}
//		Objects.clear();
//	}
//
//	@Override
//	public boolean contains(final Object o) {
//		return Objects.containsKey(o) || Objects.containsValue(o);
//	}
//
//	@Override
//	public boolean containsAll(final Collection<?> c) {
//		return Objects.values()
//						.containsAll(c) || Objects.keySet()
//													.containsAll(c);
//	}
//
//	@Override
//	public void dispose() {
//		clear();
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T extends Object> T get(final Class<T> klass) {
//		return (T) (Objects.get(klass)); // Can't do klass.cast in GWT
//	}
//
//	@Override
//	public boolean isEmpty() {
//		return Objects.isEmpty();
//	}
//
//	@Override
//	public Iterator<Object> iterator() {
//		return Objects.values()
//						.iterator();
//	}
//
//	@Override
//	public boolean remove(final Object o) {
//		Object removed = Objects.remove(o);
//		if (removed == null) {
//			removed = Objects.remove(o.getClass());
//		}
//		if (removed != null) {
//			endObject(removed);
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public boolean removeAll(final Collection<?> c) {
//		boolean result = !c.isEmpty();
//		for (final Object o : c) {
//			result = remove(o) || result;
//		}
//		return result;
//	}
//
//	@Override
//	public boolean retainAll(final Collection<?> c) {
//		final Collection<Object> values = Objects.values();
//		final Collection<Object> toRemove = new ArrayList<Object>();
//		for (final Object s : values) {
//			if (contains(s)) toRemove.add(s);
//		}
//		for (final Object o : toRemove) {
//			remove(o);
//		}
//		return !toRemove.isEmpty();
//	}
//
//	@Override
//	public int size() {
//		return Objects.size();
//	}
//
//	@Override
//	public Object[] toArray() {
//		return Objects.values()
//						.toArray();
//	}
//
//	@Override
//	public <T> T[] toArray(final T[] a) {
//		return Objects.values()
//						.toArray(a);
//	}
//
//	private void endObject(final Object s) {
//		if (s instanceof Service) ((Service) s).stop();
//		if (s instanceof Disposable) ((Disposable) s).dispose();
//	}
// }
