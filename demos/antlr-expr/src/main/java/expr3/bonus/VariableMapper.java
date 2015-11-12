package expr3.bonus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import expr3.ExpressionException;

public class VariableMapper<T> {

	private Map<String, Function<T, Object>> variableValueFunctions = new HashMap<>();

	public VariableMapper<T> set(String variableName, Function<T, Object> valueFunction) {
		variableValueFunctions.put(variableName, valueFunction);
		return this;
	}

	public Object resolveVariable(String variableName, T object) {
		Function<T, Object> valueFunction = variableValueFunctions.get(variableName);
		if (valueFunction == null) {
			throw new ExpressionException("Unknown variable " + variableName);
		}
		return valueFunction.apply(object);
	}
}
