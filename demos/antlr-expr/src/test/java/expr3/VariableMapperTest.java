package expr3;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import expr3.bonus.VariableMapper;
import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class VariableMapperTest {

private VariableMapper<SomeBean> variableMapper;
private ParseTree myNameExpression;
private ParseTree myCountExpression;

@BeforeClass
public void init() {
	variableMapper = new VariableMapper<SomeBean>()
		.set("myName", o -> o.name)
		.set("myCount", SomeBean::getCount);
	myNameExpression = ExpressionUtils.createParseTree("myName <= 'Virgo'");
	myCountExpression = ExpressionUtils.createParseTree("myCount * 3");
}

@Test
public void myNameExpressionTest() {
	SomeBean bean = new SomeBean();
	ExpressionCalculatorVisitor visitor = new ExpressionCalculatorVisitor(
		var -> variableMapper.resolveVariable(var, bean));

	assertEquals(visitor.visit(myNameExpression), false); // null comparison is false
	bean.name = "Virgo";
	assertEquals(visitor.visit(myNameExpression), true);
	bean.name = "ABBA";
	assertEquals(visitor.visit(myNameExpression), true);
	bean.name = "Virgo47";
	assertEquals(visitor.visit(myNameExpression), false);
}

@Test
public void myCountExpressionTest() {
	SomeBean bean = new SomeBean();
	ExpressionCalculatorVisitor visitor = new ExpressionCalculatorVisitor(
		var -> variableMapper.resolveVariable(var, bean));

//		assertEquals(visitor.visit(myCountExpression), false); // NPE!
	bean.setCount(3f);
	assertEquals(visitor.visit(myCountExpression), new BigDecimal("9"));
	bean.setCount(-1.1f);
	assertEquals(visitor.visit(myCountExpression), new BigDecimal("-3.3"));
}

public static class SomeBean {
	public String name;
	private Float count;

	public Float getCount() {
		return count;
	}

	public void setCount(Float count) {
		this.count = count;
	}
}
}
