package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ArrayLiteral extends Expression{

	public final ExpressionList exprList;
	
	public ArrayLiteral(ExpressionList list, SourcePosition sp) {
		super(sp);
		exprList=list;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitArrayLiteral(this);
	}
}