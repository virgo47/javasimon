package expr2;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.Test;

public class Expr2Test {

	@Test
	public void nullLiteralResultsInNull() {
		assertEquals(expr("nuLL"), null);
	}

	@Test
	public void booleanLiterals() {
		assertEquals(expr("t"), true);
		assertEquals(expr("True"), true);
		assertEquals(expr("f"), false);
		assertEquals(expr("faLSE"), false);
	}

	@Test
	public void stringLiteral() {
		assertEquals(expr("''"), "");
		assertEquals(expr("''''"), "'");
		assertEquals(expr("'something'"), "something");
	}

	@Test
	public void numberLiterals() {
		assertEquals(expr("5"), new BigDecimal("5"));
		assertEquals(expr("10.35"), new BigDecimal("10.35"));
		assertEquals(expr(".35"), new BigDecimal("0.35"));
		assertEquals(expr("1."), BigDecimal.ONE);
	}

	@Test
	public void arithmeticTest() {
		assertEquals(expr("5+5.1"), new BigDecimal("10.1"));
		assertEquals(expr("5-5.1"), new BigDecimal("-0.1"));
		assertEquals(expr("0.3*0.1"), new BigDecimal("0.03"));
		assertEquals(expr("0.33/0.1"), new BigDecimal("3.3"));
		assertEquals(expr("1/3"), new BigDecimal("0.333333"));
		assertEquals(expr("10%3"), new BigDecimal("1"));
	}

	@Test
	public void unarySignTest() {
		assertEquals(expr("-5"), new BigDecimal("-5"));
		assertEquals(expr("-+-5"), new BigDecimal("5"));
		assertEquals(expr("-(3+5)"), new BigDecimal("-8"));
	}

	@Test
	public void logicalOperatorTest() {
		assertEquals(expr("F && F"), false);
		assertEquals(expr("F && T"), false);
		assertEquals(expr("T and F"), false);
		assertEquals(expr("T AND T"), true);
		assertEquals(expr("F || F"), false);
		assertEquals(expr("F || T"), true);
		assertEquals(expr("T or F"), true);
		assertEquals(expr("T OR T"), true);
		assertEquals(expr("!T"), false);
		assertEquals(expr("not T"), false);
		assertEquals(expr("!f"), true);
	}

	@Test
	public void relationalOperatorTest() {
		assertEquals(expr("1 > 0.5"), true);
		assertEquals(expr("1 > 1"), false);
		assertEquals(expr("1 >= 0.5"), true);
		assertEquals(expr("1 >= 1"), true);
		assertEquals(expr("5 == 5"), true);
		assertEquals(expr("5 != 5"), false);
		assertEquals(expr("'a' > 'b'"), false);
		assertEquals(expr("'a' >= 'b'"), false);
		assertEquals(expr("'a' < 'b'"), true);
		assertEquals(expr("'a' <= 'b'"), true);
		assertEquals(expr("true == true"), true);
		assertEquals(expr("true == f"), false);
		assertEquals(expr("true eq t"), true);
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor()
			.visit(parseTree);
	}
}
