// Generated from C:/work/litterbin/demos/antlr-expr/src/main/java/expr3/grammar\Expr.g4 by ANTLR 4.5.1
package expr3.grammar;
import expr3.grammar.*;
import expr3.grammar.ExprVisitor;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, OP_LT=3, OP_GT=4, OP_LE=5, OP_GE=6, OP_EQ=7, OP_NE=8,
		OP_AND=9, OP_OR=10, OP_NOT=11, OP_ADD=12, OP_SUB=13, OP_MUL=14, OP_DIV=15,
		OP_MOD=16, NULL_LITERAL=17, BOOLEAN_LITERAL=18, NUMERIC_LITERAL=19, STRING_LITERAL=20,
		ID=21, WS=22;
	public static final int
		RULE_result = 0, RULE_expr = 1;
	public static final String[] ruleNames = {
		"result", "expr"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", null, null, null, null, null, null, null, null, null,
		"'+'", "'-'", "'*'", "'/'", "'%'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "OP_LT", "OP_GT", "OP_LE", "OP_GE", "OP_EQ", "OP_NE",
		"OP_AND", "OP_OR", "OP_NOT", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "OP_MOD",
		"NULL_LITERAL", "BOOLEAN_LITERAL", "NUMERIC_LITERAL", "STRING_LITERAL",
		"ID", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Expr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExprParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ResultContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultContext result() throws RecognitionException {
		ResultContext _localctx = new ResultContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_result);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(4);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }

		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class UnarySignContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnarySignContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitUnarySign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParensContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullLiteralContext extends ExprContext {
		public TerminalNode NULL_LITERAL() { return getToken(ExprParser.NULL_LITERAL, 0); }
		public NullLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLiteralContext extends ExprContext {
		public TerminalNode STRING_LITERAL() { return getToken(expr3.grammar.ExprParser.STRING_LITERAL, 0); }
		public StringLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableContext extends ExprContext {
		public TerminalNode ID() { return getToken(expr3.grammar.ExprParser.ID, 0); }
		public VariableContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicOpContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OP_AND() { return getToken(expr3.grammar.ExprParser.OP_AND, 0); }
		public TerminalNode OP_OR() { return getToken(expr3.grammar.ExprParser.OP_OR, 0); }
		public LogicOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitLogicOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComparisonOpContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OP_LT() { return getToken(expr3.grammar.ExprParser.OP_LT, 0); }
		public TerminalNode OP_GT() { return getToken(expr3.grammar.ExprParser.OP_GT, 0); }
		public TerminalNode OP_EQ() { return getToken(expr3.grammar.ExprParser.OP_EQ, 0); }
		public TerminalNode OP_NE() { return getToken(expr3.grammar.ExprParser.OP_NE, 0); }
		public TerminalNode OP_LE() { return getToken(expr3.grammar.ExprParser.OP_LE, 0); }
		public TerminalNode OP_GE() { return getToken(expr3.grammar.ExprParser.OP_GE, 0); }
		public ComparisonOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitComparisonOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArithmeticOpContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class, i);
		}
		public TerminalNode OP_MUL() { return getToken(expr3.grammar.ExprParser.OP_MUL, 0); }
		public TerminalNode OP_DIV() { return getToken(expr3.grammar.ExprParser.OP_DIV, 0); }
		public TerminalNode OP_MOD() { return getToken(expr3.grammar.ExprParser.OP_MOD, 0); }
		public TerminalNode OP_ADD() { return getToken(expr3.grammar.ExprParser.OP_ADD, 0); }
		public TerminalNode OP_SUB() { return getToken(expr3.grammar.ExprParser.OP_SUB, 0); }
		public ArithmeticOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitArithmeticOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanLiteralContext extends ExprContext {
		public TerminalNode BOOLEAN_LITERAL() { return getToken(expr3.grammar.ExprParser.BOOLEAN_LITERAL, 0); }
		public BooleanLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumericLiteralContext extends ExprContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(expr3.grammar.ExprParser.NUMERIC_LITERAL, 0); }
		public NumericLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((expr3.grammar.ExprVisitor<? extends T>)visitor).visitNumericLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicNotContext extends ExprContext {
		public TerminalNode OP_NOT() { return getToken(expr3.grammar.ExprParser.OP_NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class, 0);
		}
		public LogicNotContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof expr3.grammar.ExprVisitor) return ((ExprVisitor<? extends T>)visitor).visitLogicNot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			switch (_input.LA(1)) {
			case OP_ADD:
			case OP_SUB:
				{
				_localctx = new UnarySignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(7);
				((UnarySignContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==OP_ADD || _la==OP_SUB) ) {
					((UnarySignContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(8);
				expr(8);
				}
				break;
			case OP_NOT:
				{
				_localctx = new LogicNotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(9);
				match(OP_NOT);
				setState(10);
				expr(4);
				}
				break;
			case STRING_LITERAL:
				{
				_localctx = new StringLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(11);
				match(STRING_LITERAL);
				}
				break;
			case BOOLEAN_LITERAL:
				{
				_localctx = new BooleanLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(12);
				match(BOOLEAN_LITERAL);
				}
				break;
			case NUMERIC_LITERAL:
				{
				_localctx = new NumericLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(13);
				match(NUMERIC_LITERAL);
				}
				break;
			case NULL_LITERAL:
				{
				_localctx = new NullLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(14);
				match(NULL_LITERAL);
				}
				break;
			case ID:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(15);
				match(ID);
				}
				break;
			case T__0:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(16);
				match(T__0);
				setState(17);
				expr(0);
				setState(18);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(36);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(34);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new ArithmeticOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(22);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(23);
						((ArithmeticOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OP_MUL) | (1L << OP_DIV) | (1L << OP_MOD))) != 0)) ) {
							((ArithmeticOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(24);
						expr(8);
						}
						break;
					case 2:
						{
						_localctx = new ArithmeticOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(25);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(26);
						((ArithmeticOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==OP_ADD || _la==OP_SUB) ) {
							((ArithmeticOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(27);
						expr(7);
						}
						break;
					case 3:
						{
						_localctx = new ComparisonOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(28);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(29);
						((ComparisonOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OP_LT) | (1L << OP_GT) | (1L << OP_LE) | (1L << OP_GE) | (1L << OP_EQ) | (1L << OP_NE))) != 0)) ) {
							((ComparisonOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(30);
						expr(6);
						}
						break;
					case 4:
						{
						_localctx = new LogicOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(31);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(32);
						((LogicOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==OP_AND || _la==OP_OR) ) {
							((LogicOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(33);
						expr(4);
						}
						break;
					}
					}
				}
				setState(38);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\30*\4\2\t\2\4\3\t"+
		"\3\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\27\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3%\n\3\f\3\16"+
		"\3(\13\3\3\3\2\3\4\4\2\4\2\6\3\2\16\17\3\2\20\22\3\2\5\n\3\2\13\f\62\2"+
		"\6\3\2\2\2\4\26\3\2\2\2\6\7\5\4\3\2\7\3\3\2\2\2\b\t\b\3\1\2\t\n\t\2\2"+
		"\2\n\27\5\4\3\n\13\f\7\r\2\2\f\27\5\4\3\6\r\27\7\26\2\2\16\27\7\24\2\2"+
		"\17\27\7\25\2\2\20\27\7\23\2\2\21\27\7\27\2\2\22\23\7\3\2\2\23\24\5\4"+
		"\3\2\24\25\7\4\2\2\25\27\3\2\2\2\26\b\3\2\2\2\26\13\3\2\2\2\26\r\3\2\2"+
		"\2\26\16\3\2\2\2\26\17\3\2\2\2\26\20\3\2\2\2\26\21\3\2\2\2\26\22\3\2\2"+
		"\2\27&\3\2\2\2\30\31\f\t\2\2\31\32\t\3\2\2\32%\5\4\3\n\33\34\f\b\2\2\34"+
		"\35\t\2\2\2\35%\5\4\3\t\36\37\f\7\2\2\37 \t\4\2\2 %\5\4\3\b!\"\f\5\2\2"+
		"\"#\t\5\2\2#%\5\4\3\6$\30\3\2\2\2$\33\3\2\2\2$\36\3\2\2\2$!\3\2\2\2%("+
		"\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'\5\3\2\2\2(&\3\2\2\2\5\26$&";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}