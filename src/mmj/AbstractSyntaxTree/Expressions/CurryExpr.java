package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.SyntacticAnalyzer.SourcePosition;

public class CurryExpr extends Expression {
	public final Reference method;
	public final ExpressionList argList;
	public CurryExpr(Reference m, ExpressionList argL, SourcePosition sp) {
		super(sp);
		method=m;
		argList=argL;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitCurryExpression(this);
	}
}
