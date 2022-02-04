package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;

abstract class LoopStmt extends Statement {
	public final Statement body;
	public final Expression cond;
	public LoopStmt(Expression c, Statement b, SourcePosition sp) {
		super(sp);
		cond=c;
		body=b;
	}
}
