//package ca.informi.wagoneer.es.archetype;
//
//import com.artemis.World;
//import com.badlogic.gdx.utils.ObjectMap;
//
//public class Archetypes {
//
//	static <T extends ParameterizedArchetypeBuilder<T>> void register(final Class<T> type,
//			final ParameterizedArchetypeBuilder<T> instance) {
//		builders.put(type, instance);
//	}
//
//	private static ObjectMap<Class<? extends ParameterizedArchetypeBuilder<?>>, ParameterizedArchetypeBuilder<?>> builders = new ObjectMap<Class<? extends ParameterizedArchetypeBuilder<?>>, ParameterizedArchetypeBuilder<?>>();
//
//	private final World world;
//
//	public Archetypes(final World world) {
//		this.world = world;
//	}
//
//	public void populate() {
//		for (final ParameterizedArchetypeBuilder<?> builder : builders.values()) {
//			if (builder.archetype == null) {
//				builder.archetype = builder.builder.build(world);
//			}
//		}
//	}
//
// }
