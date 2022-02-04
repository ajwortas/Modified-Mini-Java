package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Declarations.VarDecl;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.AbstractSyntaxTree.Expressions.RangeExpr;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ForAllStmt extends ForStmt {
	public ForAllStmt(Statement initial, Expression cond, Statement increment, Statement b, SourcePosition sp) {
		super(initial, cond, increment, b, sp);
	}
	public ForAllStmt(VarDecl var, RangeExpr range, Statement b, SourcePosition sp) {
		super(var,range,b,sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitForAllStmt(this);
	}
}
