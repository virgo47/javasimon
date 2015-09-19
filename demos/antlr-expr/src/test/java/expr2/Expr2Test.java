package expr2;

import static org.testng.Assert.assertEquals;

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
		assertEquals(expr("var"), 6);
	}

	@Test
	public void plusWithVariable() {
		variableResolver = var -> 6;
		assertEquals(expr("anyVarNameReally+4"), 10);
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor(variableResolver)
			.visit(parseTree);
	}
}
