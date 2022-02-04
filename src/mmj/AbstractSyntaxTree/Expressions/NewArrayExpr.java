package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public class NewArrayExpr extends Expression{
	public final TypeDenoter elType;
	public final ExpressionList size;
	public NewArrayExpr(TypeDenoter elementType, ExpressionList expr, SourcePosition sp) {
		super(sp);
		elType = elementType;
		size=expr;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitNewArrayExpression(this);
	}
}
