package j9mm.common.reflex;

public class ReflexFailure extends RuntimeException {

	public ReflexFailure(String message) {
		super(message);
	}

	public ReflexFailure(String message, Throwable cause) {
		super(message, cause);
	}
}
