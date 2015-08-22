grammar Expr;

expr: expr op=('*'|'/') expr # arithmetic
	| expr op=('+'|'-') expr # arithmetic
	| INT # int
	| ID # id
	| '(' expr ')' # parens
	;

OP_ADD: '+';
OP_SUB: '-';
OP_MUL: '*';
OP_DIV: '/';

INT: [0-9]+;
ID: [a-zA-Z_]+;
WS: [ \t\r\n]+ -> skip;