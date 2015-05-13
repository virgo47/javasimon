package anypkg;

import javax.validation.constraints.NotNull;

import com.google.common.base.Strings;

/** This class uses guava (compile) and some EE annotation (provided). */
public class AnyClass {

	public void anyMethod(@NotNull String argument) {
		Strings.isNullOrEmpty(argument);
	}
}
