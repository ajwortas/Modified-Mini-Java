package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.MethodComponents;
import mmj.SyntacticAnalyzer.SourcePosition;

public class NewMethodExpr extends Expression {
	public final MethodComponents newMethod;
	public NewMethodExpr(MethodComponents mt, SourcePosition sp) {
		super(sp);
		newMethod=mt;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitNewMethodExpression(this);
	}

}
