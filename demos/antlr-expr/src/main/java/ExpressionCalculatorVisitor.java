public class ExpressionCalculatorVisitor extends ExprBaseVisitor<Integer> {

	@Override
	public Integer visitInt(ExprParser.IntContext ctx) {
		return Integer.valueOf(ctx.INT().getText());
	}

	@Override
	public Integer visitId(ExprParser.IdContext ctx) {
		return ctx.ID().getText().length();
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
