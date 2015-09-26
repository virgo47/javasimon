package expr2;

import static expr2.grammar.ExprParser.BooleanLiteralContext;
import static expr2.grammar.ExprParser.NullLiteralContext;
import static expr2.grammar.ExprParser.NumericLiteralContext;
import static expr2.grammar.ExprParser.OP_ADD;
import static expr2.grammar.ExprParser.OP_AND;
import static expr2.grammar.ExprParser.OP_DIV;
import static expr2.grammar.ExprParser.OP_EQ;
import static expr2.grammar.ExprParser.OP_GE;
import static expr2.grammar.ExprParser.OP_GT;
import static expr2.grammar.ExprParser.OP_LE;
import static expr2.grammar.ExprParser.OP_LT;
import static expr2.grammar.ExprParser.OP_MOD;
import static expr2.grammar.ExprParser.OP_MUL;
import static expr2.grammar.ExprParser.OP_NE;
import static expr2.grammar.ExprParser.OP_OR;
import static expr2.grammar.ExprParser.OP_SUB;
import static expr2.grammar.ExprParser.ParensContext;
import static expr2.grammar.ExprParser.StringLiteralContext;
import static expr2.grammar.ExprParser.UnarySignContext;

import java.math.BigDecimal;

import expr2.grammar.ExprBaseVisitor;
import expr2.grammar.ExprParser;

public class ExpressionCalculatorVisitor extends ExprBaseVisitor {

	public static final int DEFAULT_MAX_SCALE = 15;
	public static final int DEFAULT_MAX_RESULT_SCALE = 6;

	private int maxScale = DEFAULT_MAX_SCALE;
	private int maxResultScale = DEFAULT_MAX_RESULT_SCALE;
	private int roundingMode = BigDecimal.ROUND_HALF_UP;

	/** Maximum BigDecimal scale used during computations. */
	public ExpressionCalculatorVisitor maxScale(int maxScale) {
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
	public Boolean visitLogicNot(ExprParser.LogicNotContext ctx) {
		return !(Boolean) visit(ctx.expr());
	}

	@Override
	public BigDecimal visitArithmeticOp(ExprParser.ArithmeticOpContext ctx) {
		BigDecimal left = (BigDecimal) visit(ctx.expr(0));
		BigDecimal right = (BigDecimal) visit(ctx.expr(1));
		return bigDecimalArithmetic(ctx, left, right);
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

	@Override
	public Boolean visitComparisonOp(ExprParser.ComparisonOpContext ctx) {
		Comparable left = (Comparable) visit(ctx.expr(0));
		Comparable right = (Comparable) visit(ctx.expr(1));
		int operator = ctx.op.getType();
		if (left == null || right == null) {
			return left == null && right == null && operator == OP_EQ
				|| (left != null || right != null) && operator == OP_NE;
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
	public BigDecimal visitNumericLiteral(NumericLiteralContext ctx) {
		String text = ctx.NUMERIC_LITERAL().getText();
		return stringToNumber(text);
	}

	private BigDecimal stringToNumber(String text) {
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
	public BigDecimal visitUnarySign(UnarySignContext ctx) {
		BigDecimal result = (BigDecimal) visit(ctx.expr());
		boolean unaryMinus = ctx.op.getText().equals("-");
		return unaryMinus
			? result.negate()
			: result;
	}

	@Override
	public Object visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}

	@Override
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
