package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.AbstractSyntaxTree.TypeDenoters.ClassType;
import mmj.SyntacticAnalyzer.SourcePosition;

public class NewObjectExpr extends Expression{

	public final ClassType ct;
	public final ExpressionList argList;
	
	public NewObjectExpr(ClassType clazz, ExpressionList argL, SourcePosition sp) {
		super(sp);
		ct=clazz;
		argList=argL;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitNewObjectExpression(this);
	}

}
