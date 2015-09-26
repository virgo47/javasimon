package expr3;

import static expr3.grammar.ExprParser.BooleanLiteralContext;
import static expr3.grammar.ExprParser.NullLiteralContext;
import static expr3.grammar.ExprParser.NumericLiteralContext;
import static expr3.grammar.ExprParser.OP_ADD;
import static expr3.grammar.ExprParser.OP_AND;
import static expr3.grammar.ExprParser.OP_DIV;
import static expr3.grammar.ExprParser.OP_EQ;
import static expr3.grammar.ExprParser.OP_GE;
import static expr3.grammar.ExprParser.OP_GT;
import static expr3.grammar.ExprParser.OP_LE;
import static expr3.grammar.ExprParser.OP_LT;
import static expr3.grammar.ExprParser.OP_MOD;
import static expr3.grammar.ExprParser.OP_MUL;
import static expr3.grammar.ExprParser.OP_NE;
import static expr3.grammar.ExprParser.OP_OR;
import static expr3.grammar.ExprParser.OP_SUB;
import static expr3.grammar.ExprParser.ParensContext;
import static expr3.grammar.ExprParser.StringLiteralContext;
import static expr3.grammar.ExprParser.UnarySignContext;
import static expr3.grammar.ExprParser.VariableContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import expr3.grammar.ExprBaseVisitor;
import expr3.grammar.ExprParser;

public class ExpressionCalculatorVisitor extends ExprBaseVisitor {

	public static final int DEFAULT_MAX_SCALE = 15;
	public static final int DEFAULT_MAX_RESULT_SCALE = 6;

	private final ExpressionVariableResolver variableResolver;

	private int maxScale = DEFAULT_MAX_SCALE;
	private int maxResultScale = DEFAULT_MAX_RESULT_SCALE;
	private int roundingMode = BigDecimal.ROUND_HALF_UP;

	public ExpressionCalculatorVisitor(ExpressionVariableResolver variableResolver) {
		if (variableResolver == null) {
			throw new IllegalArgumentException("Variable resolver must be provided");
		}
		this.variableResolver = variableResolver;
	}

	/** Maximum BigDecimal scale used during computations. */
	public expr3.ExpressionCalculatorVisitor maxScale(int maxScale) {
		this.maxScale = maxScale;
		return this;
	}

	/** Maximum BigDecimal scale for result. */
	public ExpressionCalculatorVisitor maxResultScale(int maxResultScale) {
		this.maxResultScale = maxResultScale;
		return this;
	}

	@Override
	public Boolean visitLogicOp(ExprParser.LogicOpContext ctx) {
		boolean left = (boolean) visit(ctx.expr(0));

		switch (ctx.op.getType()) {
			case OP_AND:
				return left && booleanRightSide(ctx);
			case OP_OR:
				return left || booleanRightSide(ctx);
			default:
				throw new IllegalStateException("Unknown operator " + ctx.op);
		}
	}

	private boolean booleanRightSide(ExprParser.LogicOpContext ctx) {
		return (boolean) visit(ctx.expr(1));
	}

