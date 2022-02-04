package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.SyntacticAnalyzer.SourcePosition;

public class RefExpr extends Expression {
	public final Reference ref;
	public RefExpr(Reference ref,SourcePosition sp) {
		super(sp);
		this.ref=ref;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitReferenceExpression(this);
	}
}
