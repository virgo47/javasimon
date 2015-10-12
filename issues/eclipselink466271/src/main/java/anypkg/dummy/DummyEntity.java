package anypkg.dummy;

import javax.persistence.Entity;
import javax.persistence.Id;

import anypkg.BaseObject;

/** Dummy entities to "provoke" EclipseLink to static weave our base classes. */
@Entity
public class DummyEntity extends BaseObject {

	@Id
	private Integer id;

	@Override
	public Integer getId() {
		return null;
	}
}
