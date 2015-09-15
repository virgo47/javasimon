package expr1;

import static org.testng.Assert.assertEquals;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.Test;

public class Expr1Test {

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(expr("5"), 5);
	}

	@Test
	public void plusAddsNumbers() {
		assertEquals(expr("5+4"), 9);
	}

	@Test
	public void minusCanProduceNegativeNumber() {
		assertEquals(expr("5-8"), -3);
	}

	@Test
	public void whitespacesAreIgnored() {
		assertEquals(expr("5\n- 8\t + 3"), 0);
	}

	@Test
	public void multiplyPrecedesPlus() {
		assertEquals(expr("2+3*3"), 11);
		assertEquals(expr("2*3+3"), 9);
	}

	@Test
	public void parenthesisChangePrecedence() {
		assertEquals(expr("(2+3)*3"), 15);
		assertEquals(expr("2*(3+3)"), 12);
	}

	@Test(expectedExceptions = ExpressionException.class,
		expectedExceptionsMessageRegExp = "no viable alternative at input '-'")
	public void cannotEnterNegativeNumber() {
		expr("-1");
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor()
			.visit(parseTree);
	}
}
