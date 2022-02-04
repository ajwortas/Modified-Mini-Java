package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;

public class IxRef extends NestedReference{
	public final Expression ixExpr;
	public IxRef(Reference ref, Expression ix, SourcePosition sp) {
		super(ref, sp);
		ixExpr=ix;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitIxReference(this);

	}
}
