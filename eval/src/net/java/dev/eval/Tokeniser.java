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

final class Tokeniser
{
	static final Character START_NEW_EXPRESSION = new Character('(');

	private final String string;
	private int position;
	private Operator pushedBackOperator = null;

	Tokeniser(String string)
	{
		this.string = string;
		this.position = 0;
	}

	int getPosition()
	{
		return this.position;
	}

	void setPosition(int position)
	{
		this.position = position;
	}

	void pushBack(Operator operator)
	{
		this.pushedBackOperator = operator;
	}

	Operator getOperator(char endOfExpressionChar)
	{
		/* Use any pushed back operator. */
		if (this.pushedBackOperator != null)
		{
			Operator operator = this.pushedBackOperator;
			this.pushedBackOperator = null;
			return operator;
		}

		/* Skip whitespace */
		final int len = this.string.length();
		char ch = 0;
		while (this.position < len
				&& Character.isWhitespace(ch = this.string
						.charAt(this.position)))
		{
			this.position++;
		}
		if (this.position == len)
		{
			if (endOfExpressionChar == 0)
			{
				return Operator.END;
			}
			else
			{
				throw new RuntimeException("missing " + endOfExpressionChar);
			}
		}

		this.position++;
		if (ch == endOfExpressionChar)
		{
			return Operator.END;
		}

		switch (ch)
		{
			case '+':
			{
				return Operator.ADD;
			}
			case '-':
			{
				return Operator.SUB;
			}
			case '/':
			{
				return Operator.DIV;
			}
			case '%':
			{
				return Operator.REMAINDER;
			}
			case '*':
			{
				return Operator.MUL;
			}
			case '?':
			{
				return Operator.TERNARY;
			}
			case '>':
			{
				if (this.position < len
						&& this.string.charAt(this.position) == '=')
				{
					this.position++;
					return Operator.GE;
				}
				return Operator.GT;
			}
			case '<':
			{
				if (this.position < len)
				{
					switch (this.string.charAt(this.position))
					{
						case '=':
							this.position++;
							return Operator.LE;
						case '>':
							this.position++;
							return Operator.NE;
					}
				}
				return Operator.LT;
			}
			case '=':
			{
				if (this.position < len
						&& this.string.charAt(this.position) == '=')
				{
					this.position++;
					return Operator.EQ;
				}
				throw new RuntimeException("use == for equality at position "
						+ this.position);
			}
			case '!':
			{
				if (this.position < len
						&& this.string.charAt(this.position) == '=')
				{
					this.position++;
					return Operator.NE;
				}
				throw new RuntimeException(
						"use != or <> for inequality at position "
								+ this.position);
			}
			case '&':
			{
				if (this.position < len
						&& this.string.charAt(this.position) == '&')
				{
					this.position++;
					return Operator.AND;
				}
				throw new RuntimeException("use && for AND at position "
						+ this.position);
			}
			case '|':
			{
				if (this.position < len
						&& this.string.charAt(this.position) == '|')
				{
					this.position++;
					return Operator.OR;
				}
				throw new RuntimeException("use || for OR at position "
						+ this.position);
			}
			default:
			{
				/* Is this an identifier name for an operator function? */
				if (Character.isUnicodeIdentifierStart(ch))
				{
					int start = this.position - 1;
					while (this.position < len
							&& Character.isUnicodeIdentifierPart(this.string
									.charAt(this.position)))
					{
						this.position++;
					}

					String name = this.string.substring(start, this.position);
					if (name.equals("pow"))
					{
						return Operator.POW;
					}
				}
				throw new RuntimeException("operator expected at position "
						+ this.position + " instead of '" + ch + "'");
			}
		}
	}

	/**
	 * Called when an operand is expected next.
	 * 
	 * @return one of:
	 *         <UL>
	 *         <LI>a {@link BigDecimal} value;</LI>
	 *         <LI>the {@link String} name of a variable;</LI>
	 *         <LI>{@link Tokeniser#START_NEW_EXPRESSION} when an opening
	 *         parenthesis is found: </LI>
	 *         <LI>or {@link Operator} when a unary operator is found in front
	 *         of an operand</LI>
	 *         </UL>
	 * 
	 * @throws RuntimeException
	 *             if the end of the string is reached unexpectedly.
	 */
	Object getOperand()
	{
		/* Skip whitespace */
		final int len = this.string.length();
		char ch = 0;
		while (this.position < len
				&& Character.isWhitespace(ch = this.string
						.charAt(this.position)))
		{
			this.position++;
		}
		if (this.position == len)
		{
			throw new RuntimeException(
					"operand expected but end of string found");
		}

		if (ch == '(')
		{
			this.position++;
			return START_NEW_EXPRESSION;
		}
		else if (ch == '-')
		{
			this.position++;
			return Operator.NEG;
		}
		else if (ch == '+')
		{
			this.position++;
			return Operator.PLUS;
		}
		else if (ch == '.' || Character.isDigit(ch))
		{
			return getBigDecimal();
		}
		else if (Character.isUnicodeIdentifierStart(ch))
		{
			int start = this.position++;
			while (this.position < len
					&& Character.isUnicodeIdentifierPart(this.string
							.charAt(this.position)))
			{
				this.position++;
			}

			String name = this.string.substring(start, this.position);
			/* Is variable name actually a keyword unary operator? */
			if (name.equals("abs"))
			{
				return Operator.ABS;
			}
			else if (name.equals("int"))
			{
				return Operator.INT;
			}
			/* Return variable name */
			return name;
		}
		throw new RuntimeException("operand expected but '" + ch + "' found");
	}

	private BigDecimal getBigDecimal()
	{
		final int len = this.string.length();
		final int start = this.position;
		char ch;

		while (this.position < len
				&& (Character.isDigit(ch = this.string.charAt(this.position)) || ch == '.'))
		{
			this.position++;
		}

		/* Optional exponent part including another sign character. */
		if (this.position < len
				&& ((ch = this.string.charAt(this.position)) == 'E' || ch == 'e'))
		{
			this.position++;
			if (this.position < len
					&& ((ch = this.string.charAt(this.position)) == '+' || ch == '-'))
			{
				this.position++;
			}
			while (this.position < len
					&& Character
							.isDigit(ch = this.string.charAt(this.position))

			)
			{
				this.position++;
			}
		}
		return new BigDecimal(this.string.substring(start, this.position));
	}

	@Override
	public String toString()
	{
		return this.string.substring(0, this.position) + ">>>"
				+ this.string.substring(this.position);
	}
}
