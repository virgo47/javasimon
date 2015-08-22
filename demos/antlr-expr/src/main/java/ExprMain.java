import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Should I cache parseTree for repeting use? Example: "(three + 5) / 3"
 * 1M parse+visit: ~9.8 s; 1M visit <0.7 s
 * Yes, we should cache it, otherwise parsing may take ~93%
 * Note that this depends on visit logic - the more complex it is, the lower the "savings",
 * but in any case they are substantial.
 */
public class ExprMain {

	public static void main(String[] args) {
		String expr = "(three + 5) / 3";
		ParseTree parseTree = parseTree(expr);

		System.out.println("VISITOR:");
		Object result = new ExpressionCalculatorVisitor().visit(parseTree);
		System.out.println("result = " + result);

		// performance test
		sillyBenchmark(expr);
	}

	private static ParseTree parseTree(String expr) {
		ANTLRInputStream input = new ANTLRInputStream(expr);
		ExprLexer lexer = new ExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		return parser.expr();
	}

	private static void sillyBenchmark(String expr) {
		ParseTree parseTree = parseTree(expr);
		Object result = null;
		for (int times = 0; times < 3; times++) {
			long ms = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
//				parseTree = parseTree(expr);
				result = new ExpressionCalculatorVisitor().visit(parseTree);
			}
			ms = System.currentTimeMillis() - ms;
			System.out.println("ms = " + ms);
		}
		System.out.println("result = " + result);
	}
}
