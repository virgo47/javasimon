/*
Use IDEA with ANTLR v4 plugin to generate classes:
- first set-up (right-click on the file, Configure ANTLR...)
- setup output directory to match this modules src/main/java
- set package for generated classes: expr1.grammar
- check "generate parse tree visitor", uncheck listener above (not necessary)

Command line when using downloaded "Complete ANTLR 4.5.1 Java binaries jar" (version may vary):
- know where the JAR is downloaded
- run the command in the expr1/grammar package directory (where Expr.g4 is)
- java -jar ~/Downloads/antlr-4.5.1-complete.jar -o . -package expr1.grammar -visitor -no-listener Expr.g4
- -o . means this directory (can be omitted), in IDEA it somehow adds package to the output directory
  but this command does not do that, it merely sets package in sources

*.tokens files are NOT necessary for runtime, they are not added to version control
*/
grammar Expr;

expr: expr op=(OP_MUL | OP_DIV) expr # arithmetic
	| expr op=(OP_ADD | OP_SUB) expr # arithmetic
	| INT # int
	| '(' expr ')' # parens
	;

OP_ADD: '+';
OP_SUB: '-';
OP_MUL: '*';
OP_DIV: '/';

INT: [0-9]+;
WS: [ \t\r\n]+ -> skip;