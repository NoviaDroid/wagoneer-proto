//package ca.informi.wagoneer.es.archetype;
//
//import com.artemis.Archetype;
//import com.artemis.ArchetypeBuilder;
//
//public class ParameterizedArchetypeBuilder<T extends ParameterizedArchetypeBuilder<T>> {
//
//	public static <K extends ParameterizedArchetypeBuilder<K>> void register(final Class<K> type, final K instance) {
//		Archetypes.register(type, instance);
//	}
//
//	public Archetype archetype;
//	public ArchetypeBuilder builder;
//
//	protected ParameterizedArchetypeBuilder(final Class<T> type, final ArchetypeBuilder builder) {
//		this.builder = builder;
//		register(type, this);
//	}
//
// }
