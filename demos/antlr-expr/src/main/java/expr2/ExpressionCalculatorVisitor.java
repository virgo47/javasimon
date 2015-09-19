package expr2;

import expr2.grammar.ExprBaseVisitor;
import expr2.grammar.ExprParser;

public class ExpressionCalculatorVisitor extends ExprBaseVisitor<Integer> {

	private final ExpressionVariableResolver variableResolver;

	public ExpressionCalculatorVisitor(ExpressionVariableResolver variableResolver) {
		if (variableResolver == null) {
			throw new IllegalArgumentException("Variable resolver must be provided");
		}
		this.variableResolver = variableResolver;
	}

	@Override
	public Integer visitInt(ExprParser.IntContext ctx) {
		return Integer.valueOf(ctx.INT().getText());
	}

	@Override
	public Integer visitVariable(ExprParser.VariableContext ctx) {
		Object value = variableResolver.resolve(ctx.ID().getText());
		return convertToSupportedType(value);
	}

	private Integer convertToSupportedType(Object value) {
		return Integer.valueOf(value.toString());
	}

	@Override
	public Integer visitParens(ExprParser.ParensContext ctx) {
		return visit(ctx.expr());
	}

	@Override
	public Integer visitArithmetic(ExprParser.ArithmeticContext ctx) {
		int left = visit(ctx.expr(0));
		int right = visit(ctx.expr(1));

		switch (ctx.op.getType()) {
			case ExprParser.OP_ADD:
				return left + right;
			case ExprParser.OP_SUB:
				return left - right;
			case ExprParser.OP_MUL:
				return left * right;
			case ExprParser.OP_DIV:
				return left / right;
			default:
				throw new IllegalStateException("Unknown operator " + ctx.op);
		}
	}
}