	@Override
	public Number visitArithmeticOp(ExprParser.ArithmeticOpContext ctx) {
		Number left = (Number) visit(ctx.expr(0));
		Number right = (Number) visit(ctx.expr(1));
		if (left instanceof BigDecimal && right instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, (BigDecimal) left, (BigDecimal) right);
		} else if (left instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, (BigDecimal) left, new BigDecimal(right.toString()));
		} else if (right instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, new BigDecimal(left.toString()), (BigDecimal) right);
		}
		return integerArithmetic(ctx, left.intValue(), right.intValue());
	}

	private BigDecimal bigDecimalArithmetic(ExprParser.ArithmeticOpContext ctx, BigDecimal left, BigDecimal right) {
		switch (ctx.op.getType()) {
			case OP_ADD:
				return left.add(right);
			case OP_SUB:
				return left.subtract(right);
			case OP_MUL:
				return left.multiply(right);
			case OP_DIV:
				return left.divide(right, maxScale, roundingMode).stripTrailingZeros();
			case OP_MOD:
				return left.remainder(right);
			default:
				throw new IllegalStateException("Unknown operator " + ctx.op);
		}
	}

	private Number integerArithmetic(ExprParser.ArithmeticOpContext ctx, int left, int right) {
		switch (ctx.op.getType()) {
			case OP_ADD:
				return left + right;
			case OP_SUB:
				return left - right;
			case OP_MUL:
				return left * right;
			case OP_DIV:
				return left / right;
			case OP_MOD:
				return left % right;
			default:
				throw new IllegalStateException("Unknown operator " + ctx.op);
		}
	}

	@Override
	public Boolean visitComparisonOp(ExprParser.ComparisonOpContext ctx) {
		Comparable left = (Comparable) visit(ctx.expr(0));
		Comparable right = (Comparable) visit(ctx.expr(1));
		int operator = ctx.op.getType();
		if (left == null || right == null) {
			// TODO alebo chceme vyhodit vynimku ked operator nie je EQ/NE?
			return left == null && right == null && operator == OP_EQ
				|| (left != null || right != null) && operator == OP_NE;
		}
		// if one side is integer and the other BigDecimal, we want to unify it to BigDecimal
		if (left instanceof BigDecimal && right instanceof Integer) {
			right = new BigDecimal(right.toString());
		}
		if (right instanceof BigDecimal && left instanceof Integer) {
			left = new BigDecimal(left.toString());
		}

		//noinspection unchecked
		int comp = left.compareTo(right);
		switch (operator) {
			case OP_EQ:
				return comp == 0;
			case OP_NE:
				return comp != 0;
			case OP_GT:
				return comp > 0;
			case OP_LT:
				return comp < 0;
			case OP_GE:
				return comp >= 0;
			case OP_LE:
				return comp <= 0;
			default:
				throw new IllegalStateException("Unknown operator " + ctx.op);
		}
	}

	@Override
	public Object visitVariable(VariableContext ctx) {
		Object value = variableResolver.resolve(ctx.ID().getText());
		return convertToSupportedType(value);
	}

	@Override
	public String visitStringLiteral(StringLiteralContext ctx) {
		String text = ctx.STRING_LITERAL().getText();
		text = text.substring(1, text.length() - 1)
			.replaceAll("''", "'");
		return text;
	}

	@Override
	public Boolean visitBooleanLiteral(BooleanLiteralContext ctx) {
		return ctx.BOOLEAN_LITERAL().getText().toLowerCase().charAt(0) == 't';
	}

	@Override
	public Number visitNumericLiteral(NumericLiteralContext ctx) {
		String text = ctx.NUMERIC_LITERAL().getText();
		return stringToNumber(text);
	}

	private Number stringToNumber(String text) {
		try {
			if (text.indexOf('.') == -1) {
				return new Integer(text);
			}
		} catch (NumberFormatException e) {
			// ignored, we will just try BigDecimal
		}
		BigDecimal bigDecimal = new BigDecimal(text);

		return bigDecimal.scale() < 0
			? bigDecimal.setScale(0, roundingMode)
			: bigDecimal;
	}

	@Override
	public Object visitNullLiteral(NullLiteralContext ctx) {
		return null;
	}

	@Override
	public Number visitUnarySign(UnarySignContext ctx) {
		Number result = (Number) visit(ctx.expr());
		boolean unaryMinus = ctx.op.getText().equals("-");
		return unaryMinus
			? (result instanceof BigDecimal ? ((BigDecimal) result).negate() : -result.intValue())
			: result;
	}

	@Override
	public Object visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}

	private Object convertToSupportedType(Object value) {
		// directly supported types and null
		if (value == null
			|| value instanceof Integer || value instanceof BigDecimal
			|| value instanceof String || value instanceof Boolean)
		{
			return value;
		}

		if (value instanceof Number) {
			return stringToNumber(value.toString());
		}

		if (value instanceof LocalDate) {
			return DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) value);
		}
		if (value instanceof LocalDateTime) {
			return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) value);
		}
		if (value instanceof Instant) {
			return DateTimeFormatter.ISO_INSTANT.format((Instant) value);
		}
		return value;
	}

	@Override
	public Boolean visitLogicNot(ExprParser.LogicNotContext ctx) {
		return !(Boolean) visit(ctx.expr());
	}

	public Object visitResult(ExprParser.ResultContext ctx) {
		Object result = visit(ctx.expr());
		if (result instanceof BigDecimal) {
			BigDecimal bdResult = (BigDecimal) result;
			if (bdResult.scale() > maxResultScale) {
				result = bdResult.setScale(maxResultScale, roundingMode);
			}
		}
		return result;
	}
}
