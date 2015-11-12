package expr3;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Expr3Test {

	private ExpressionVariableResolver variableResolver;

	@BeforeMethod
	public void init() {
		variableResolver = var -> null;
	}

	@Test
	public void primitiveVariableResolverReturnsTheSameValueForAnyVarName() {
		variableResolver = var -> 5;
		assertEquals(expr("var"), 5);
		assertEquals(expr("anyvarworksnow"), 5);
	}

	@Test
	public void variableResolverReturnsValueForOneVarName() {
		variableResolver = var -> var.equals("var") ? 5 : null;
		assertEquals(expr("var"), 5);
		assertEquals(expr("var != null"), true);
		assertEquals(expr("var == null"), false);

		assertEquals(expr("anyvarworksnow"), null);
		assertEquals(expr("anyvarworksnow == null"), true);
	}

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(expr("5"), 5);
	}

	@Test
	public void tooBigIntegerLiteralConvertedToBigDecimal() {
		assertEquals(expr("5555555555555"), new BigDecimal("5555555555555"));
	}

	@Test
	public void fpNumberCanStartWithPoint() {
		assertEquals(expr(".047"), new BigDecimal("0.047"));
	}

	@Test
	public void fpNumberCanContainExponent() {
		assertEquals(expr("1.47E5"), new BigDecimal("147000"));
	}

	@Test
	public void fpNumberCanContainExplicitlyPositiveExponent() {
		assertEquals(expr(".47E+3"), new BigDecimal("470"));
	}

	@Test
	public void fpNumberCanContainNegativeExponent() {
		assertEquals(expr("1.47E-1"), new BigDecimal("0.147"));
	}

	@Test
	public void fpNumberCanEndWithPoint() {
		assertEquals(expr("3."), new BigDecimal("3"));
	}

	@Test
	public void shortVariableIsConvertedToInteger() {
		variableResolver = var -> (short) 5;
		assertEquals(expr("var").getClass(), Integer.class);
	}

	@Test
	public void smallLongIsConvertedToInteger() {
		variableResolver = var -> 5L;
		assertEquals(expr("var").getClass(), Integer.class);
	}

	@Test
	public void valueOutOfIntegerRangeIsConvertedToBigDecimal() {
		variableResolver = var -> 5555555555555L;
		assertEquals(expr("var").getClass(), BigDecimal.class);
	}

	@Test
	public void fpValueConvertedToBigDecimal() {
		variableResolver = var -> 5.1;
		assertEquals(expr("var"), new BigDecimal("5.1"));
	}

	@Test
	public void localDateIsConvertedToIsoString() {
		variableResolver = var -> LocalDate.of(2015, 8, 26);
		assertEquals(expr("var"), "2015-08-26");
	}

	@Test
	public void localDateTimeIsConvertedToIsoString() {
		variableResolver = var -> LocalDateTime.of(2015, 8, 26, 22, 37);
		assertEquals(expr("var"), "2015-08-26T22:37:00");
	}

	@Test
	public void instantVariableIsConvertedToIsoString() {
		Instant now = Instant.now();
		variableResolver = var -> now;
		assertEquals(expr("var"), now.toString());
	}

	@Test
	public void booleanVariableStaysBoolean() {
		variableResolver = var -> true;
		assertEquals(expr("var"), true);
	}

	@Test
	public void stringVariableStaysString() {
		variableResolver = var -> "str'val";
		assertEquals(expr("var"), "str'val");
	}

	@Test
	public void stringLiteralEscapedQuoteInterpretedProperly() {
		assertEquals(expr("'str''val'"), "str'val");
	}

	@Test
	public void stringCompare() {
		variableResolver = var -> "str'val";
		assertEquals(expr("var == 'str''val'"), true);
	}

	@Test
	public void dateComparisonIsBasedOnIsoStrings() {
		variableResolver = var -> LocalDate.now();
	}

	@Test
	public void booleanLiterals() {
		assertEquals(expr("true"), true);
		assertEquals(expr("false"), false);
		assertEquals(expr("T"), true);
		assertEquals(expr("F"), false);
	}

	@Test
	public void booleanAnd() {
		assertEquals(expr("true && true"), true);
		assertEquals(expr("true && false"), false);
		assertEquals(expr("false && true"), false);
		assertEquals(expr("false && false"), false);
		assertEquals(expr("true AND T"), true);
		assertEquals(expr("true AND F"), false);
		// keyword is case-insensitive
		assertEquals(expr("false and T"), false);
		assertEquals(expr("false and F"), false);
	}

	@Test
	public void booleanOr() {
		assertEquals(expr("true || true"), true);
		assertEquals(expr("true || false"), true);
		assertEquals(expr("false || true"), true);
		assertEquals(expr("false || false"), false);
		assertEquals(expr("true OR true"), true);
		assertEquals(expr("true OR false"), true);
		assertEquals(expr("false OR true"), true);
		assertEquals(expr("false OR false"), false);
	}

	@Test
	public void numberComparison() {
		assertEquals(expr("5 > 1"), true);
		assertEquals(expr("1 > 5"), false);
		assertEquals(expr("5 > 5"), false);
		assertEquals(expr("5 < 1"), false);
		assertEquals(expr("1 < 5"), true);
		assertEquals(expr("5 < 5"), false);
		assertEquals(expr("+5 < -7"), false);
		assertEquals(expr("-5 < -(3)"), true);
		assertEquals(expr("5 == 1"), false);
		assertEquals(expr("5 == 5"), true);
		assertEquals(expr("5 != 5"), false);
		assertEquals(expr("5 != 3"), true);
		// mixing BigDecimal and Integer on either side
		assertEquals(expr("5. == 5"), true);
		assertEquals(expr("5 < 5.1"), true);
	}

	@Test
	public void nullComparison() {
		assertEquals(expr("null == null"), true);
		assertEquals(expr("null != null"), false);
		assertEquals(expr("5 != null"), true);
		assertEquals(expr("5 == null"), false);
		assertEquals(expr("null != 5"), true);
		assertEquals(expr("null == 5"), false);
		assertEquals(expr("null > null"), false);
		assertEquals(expr("null < null"), false);
		assertEquals(expr("null <= null"), false);
		assertEquals(expr("null >= null"), false);
	}

	@Test
	public void arithmeticOperations() {
		assertEquals(expr("5. / 2"), new BigDecimal("2.5"));
		assertEquals(expr("1"), 1);
		assertEquals(expr("5 + 1"), 6);
		// any non-integer promotes the whole result to BigDecimal, also notice how scale is set
		assertEquals(expr("5 + 1."), new BigDecimal("6"));
		assertEquals(expr("5 + 1.0"), new BigDecimal("6")); // trailing zeroes are stripped
		assertEquals(expr("1 - 1.050"), new BigDecimal("-0.05"));
		assertEquals(expr("5 - 6"), -1);
		// integer division
		assertEquals(expr("5 / 2"), 2);
		assertEquals(expr("5 % 2"), 1);
		// floating point division
	}

	@Test
	public void arithmeticScale() {
		assertEquals(expr("1 / 3"), 0); // integer result
		assertEquals(((BigDecimal) expr("1 / 3.")).scale(), ExpressionCalculatorVisitor.DEFAULT_MAX_RESULT_SCALE);
		assertEquals(expr("1 / 4."), new BigDecimal("0.25")); // but trailing zeroes are always trimmed
	}

	@Test
	public void customResultScale() {
		ParseTree parseTree = ExpressionUtils.createParseTree("1 / 3.");
		Object result = new ExpressionCalculatorVisitor(variableResolver)
			.maxResultScale(8)
			.visit(parseTree);
		assertEquals(((BigDecimal) result).scale(), 8);
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor(variableResolver)
			.visit(parseTree);
	}
}
