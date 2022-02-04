package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Terminals.Operator;
import mmj.SyntacticAnalyzer.SourcePosition;

public class BinaryExpr extends Expression {

	public final Expression left,right;
	public final Operator op;
	
	public BinaryExpr(Expression exprL, Operator oper, Expression exprR, SourcePosition sp) {
		super(sp);
		left=exprL;
		right=exprR;
		op=oper;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitBinaryExpression(this);
	}

}
