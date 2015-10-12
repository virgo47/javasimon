package expr2;

import expr2.grammar.ExprParser;
import expr2.grammar.ExprLexer;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

public class ExpressionUtils {

	private static final ANTLRErrorListener ERROR_LISTENER = new ExceptionThrowingErrorListener();

	public static ParseTree createParseTree(String expression) {
		ANTLRInputStream input = new ANTLRInputStream(expression);
		ExprLexer lexer = new ExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(ERROR_LISTENER);
		return parser.result();
	}

	public static class ExceptionThrowingErrorListener extends BaseErrorListener {
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
			int line, int charPositionInLine, String msg, RecognitionException e)
		{
			throw new ExpressionException(msg);
		}
	}
}
