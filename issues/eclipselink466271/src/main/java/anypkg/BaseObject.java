package anypkg;

import java.util.Objects;

/** Abstract implementation of base JPA entity object. */
@SuppressWarnings("serial")
public abstract class BaseObject {

	protected abstract Integer getId();

	@Override
	public final int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public final boolean equals(Object obj) {
		return this == obj
			|| obj != null && getClass().isAssignableFrom(obj.getClass())
			&& Objects.equals(getId(), ((BaseObject) obj).getId());
	}
}