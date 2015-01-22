package ca.informi.service.engine;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;

public interface EntityFamilyListener extends EntityListener {

	Family getInterestedFamily();

}
