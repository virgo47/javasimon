/*
 * Copyright 2008  Reg Whitton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.dev.eval;

import java.math.BigDecimal;
import java.util.Map;

final class Operation
{
	final Type type;
	final Operator operator;
	final Object operand1;
	final Object operand2;
	final Object operand3;

	private Operation(Type type, Operator operator, Object operand1,
			Object operand2, Object operand3)
	{
		this.type = type;
		this.operator = operator;
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operand3 = operand3;
	}

	static Operation nopOperationfactory(Object operand)
	{
		return new Operation(Operator.NOP.resultType, Operator.NOP, operand,
				null, null);
	}

	static Object unaryOperationfactory(Operator operator, Object operand)
	{
		validateOperandType(operand, operator.operandType);

		/*
		 * If values can already be resolved then return result instead of
		 * operation
		 */
		if (operand instanceof BigDecimal)
		{
			return operator.perform((BigDecimal) operand, null, null);
		}
		return new Operation(operator.resultType, operator, operand, null, null);
	}

	static Object binaryOperationfactory(Operator operator, Object operand1,
			Object operand2)
	{
		validateOperandType(operand1, operator.operandType);
		validateOperandType(operand2, operator.operandType);

		/*
		 * If values can already be resolved then return result instead of
		 * operation
		 */
		if (operand1 instanceof BigDecimal && operand2 instanceof BigDecimal)
		{
			return operator.perform((BigDecimal) operand1,
					(BigDecimal) operand2, null);
		}
		return new Operation(operator.resultType, operator, operand1, operand2,
				null);
	}

	static Object tenaryOperationFactory(Operator operator, Object operand1,
			Object operand2, Object operand3)
	{
		validateOperandType(operand1, Type.BOOLEAN);
		validateOperandType(operand2, Type.ARITHMETIC);
		validateOperandType(operand3, Type.ARITHMETIC);

		/*
		 * If values can already be resolved then return result instead of
		 * operation
		 */
		if (operand1 instanceof BigDecimal)
		{
			return ((BigDecimal) operand1).signum() != 0 ? operand2 : operand3;
		}
		return new Operation(Type.ARITHMETIC, operator, operand1, operand2,
				operand3);
	}

	BigDecimal eval(Map<String, BigDecimal> variables)
	{
		switch (this.operator.numberOfOperands)
		{
			case 3:
				return this.operator.perform(evaluateOperand(this.operand1,
						variables), evaluateOperand(this.operand2, variables),
						evaluateOperand(this.operand3, variables));
			case 2:
				return this.operator.perform(evaluateOperand(this.operand1,
						variables), evaluateOperand(this.operand2, variables),
						null);
			default:
				return this.operator.perform(evaluateOperand(this.operand1,
						variables), null, null);
		}
	}

	private BigDecimal evaluateOperand(Object operand,
			Map<String, BigDecimal> variables)
	{
		if (operand instanceof Operation)
		{
			return ((Operation) operand).eval(variables);
		}
		else if (operand instanceof String)
		{
			BigDecimal value;
			if (variables == null || (value = variables.get(operand)) == null)
			{
				throw new RuntimeException("no value for variable \"" + operand
						+ "\"");
			}
			return value;
		}
		else
		{
			return (BigDecimal) operand;
		}
	}

	/**
	 * Validate that where operations are combined together that the types are
	 * as expected.
	 */
	private static void validateOperandType(Object operand, Type type)
	{
		Type operandType;

		if (operand instanceof Operation
				&& (operandType = ((Operation) operand).type) != type)
		{
			throw new RuntimeException("cannot use " + operandType.name
					+ " operands with " + type.name + " operators");
		}
	}

	@Override
	public String toString()
	{
		switch (this.operator.numberOfOperands)
		{
			case 3:
				return "(" + this.operand1 + this.operator.string
						+ this.operand2 + ":" + this.operand3 + ")";
			case 2:
				return "(" + this.operand1 + this.operator.string
						+ this.operand2 + ")";
			default:
				return "(" + this.operator.string + this.operand1 + ")";
		}
	}
}
