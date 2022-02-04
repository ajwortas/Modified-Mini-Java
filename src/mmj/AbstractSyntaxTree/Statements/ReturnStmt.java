package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ReturnStmt extends Statement {
	public final Expression expr;
	public ReturnStmt(Expression expression, SourcePosition sp) {
		super(sp);
		expr=expression;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitReturnStatement(this);
	}
}
