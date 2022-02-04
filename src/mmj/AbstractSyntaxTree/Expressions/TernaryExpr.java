package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.SourcePosition;

public class TernaryExpr extends Expression{
	public final Expression cond,ifTrue,ifFalse;
	public TernaryExpr(Expression cond, Expression ifTrue, Expression ifFalse, SourcePosition sp) {
		super(sp);
		this.cond=cond;
		this.ifTrue=ifTrue;
		this.ifFalse=ifFalse;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitTurnaryExpression(this);
	}

}
