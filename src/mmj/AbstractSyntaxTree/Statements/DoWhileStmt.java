package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;

public class DoWhileStmt extends LoopStmt {
	public DoWhileStmt(Expression c, Statement b, SourcePosition sp) {
		super(c, b, sp);
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitDoWhileStatement(this);
	}
}
