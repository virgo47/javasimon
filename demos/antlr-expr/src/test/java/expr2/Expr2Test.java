package expr2;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Expr2Test {

	private ExpressionVariableResolver variableResolver;

	@BeforeMethod
	public void initVariableResolver() {
		variableResolver = var -> null;
	}

	@Test
	public void integerVariable() {
		variableResolver = var -> 6;
		assertEquals(expr("var"), new BigDecimal("6"));
	}

	@Test
	public void plusWithVariable() {
		variableResolver = var -> 6;
		assertEquals(expr("anyVarNameReally+4"), BigDecimal.TEN);
	}

	@Test
	public void nullLiteralResultsInNull() {
		assertEquals(expr("null"), null);
	}

	@Test
	public void nullTestResultsInBoolean() {
		assertEquals(expr("var == null"), true);
	}

	@Test
	public void booleanLiteralsAndNegation() {
		assertEquals(expr("t"), true);
		assertEquals(expr("true"), true);
		assertEquals(expr("not f"), true);
		assertEquals(expr("not false"), true);
	}

	@Test
	public void keywordsAndLiteralsAreCaseInsensitive() {
		assertEquals(expr("TrUe"), true);
		assertEquals(expr("not TruE"), false);
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor(variableResolver)
			.visit(parseTree);
	}
}
