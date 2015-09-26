/*
Use IDEA with ANTLR v4 plugin to generate classes:
- first set-up (right-click on the file, Configure ANTLR...)
- setup output directory to match this modules src/main/java
- set package for generated classes: expr2.grammar
- check "generate parse tree visitor", uncheck listener above (not necessary)

Command line when using downloaded "Complete ANTLR 4.5.1 Java binaries jar" (version may vary):
- know where the JAR is downloaded
- run the command in the expr1/grammar package directory (where Expr.g4 is)
- java -jar ~/Downloads/antlr-4.5.1-complete.jar -o . -package expr2.grammar -visitor -no-listener Expr.g4
- -o . means this directory (can be omitted), in IDEA it somehow adds package to the output directory
  but this command does not do that, it merely sets package in sources

*.tokens files are NOT necessary for runtime, they are not added to version control
*/
grammar Expr;

result: expr;

expr: STRING_LITERAL # stringLiteral
   	| BOOLEAN_LITERAL # booleanLiteral
   	| NUMERIC_LITERAL # numericLiteral
	| op=('-' | '+') expr # unarySign
	| expr op=(OP_MUL | OP_DIV | OP_MOD) expr # arithmeticOp
	| expr op=(OP_ADD | OP_SUB) expr # arithmeticOp
	| expr op=(OP_LT | OP_GT | OP_EQ | OP_NE | OP_LE | OP_GE) expr # comparisonOp
	| OP_NOT expr # logicNot
	| expr op=(OP_AND | OP_OR) expr # logicOp
	| '(' expr ')' # parens
	;

OP_LT: L T | '<';
OP_GT: G T | '>';
OP_LE: L E | '<=';
OP_GE: G E | '>=';
OP_EQ: E Q | '=' '='?;
OP_NE: N E | N E Q | '!=' | '<>';
OP_AND: A N D | '&&';
OP_OR: O R | '||';
OP_NOT: N O T | '!';
OP_ADD: '+';
OP_SUB: '-';
OP_MUL: '*';
OP_DIV: '/';
OP_MOD: '%';

BOOLEAN_LITERAL: T R U E | T
	| F A L S E | F
	;

NUMERIC_LITERAL : DIGIT+ ( '.' DIGIT* )? ( E [-+]? DIGIT+ )?
	| '.' DIGIT+ ( E [-+]? DIGIT+ )?
	;

STRING_LITERAL : '\'' ( ~'\'' | '\'\'' )* '\'';

WS: [ \t\r\n]+ -> skip;

fragment DIGIT : [0-9];

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];