package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Terminals.Operator;
import mmj.SyntacticAnalyzer.SourcePosition;

public class UnaryExpr extends Expression {

	public final Operator op;
	public final Expression expr;
	
	public UnaryExpr(Expression expression, Operator op, SourcePosition sp) {
		super(sp);
		this.op=op;
		expr=expression;
		
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitUnaryExpression(this);

	}

}
